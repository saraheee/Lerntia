package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class LearnAlgorithmController {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @FXML
    private Button learnAlgorithmButton;
    private boolean selected;

    @FXML
    private void initialize(){
        learnAlgorithmButton.setText("Algorithm AUS");
    }
    public LearnAlgorithmController(){
        this.selected = false;

    }

    @FXML
    public void onAlgorithmButtonPressed() {
        LOG.info("Learn Algorithm Button Pressed");
        if (learnAlgorithmButton.isDefaultButton()){
            LOG.info("Set Algorithm to OFF");
            selected = false;
            learnAlgorithmButton.defaultButtonProperty().setValue(false);
            learnAlgorithmButton.setText("Algorithm AUS");
        }else {
            learnAlgorithmButton.defaultButtonProperty().setValue(true);
            LOG.info("Set Algorithm to ON");
            selected = true;
            learnAlgorithmButton.setText("Algorithm AN");
        }
    }

    public boolean isSelected() {
        return selected;
    }
}
