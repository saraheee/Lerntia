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
public class SimpleTextToSpeechService extends Thread implements ITextToSpeechService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String WELCOME = "Hallo und willkommen bei Lerntia. Schöön, dass du hier bist!";
    private final String ANSWER = "Antwort nummer ";
    private final String VOICE = "bits3-hsmm";
    private final String BREAK = "....";
    private final String DP = ": ";
    private AudioPlayer audioPlayer;
    private MaryInterface marytts;

    public enum answerNumber {
        eins, zwei, drei, vier, fünf
    }

    @Override
    public void playWelcomeText() {
        try {
            marytts = new LocalMaryInterface();
            marytts.setVoice(VOICE);
        } catch (MaryConfigurationException e) {
            LOG.error("Failed to initialize speech synthesizer: " + e.getMessage());
        }
        playText(WELCOME);
    }

    @Override
    public void speak(Speech textToSpeech) {
        if (marytts != null) {
            stopSpeaking();
            playText(getText(textToSpeech));
        } else {
            try {
                marytts = new LocalMaryInterface();
                marytts.setVoice(VOICE);
            } catch (MaryConfigurationException e) {
                LOG.error("Failed to initialize speech synthesizer: " + e.getMessage());
            } catch (Exception e) {
                LOG.error("Failed to play text with speech synthesizer: " + e.getMessage());
            }
        }
    }

    private void playText(String text) {
        try (var audio = marytts.generateAudio(text)) {
            audioPlayer = new AudioPlayer();
            audioPlayer.setAudio(audio);
            audioPlayer.setGain(1);
            audioPlayer.setDaemon(false);
            audioPlayer.start();
        } catch (SynthesisException e) {
            LOG.error("Failed to generate audio with speech synthesizer: " + e.getMessage());
        } catch (IOException e) {
            LOG.error("Failed or interrupted IO operation occurred: " + e.getMessage());
        }
    }

    @Override
    public void stopSpeaking() {
        if (audioPlayer != null) {
            audioPlayer.cancel();
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


    @Override
    public void run() {



    }

}
