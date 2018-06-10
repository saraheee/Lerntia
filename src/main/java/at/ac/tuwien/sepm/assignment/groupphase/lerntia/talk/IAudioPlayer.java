package at.ac.tuwien.sepm.assignment.groupphase.lerntia.talk;

import javax.sound.sampled.AudioInputStream;

public interface IAudioPlayer {

    /**
     * Sets a new audio stream.
     *
     * @param audio the audio to set
     * @throws IllegalStateException if the audio is currently played and a new stream can't be set
     */
    void setAudio(AudioInputStream audio) throws IllegalStateException;

    /**
     * Cancels the audio player and exits the thread.
     */
    void cancel();

    /**
     * Sets a new gain value for the audio player.
     *
     * @param fGain the gain value to set
     */
    void setGain(float fGain);

    /**
     * Starts the audio thread.
     */
    void start();

}