package at.ac.tuwien.sepm.assignment.groupphase.lerntia.talk;

import marytts.util.data.audio.MonoAudioInputStream;
import marytts.util.data.audio.StereoAudioInputStream;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class is taken from a Youtube Tutorial. URL: https://www.youtube.com/watch?v=OLKxBorVwk8
 *
 * @author GOXR3PLUS
 */
public class AudioPlayer extends Thread implements IAudioPlayer {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public boolean finishedAudio = false;
    private AudioInputStream ais;
    private SourceDataLine line;
    private Status status = Status.WAITING;
    private boolean exitRequested = false;
    private float gain = 1.0f;


    public void setAudio(AudioInputStream audio) {
        if (status == Status.PLAYING) {
            throw new IllegalStateException("Cannot set audio while playing");
        }
        this.ais = audio;
    }


    //Cancel the AudioPlayer which will cause the Thread to exit
    public void cancel() {
        if (line != null) {
            line.stop();
        }
        exitRequested = true;
    }


    private float getGainValue() {
        return gain;
    }


    //Sets Gain value. Line should be opened before calling this method. Linear scale 0.0 <--> 1.0 Threshold Coef. : 1/2 to avoid saturation.
    public void setGain(float fGain) {
        gain = fGain;

        // Better type
        if (line != null && line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            ((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue((float) (20 * Math.log10(fGain <= 0.0 ? 0.0000 : fGain)));
        }
    }

    @Override
    public void run() {

        status = Status.PLAYING;
        finishedAudio = false;
        var audioFormat = ais.getFormat();
        if (audioFormat.getChannels() == 1) {
            ais = new StereoAudioInputStream(ais);
            audioFormat = ais.getFormat();

        } else {
            assert audioFormat.getChannels() == 2 : "Unexpected number of channels: " + audioFormat.getChannels();
            ais = new MonoAudioInputStream(ais);
        }

        var info = new DataLine.Info(SourceDataLine.class, audioFormat);

        try {
            if (line == null) {
                var bIsSupportedDirectly = AudioSystem.isLineSupported(info);
                if (!bIsSupportedDirectly) {
                    var sourceFormat = audioFormat;
                    var targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), sourceFormat.getSampleSizeInBits(),
                        sourceFormat.getChannels(), sourceFormat.getChannels() * (sourceFormat.getSampleSizeInBits() / 8), sourceFormat.getSampleRate(),
                        sourceFormat.isBigEndian());

                    ais = AudioSystem.getAudioInputStream(targetFormat, ais);
                    audioFormat = ais.getFormat();
                }
                info = new DataLine.Info(SourceDataLine.class, audioFormat);
                line = (SourceDataLine) AudioSystem.getLine(info);
            }
            line.open(audioFormat);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
            return;
        }

        line.start();
        setGain(getGainValue());

        var nRead = 0;
        var abData = new byte[65532];
        while ((nRead != -1) && (!exitRequested)) {
            try {
                nRead = ais.read(abData, 0, abData.length);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
            }
            if (nRead >= 0) {
                line.write(abData, 0, nRead);
            }
        }
        if (!exitRequested) {
            line.drain();
        }
        line.close();
        finishedAudio = true;
        LOG.debug("Finished playing!");
    }

    /*
     * The status of the player
     *
     * @author GOXR3PLUS
     */
    public enum Status {
        /**
         *
         */
        WAITING,
        /**
         *
         */
        PLAYING
    }
}

