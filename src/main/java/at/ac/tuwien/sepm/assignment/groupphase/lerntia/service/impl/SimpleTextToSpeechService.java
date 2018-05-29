package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceValidationException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Speech;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ITextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.talk.AudioPlayer;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Service
public class SimpleTextToSpeechService implements ITextToSpeechService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ConfigReader configReaderSpeech = new ConfigReader("speech");

    private final String WELCOME = configReaderSpeech.getValue("welcomeText");
    private final String ANSWER = configReaderSpeech.getValue("answerPrefix");
    private final String VOICE = configReaderSpeech.getValue("voice");
    private final String BREAK = configReaderSpeech.getValue("break");
    private final boolean playWelcomeText = configReaderSpeech.getValueBoolean("playWelcomeText");

    private AudioPlayer audioPlayer;
    private MaryInterface maryTTS;
    private boolean singleAnswer = false;

    @Override
    public void playWelcomeText() throws TextToSpeechServiceException {
        LOG.trace("Entering method playWelcomeText.");
        try {
            maryTTS = new LocalMaryInterface();
            maryTTS.setVoice(VOICE);
        } catch (MaryConfigurationException e) {
            LOG.error("Failed to initialize speech synthesizer!");
            throw new TextToSpeechServiceException("Failed to initialize the speech synthesizer.");
        }
        if (playWelcomeText) {
            playText(WELCOME);
        }
    }

    @Override
    public void readQuestionAndAnswers(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        LOG.trace("Entering method readQuestionAndAnswers.");
        if (maryTTS != null) {
            LOG.trace("maryTTS is NOT null. Calling stopSpeaking method.");
            stopSpeaking();
            LOG.trace("stopSpeaking method is called. Calling playText method.");
            if (!singleAnswer) {
                playText(getText(textToSpeech));
            } else {
                playText(textToSpeech.getSingleAnswer());
                singleAnswer = false;
            }
            LOG.trace("playText method is called.");
        } else {
            try {
                LOG.trace("maryTTS is null. Creating a new mary interface.");
                maryTTS = new LocalMaryInterface();
                LOG.trace("mary interface is created. maryTTS is not null anymore.");
                maryTTS.setVoice(VOICE);
                if (!singleAnswer) {
                    playText(getText(textToSpeech));
                } else {
                    playText(textToSpeech.getSingleAnswer());
                    singleAnswer = false;
                }
            } catch (MaryConfigurationException e) {
                LOG.error("Failed to initialize speech synthesizer!");
                throw new TextToSpeechServiceException("Failed to initialize the speech synthesizer.");
            }
        }
    }

    @Override
    public void readSingleAnswer(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        LOG.trace("Entering method readSingleAnswer.");
        singleAnswer = true;
        readQuestionAndAnswers(textToSpeech);
    }

    private void playText(String text) throws TextToSpeechServiceException {
        LOG.trace("Entering method playText.");
        try (var audio = maryTTS.generateAudio(replaceUmlauts(filterTextInParenthesis(text)))) {
            LOG.trace("Creating and setting a new audioPlayer.");
            audioPlayer = new AudioPlayer();
            audioPlayer.setAudio(audio);
            audioPlayer.setGain(1);
            audioPlayer.setDaemon(false);
            audioPlayer.start();
            LOG.info("audioPlayer successfully created and started.");

        } catch (SynthesisException e) {
            LOG.error("Failed to generate audio with the speech synthesizer!");
            throw new TextToSpeechServiceException("Failed to generate audio.");

        } catch (IOException e) {
            LOG.error("Failed or interrupted IO operation occurred during speech synthesis.");
            throw new TextToSpeechServiceException("Failed or interrupted IO operation.");
        }
    }

    public String filterTextInParenthesis(String text) {
        var counter = 0;
        var filtered = new StringBuilder();
        for (var p : text.toCharArray()) {
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
            LOG.info("Stopping speech synthesis.");
        } else {
            LOG.debug("audioPlayer is already null.");
        }
    }

    @Override
    public void setVoice(Speech textToSpeech) {
        maryTTS.setVoice(textToSpeech.getVoice());
    }

    @Override
    public boolean noCurrentAudio() {
        return audioPlayer == null || audioPlayer.finishedAudio;
    }

    public String getText(Speech textToSpeech) throws TextToSpeechServiceValidationException {
        if (emptyQuestionAndAnswer(textToSpeech)) {
            throw new TextToSpeechServiceValidationException("All questions and answers are empty. Nothing to read!");
        }
        var out = "";
        out += isValidText(textToSpeech.getQuestion()) ? textToSpeech.getQuestion() : "" + '\n';
        out += BREAK + ((isValidText(textToSpeech.getAnswer1())) ? (ANSWER + answerNumber.eins + BREAK + textToSpeech.getAnswer1() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer2())) ? (ANSWER + answerNumber.zwei + BREAK + textToSpeech.getAnswer2() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer3())) ? (ANSWER + answerNumber.drei + BREAK + textToSpeech.getAnswer3() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer4())) ? (ANSWER + answerNumber.vier + BREAK + textToSpeech.getAnswer4() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer5())) ? (textToSpeech.getAnswer5() + '\n') : "");
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


    public enum answerNumber {
        eins, zwei, drei, vier, fünf
    }

}
