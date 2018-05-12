package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

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
    private final String WELCOME = "Hallo (das) und (hier) willkommen (wird) bei (nicht) Lerntia. Schöön, dass (ausgesprochen) du hier bist!";
    private final String ANSWER = "Antwort nummer ";
    private final String VOICE = "bits3-hsmm";
    private final String BREAK = "....";
    private final String DP = ": ";
    private AudioPlayer audioPlayer;
    private MaryInterface marytts;
    private boolean singleAnswer = false;

    @Override
    public void playWelcomeText() {
        LOG.trace("Entering method playWelcomeText.");
        try {
            marytts = new LocalMaryInterface();
            marytts.setVoice(VOICE);
        } catch (MaryConfigurationException e) {
            LOG.error("Failed to initialize speech synthesizer: " + e.getMessage());
        }
        //playText(WELCOME);
    }

    @Override
    public void readQuestionAndAnswers(Speech textToSpeech) {
        LOG.trace("Entering method speak.");
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
                playText(getText(textToSpeech));
            } catch (MaryConfigurationException e) {
                LOG.error("Failed to initialize speech synthesizer: " + e.getMessage());
            } catch (Exception e) {
                LOG.error("Failed to play text with speech synthesizer: " + e.getMessage());
            }
        }
    }

    @Override
    public void readSingleAnswer(Speech textToSpeech) {
        LOG.trace("Entering method readSingleAnswer.");
        singleAnswer = true;
        readQuestionAndAnswers(textToSpeech);
    }

    private void playText(String text) {
        LOG.trace("Entering method playText.");
        try (var audio = marytts.generateAudio(filterTextInParenthesis(text))) {
            LOG.trace("Creating and setting a new audioPlayer.");
            audioPlayer = new AudioPlayer();
            audioPlayer.setAudio(audio);
            audioPlayer.setGain(1);
            audioPlayer.setDaemon(false);
            audioPlayer.start();
            LOG.debug("audioPlayer successfully created and started.");
        } catch (SynthesisException e) {
            LOG.error("Failed to generate audio with speech synthesizer: " + e.getMessage());
        } catch (IOException e) {
            LOG.error("Failed or interrupted IO operation occurred: " + e.getMessage());
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
        LOG.debug("Entering method stopSpeaking.");
        if (audioPlayer != null) {
            audioPlayer.cancel();
            LOG.debug("Cancelling speech.");
        } else {
            LOG.debug("audioPlayer is already null.");
        }
    }

    @Override
    public void setVoice(Speech textToSpeech) {
        marytts.setVoice(textToSpeech.getVoice());
    }

    private String getText(Speech textToSpeech) {
        return textToSpeech.getQuestion() + BREAK + '\n'
            + BREAK + ANSWER + answerNumber.eins + DP + textToSpeech.getAnswer1() + '\n'
            + BREAK + ANSWER + answerNumber.zwei + DP + textToSpeech.getAnswer2() + '\n'
            + BREAK + ANSWER + answerNumber.drei + DP + textToSpeech.getAnswer3() + '\n'
            + BREAK + ANSWER + answerNumber.vier + DP + textToSpeech.getAnswer4() + '\n'
            + BREAK + ANSWER + answerNumber.fünf + DP + textToSpeech.getAnswer5();
    }

    public enum answerNumber {
        eins, zwei, drei, vier, fünf
    }

}
