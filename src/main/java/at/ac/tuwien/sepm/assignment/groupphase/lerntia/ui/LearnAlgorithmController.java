package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.util.ButtonText;
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

    public LearnAlgorithmController() {
        this.selected = false;

    }

    @FXML
    private void initialize() {
        learnAlgorithmButton.setText(ButtonText.ALGORITHMOFF.toString());
    }

    @FXML
    public void onAlgorithmButtonPressed() {
        LOG.info("Learn Algorithm Button Pressed");
        if (learnAlgorithmButton.isDefaultButton()) {
            LOG.info("Set Algorithm to OFF");
            selected = false;
            learnAlgorithmButton.defaultButtonProperty().setValue(false);
            learnAlgorithmButton.setText(ButtonText.ALGORITHMOFF.toString());
        } else {
            learnAlgorithmButton.defaultButtonProperty().setValue(true);
            LOG.info("Set Algorithm to ON");
            selected = true;
            learnAlgorithmButton.setText(ButtonText.ALGORITHMON.toString());
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void reset() {
        selected = false;
        learnAlgorithmButton.defaultButtonProperty().setValue(false);
        learnAlgorithmButton.setText(ButtonText.ALGORITHMOFF.toString());
    }
}
