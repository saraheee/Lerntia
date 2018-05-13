package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.application.MainApplication;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Speech;

public interface ITextToSpeechService {

    /**
     * Plays a first audio output on startup to welcome the user
     *
     * @throws ServiceException if welcome audio can't be played.
     */
    void playWelcomeText() throws ServiceException;


    /**
     * Reads a question and all answers with an audio player
     *
     * @param textToSpeech the textToSpeech object with the properties of the text
     * @throws ServiceException if audio can't be played.
     */
    void readQuestionAndAnswers(Speech textToSpeech) throws ServiceException;

    /**
     * Reads a single answer with an audio player
     *
     * @param textToSpeech the textToSpeech object with the properties of the text
     * @throws ServiceException if audio can't be played.
     */
    void readSingleAnswer(Speech textToSpeech) throws ServiceException;

    /**
     * Stops the playing of an audio text
     *
     * @throws ServiceException if the method can't stop playing the audio
     */
    void stopSpeaking() throws ServiceException;


    /**
     * Sets a certain voice for speaking
     *
     * @param textToSpeech the new textToSpeech object with the voice properties
     * @throws ServiceException if the method can't set the voice
     */
    void setVoice(Speech textToSpeech) throws ServiceException;

}
