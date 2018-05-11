package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.talk.TextToSpeech;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.Scanner;

import static org.springframework.util.Assert.notNull;

@Controller
public class AudioController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LerntiaService lerntiaService;
    private final LerntiaMainController lerntiaMainController;
    private final TextToSpeech textToSpeech;
    private final String VOICE = "bits3-hsmm";
    @FXML
    private Button audioButton;

    @Autowired
    public AudioController(LerntiaService lerntiaService, LerntiaMainController lerntiaMainController) {
        notNull(lerntiaService, "'lerntiaService' should not be null");
        notNull(lerntiaMainController, "'lerntiaMainController' should not be null");
        this.lerntiaService = lerntiaService;
        this.lerntiaMainController = lerntiaMainController;
        this.textToSpeech = new TextToSpeech();
    }

    @FXML
    private void onAudioButtonClicked() {
        LOG.debug("Audio button clicked");
        //play sound
        var textToRead = lerntiaMainController.getAudioText();
        //LOG.debug("Text to read:\n" + textToRead);

        try {
            textToSpeech.setVoice(VOICE);
            textToSpeech.stopSpeaking();
            textToSpeech.speak(textToRead, 1.0f, false, false);
        } catch (Exception e) {
            LOG.error("Failed to read question and answers with MaryTTS.");
        }
    }


    void setSelected() {
        if (audioButton.isDefaultButton()) {
            audioButton.defaultButtonProperty().setValue(false);
            //stop sound
            textToSpeech.stopSpeaking();
        } else {
            audioButton.defaultButtonProperty().setValue(true);
            onAudioButtonClicked();
        }

    }

}
