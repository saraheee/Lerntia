package at.ac.tuwien.lerntia.lerntia.service;

import at.ac.tuwien.lerntia.exception.TextToSpeechServiceException;
import at.ac.tuwien.lerntia.exception.TextToSpeechServiceValidationException;
import at.ac.tuwien.lerntia.lerntia.dto.Speech;

public interface ITextToSpeechService {

    /**
     * Plays a first audio output on startup to welcome the user
     *
     * @throws TextToSpeechServiceException if the appropriate config file ('speech.properties') is not provided
     *                                      or if welcome audio can't be played.
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
     * Reads the feedback text with an audio player
     *
     * @param textToSpeech the textToSpeech object with the properties of the text
     * @throws TextToSpeechServiceException           if the audio can't be played.
     * @throws TextToSpeechServiceValidationException if the text input fails the validation.
     */
    void readFeedbackText(Speech textToSpeech) throws TextToSpeechServiceException, TextToSpeechServiceValidationException;

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

    /**
     * Checks if the read process has already ended
     *
     * @return true if the audio has already ended, else false
     */
    boolean noCurrentAudio();
}
