package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceValidationException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Speech;

public interface ITextToSpeechService {

    /**
     * Plays a first audio output on startup to welcome the user
     *
     * @throws TextToSpeechServiceException if welcome audio can't be played.
     */
    void playWelcomeText() throws TextToSpeechServiceException;


    /**
     * Reads a question and all answers with an audio player
     *
     * @param textToSpeech the textToSpeech object with the properties of the text
     * @throws TextToSpeechServiceException           if the audio can't be played.
     * @throws TextToSpeechServiceValidationException if the text input fails the validation.
     */
    void readQuestionAndAnswers(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException;

    /**
     * Reads a single answer with an audio player
     *
     * @param textToSpeech the textToSpeech object with the properties of the text
     * @throws TextToSpeechServiceException           if the audio can't be played.
     * @throws TextToSpeechServiceValidationException if the text input fails the validation.
     */
    void readSingleAnswer(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException;

    /**
     * Stops the playing of an audio text
     */
    void stopSpeaking();


    /**
     * Sets a certain voice for speaking
     *
     * @param textToSpeech the new textToSpeech object with the voice properties
     */
    void setVoice(Speech textToSpeech);

}
