package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceValidationException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Speech;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ITextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.talk.AudioPlayer;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Service
public class SimpleTextToSpeechService implements ITextToSpeechService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String WELCOME = "Hallo und willkommen bei Lerntia. Schöön, dass du hier bist!";
    private final String ANSWER = "Antwort nummer ";
    private final String VOICE = "bits3-hsmm";
    private final String BREAK = "....";
    private final String DP = ": ";
    private AudioPlayer audioPlayer;
    private MaryInterface marytts;
    private boolean singleAnswer = false;

    @Override
    public void playWelcomeText() throws TextToSpeechServiceException {
        LOG.trace("Entering method playWelcomeText.");
        try {
            marytts = new LocalMaryInterface();
            marytts.setVoice(VOICE);
        } catch (MaryConfigurationException e) {
            LOG.error("Failed to initialize speech synthesizer!");
            throw new TextToSpeechServiceException("Failed to initialize the speech synthesizer.");
        }
        playText(WELCOME);
    }

    @Override
    public void readQuestionAndAnswers(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        LOG.trace("Entering method readQuestionAndAnswers.");
        if (marytts != null) {
            LOG.trace("marytts is NOT null. Calling stopSpeaking method.");
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
                LOG.trace("marytts is null. Creating a new mary interface.");
                marytts = new LocalMaryInterface();
                LOG.trace("mary interface is created. marytts is not null anymore.");
                marytts.setVoice(VOICE);
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
        try (var audio = marytts.generateAudio(filterTextInParenthesis(text))) {
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

    private String filterTextInParenthesis(String text) {
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
        marytts.setVoice(textToSpeech.getVoice());
    }

    private String getText(Speech textToSpeech) throws TextToSpeechServiceValidationException {
        if (emptyQuestionAndAnswer(textToSpeech)) {
            throw new TextToSpeechServiceValidationException("All questions and answers are empty. Nothing to read!");
        }
        var out = "";
        out += isValidText(textToSpeech.getQuestion()) ? textToSpeech.getQuestion() : "" + '\n';
        out += BREAK + ((isValidText(textToSpeech.getAnswer1())) ? (ANSWER + answerNumber.eins + DP + textToSpeech.getAnswer1() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer2())) ? (ANSWER + answerNumber.zwei + DP + textToSpeech.getAnswer2() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer3())) ? (ANSWER + answerNumber.drei + DP + textToSpeech.getAnswer3() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer4())) ? (ANSWER + answerNumber.vier + DP + textToSpeech.getAnswer4() + '\n') : "");
        out += BREAK + ((isValidText(textToSpeech.getAnswer5())) ? (ANSWER + answerNumber.fünf + DP + textToSpeech.getAnswer5() + '\n') : "");
        LOG.debug(out);
        return out;
    }

    private boolean emptyQuestionAndAnswer(Speech textToSpeech) {
        return !isValidText(textToSpeech.getQuestion()) &&
            !isValidText(textToSpeech.getAnswer1()) &&
            !isValidText(textToSpeech.getAnswer2()) &&
            !isValidText(textToSpeech.getAnswer3()) &&
            !isValidText(textToSpeech.getAnswer4()) &&
            !isValidText(textToSpeech.getAnswer5());
    }

    private boolean isValidText(String text) {
        return text != null && text.trim().length() > 0;
    }


    public enum answerNumber {
        eins, zwei, drei, vier, fünf
    }

}
