package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.MainLerntiaService;
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
    private final MainLerntiaService mainLerntiaService;

    public LearnAlgorithmController(MainLerntiaService mainLerntiaService) {
        this.selected = false;
        this.mainLerntiaService = mainLerntiaService;
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
            mainLerntiaService.setLearnAlgorithmStatus(false);
            learnAlgorithmButton.defaultButtonProperty().setValue(false);
            learnAlgorithmButton.setText(ButtonText.ALGORITHMOFF.toString());
        } else {
            learnAlgorithmButton.defaultButtonProperty().setValue(true);
            LOG.info("Set Algorithm to ON");
            selected = true;

            mainLerntiaService.setLearnAlgorithmStatus(true);
            learnAlgorithmButton.setText(ButtonText.ALGORITHMON.toString());
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void reset() {
        selected = false;
        if (learnAlgorithmButton != null) {
            learnAlgorithmButton.defaultButtonProperty().setValue(false);
            learnAlgorithmButton.setText(ButtonText.ALGORITHMOFF.toString());
            mainLerntiaService.setLearnAlgorithmStatus(false);
            try {
                mainLerntiaService.stopAlgorithm();
            } catch (ServiceException e) {
                LOG.error("Learn Algorithm had an error Shuting down.");
            }
        }

    }
}
