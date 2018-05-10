package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.FocusModel;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

import static java.awt.Color.black;

@Controller
public class AudioController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LerntiaService lerntiaService;

    @FXML
    private Button audioButton;

    @Autowired
    public AudioController(LerntiaService lerntiaService) {
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void onAudioButtonClicked() {
        audioButton.focusTraversableProperty().setValue(true);
        LOG.debug("Audio button clicked");
        //TODO: play sound
    }


    void setSelected() {
        audioButton.getStylesheets().add(getClass().getResource("/css/square-button.css").toExternalForm());
        audioButton.getStyleClass().add("button:focused");
        if(audioButton.isDefaultButton()) {
            audioButton.defaultButtonProperty().setValue(false);
            //TODO: stop sound
        } else {
            audioButton.defaultButtonProperty().setValue(true);
            onAudioButtonClicked();
        }

    }

}
