package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.ConfigReaderException;
import at.ac.tuwien.lerntia.exception.TextToSpeechServiceException;
import at.ac.tuwien.lerntia.exception.TextToSpeechServiceValidationException;
import at.ac.tuwien.lerntia.lerntia.dto.Speech;
import at.ac.tuwien.lerntia.lerntia.service.ITextToSpeechService;
import at.ac.tuwien.lerntia.lerntia.talk.AudioPlayer;
import at.ac.tuwien.lerntia.util.ConfigReader;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.MaryRuntimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioInputStream;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextToSpeechService implements ITextToSpeechService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ConfigReader configReaderSpeech = null;
    private Map<String, String> dictionary = null;
    private String WELCOME = "Hallo und willkommen bei Lerntia. Schöön, dass du hier bist!";
    private String ANSWER = "Antwort nummer";
    private String VOICE_DE = "bits3-hsmm";
    private String PREFIX_DE = "Antwort";
    private String VOICE = "bits3-hsmm";
    private String BREAK = "....";
    private boolean english = false;

    private AudioPlayer audioPlayer;
    private MaryInterface maryTTS;
    private boolean singleAnswer = false;
    private boolean feedbackText = false;
    private boolean notInitialized = false;
    private boolean playWelcomeText = false;

    @Override
    public void playWelcomeText() throws TextToSpeechServiceException {
        createConfigReader();

        WELCOME = configReaderSpeech.getValue("welcomeText") != null ? configReaderSpeech.getValue("welcomeText") : WELCOME;
        ANSWER = configReaderSpeech.getValue("answerPrefix") != null ? configReaderSpeech.getValue("answerPrefix") : ANSWER;
        VOICE = configReaderSpeech.getValue("voice") != null ? configReaderSpeech.getValue("voice").trim() : VOICE;
        BREAK = configReaderSpeech.getValue("break") != null ? configReaderSpeech.getValue("break") : BREAK;
        playWelcomeText = configReaderSpeech.getValueBoolean("playWelcomeText") != null ? configReaderSpeech.getValueBoolean("playWelcomeText") : playWelcomeText;
        english = !ANSWER.startsWith(PREFIX_DE);

        try { // mark first button after starting the application
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_TAB);
        } catch (AWTException e) {
            LOG.error("Failed to go to first button after starting the application! " + e.getLocalizedMessage());
        }

        LOG.trace("Entering method playWelcomeText.");
        try {
            maryTTS = new LocalMaryInterface();
            if (VOICE != null) {
                maryTTS.setVoice(VOICE);
            }
        } catch (MaryConfigurationException e) {
            notInitialized = true;
            LOG.error("Failed to initialize speech synthesizer!");
            throw new TextToSpeechServiceException("Failed to initialize the speech synthesizer: " + e.getLocalizedMessage());
        }
        if (playWelcomeText) {
            playText(WELCOME);
        }
    }

    public boolean isEnglish() {
        return english;
    }

    public void setEnglish(boolean en) {
        this.english = en;
    }

    @Override
    public void readQuestionAndAnswers(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        createConfigReader();
        ANSWER = configReaderSpeech.getValue("answerPrefix") != null ? configReaderSpeech.getValue("answerPrefix") : ANSWER;
        VOICE = configReaderSpeech.getValue("voice") != null ? configReaderSpeech.getValue("voice").trim() : VOICE;
        BREAK = configReaderSpeech.getValue("break") != null ? configReaderSpeech.getValue("break") : BREAK;
        english = !ANSWER.startsWith(PREFIX_DE);

        LOG.trace("Entering method readQuestionAndAnswers.");
        if (maryTTS != null) {
            LOG.trace("maryTTS is NOT null. Calling stopSpeaking method.");
            stopSpeaking();
            try {
                MaryRuntimeUtils.ensureMaryStarted();
            } catch (Exception e) {
                notInitialized = true;
                throw new TextToSpeechServiceException("Speech synthesizer is not started!");
            }
            if (VOICE != null) {
                setVoice(textToSpeech);
            }
            getTextToRead(textToSpeech);
            LOG.trace("playText method is called.");
        } else {
            try {
                LOG.trace("maryTTS is null. Creating a new mary interface.");
                maryTTS = new LocalMaryInterface();
                LOG.trace("mary interface is created. maryTTS is not null anymore.");
                MaryRuntimeUtils.ensureMaryStarted();
                if (VOICE != null) {
                    setVoice(textToSpeech);
                }
                getTextToRead(textToSpeech);
                notInitialized = false;
            } catch (MaryConfigurationException e) {
                notInitialized = true;
                LOG.error("Failed to initialize speech synthesizer!");
                throw new TextToSpeechServiceException("Failed to initialize the speech synthesizer: " + e.getLocalizedMessage());
            } catch (Exception e) {
                notInitialized = true;
                throw new TextToSpeechServiceException("Speech synthesizer is not started!");
            }
        }
    }

    private void createConfigReader() throws TextToSpeechServiceException {
        if (configReaderSpeech == null) {
            LOG.debug("configReaderSpeech is null, trying to add a new one.");
            try {
                configReaderSpeech = new ConfigReader("speech");
            } catch (ConfigReaderException e) {
                throw new TextToSpeechServiceException(e.getCustomMessage());
            }
        }
    }

    private void getTextToRead(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        if (singleAnswer) {
            playText(textToSpeech.getSingleAnswer());
            singleAnswer = false;
        } else if (feedbackText) {
            if (english && textToSpeech.getFeedbackText().contains("|")) {
                maryTTS.setVoice(VOICE);
                playText(textToSpeech.getFeedbackText().substring(textToSpeech.getFeedbackText().indexOf("|")));
            } else {
                playText(textToSpeech.getFeedbackText());
            }
            feedbackText = false;
        } else {
            try {
                MaryRuntimeUtils.ensureMaryStarted();
            } catch (Exception e) {
                notInitialized = true;
                throw new TextToSpeechServiceException("Speech synthesizer is not started: " + e.getLocalizedMessage());
            }
            playText(getQuestionAndAnswerText(textToSpeech));
        }
    }

    @Override
    public void readSingleAnswer(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        LOG.trace("Entering method readSingleAnswer.");
        singleAnswer = true;
        readQuestionAndAnswers(textToSpeech);
    }

    @Override
    public void readFeedbackText(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        LOG.trace("Entering method readFeedbackText.");
        feedbackText = true;
        readQuestionAndAnswers(textToSpeech);
    }

    private void playText(String text) throws TextToSpeechServiceException {
        LOG.trace("Entering method playText.");
        if (notInitialized && !singleAnswer && !feedbackText) {
            throw new TextToSpeechServiceException("Speech synthesizer is not started!");
        } else if (notInitialized) {
            return;
        }
        try (AudioInputStream audio = maryTTS.generateAudio(replaceUmlauts(replaceWordsInDictionary(filterTextInParenthesis(text))))) {
            LOG.trace("Creating and setting a new audioPlayer.");
            audioPlayer = new AudioPlayer();
            audioPlayer.setAudio(audio);
            audioPlayer.setGain(1);
            audioPlayer.setDaemon(false);
            audioPlayer.start();
            LOG.debug("audioPlayer successfully created and started.");

        } catch (SynthesisException e) {
            LOG.error("Failed to generate audio with the speech synthesizer!");
            throw new TextToSpeechServiceException("Failed to generate audio: " + e.getLocalizedMessage());

        } catch (IOException e) {
            LOG.error("Failed or interrupted IO operation occurred during speech synthesis.");
            throw new TextToSpeechServiceException("Failed or interrupted IO operation: " + e.getLocalizedMessage());
        }
    }

    public String replaceWordsInDictionary(String input) throws TextToSpeechServiceException {
        String output = input;
        try {
            dictionary = ConfigReader.readTextFile("dictionary");
        } catch (ConfigReaderException e) {
            throw new TextToSpeechServiceException(e.getCustomMessage());
        }
        if (dictionary != null) {
            for (String word : dictionary.keySet()) {
                int counter = 0;
                int occNumber = occurrencesOfWordInText(output, word);
                while (output.contains(word) && counter < occNumber) {
                    LOG.info("Replacing word '" + word.trim() + "' with '" + dictionary.get(word).trim() + "'");
                    output = output.substring(0, output.indexOf(word.trim())) + dictionary.get(word).trim() + output.substring(output.indexOf(word) + word.trim().length());
                    counter++;
                }
            }
        }
        return output;
    }

    private int occurrencesOfWordInText(String text, String word) {

        int num = 0;
        Pattern pat = Pattern.compile(Pattern.quote(word));
        Matcher m = pat.matcher(text);
        while (m.find()) {
            num++;
        }
        LOG.debug("Number of occurrences of word '" + word + "': " + num);
        return num;
    }

    public String filterTextInParenthesis(String text) {
        int counter = 0;
        StringBuilder filtered = new StringBuilder();
        for (char p : text.toCharArray()) {
            if (p == '(') {
                counter++;
            }
            if (p == ')') {
                counter--;
            }
            if (p != ')' && p != '(' && counter == 0) {
                filtered.append(p);
            }
        }
        return filtered.toString();
    }

    public String replaceUmlauts(String text) {
        return text.replace("ae", "\u00e4")
            .replace("oe", "\u00f6")
            .replace("ue", "\u00fc")
            .replace("sz", "\u00DF");
    }

    @Override
    public void stopSpeaking() {
        LOG.trace("Entering method stopSpeaking.");
        if (audioPlayer != null) {
            audioPlayer.cancel();
            LOG.debug("Stopping speech synthesis.");
        } else {
            LOG.debug("audioPlayer is already null.");
        }
    }

    @Override
    public void setVoice(Speech textToSpeech) {
        textToSpeech.setVoice(VOICE);
        maryTTS.setVoice(textToSpeech.getVoice());
    }

    @Override
    public boolean noCurrentAudio() {
        return audioPlayer == null || audioPlayer.finishedAudio;
    }

    public String getQuestionAndAnswerText(Speech textToSpeech) throws TextToSpeechServiceValidationException {
        if (emptyQuestionAndAnswer(textToSpeech)) {
            throw new TextToSpeechServiceValidationException("All questions and answers are empty. Nothing to read!");
        }
        String out = "";
        out += isValidText(textToSpeech.getQuestion()) ? textToSpeech.getQuestion() : "" + '\n';
        out += BREAK + ((isValidText(textToSpeech.getAnswer1())) ? (" " + ANSWER + (english ? " " + answerNumberEN.one : answerNumberDE.eins) + BREAK + " " + textToSpeech.getAnswer1() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer2())) ? (" " + ANSWER + (english ? " " + answerNumberEN.two : answerNumberDE.zwei) + BREAK + " " + textToSpeech.getAnswer2() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer3())) ? (" " + ANSWER + (english ? " " + answerNumberEN.three : answerNumberDE.drei) + BREAK + " " + textToSpeech.getAnswer3() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer4())) ? (" " + ANSWER + (english ? " " + answerNumberEN.four : answerNumberDE.vier)+ BREAK + " " + textToSpeech.getAnswer4() + '\n') : "");
        out += BREAK +
            (((isValidText(textToSpeech.getAnswer5()) && (textToSpeech.getAnswer5().startsWith("Keine der genannten")))) ? (" " + textToSpeech.getAnswer5() + '\n') :
            ((isValidText(textToSpeech.getAnswer5()) && (textToSpeech.getAnswer5().startsWith("None of these"))) ? (" " + textToSpeech.getAnswer5() + '\n') :
            ((isValidText(textToSpeech.getAnswer5())) ? (" " + ANSWER + (english ? answerNumberEN.five : answerNumberDE.fünf) + BREAK + " " + textToSpeech.getAnswer5() + '\n') : "")));
        LOG.trace(out);
        return out;
    }

    public boolean emptyQuestionAndAnswer(Speech textToSpeech) {
        return !isValidText(textToSpeech.getQuestion()) &&
            !isValidText(textToSpeech.getAnswer1()) &&
            !isValidText(textToSpeech.getAnswer2()) &&
            !isValidText(textToSpeech.getAnswer3()) &&
            !isValidText(textToSpeech.getAnswer4()) &&
            !isValidText(textToSpeech.getAnswer5());
    }

    public boolean isValidText(String text) {
        return text != null && text.trim().length() > 0;
    }


    public enum answerNumberDE {
        eins, zwei, drei, vier, fünf
    }

    public enum answerNumberEN {
        one, two, three, four, five
    }
}
