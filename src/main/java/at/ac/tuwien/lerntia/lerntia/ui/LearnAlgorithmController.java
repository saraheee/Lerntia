package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.service.impl.MainLerntiaService;
import at.ac.tuwien.lerntia.util.ButtonText;
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
    private Button algorithmButton;
    private boolean selected;
    private final MainLerntiaService mainLerntiaService;

    public LearnAlgorithmController(MainLerntiaService mainLerntiaService) {
        this.selected = false;
        this.mainLerntiaService = mainLerntiaService;
    }

    @FXML
    private void initialize() {
        algorithmButton.setText(ButtonText.ALGORITHMOFF.toString());
    }

    @FXML
    public void onAlgorithmButtonPressed() {
        LOG.info("Learn algorithm button pressed");
        if (algorithmButton.isDefaultButton()) {
            LOG.info("Set algorithm to OFF");
            selected = false;
            mainLerntiaService.setLearnAlgorithmStatus(false);
            algorithmButton.defaultButtonProperty().setValue(false);
            algorithmButton.setText(ButtonText.ALGORITHMOFF.toString());
        } else {
            algorithmButton.defaultButtonProperty().setValue(true);
            LOG.info("Set algorithm to ON");
            selected = true;

            mainLerntiaService.setLearnAlgorithmStatus(true);
            algorithmButton.setText(ButtonText.ALGORITHMON.toString());
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void reset() {
        selected = false;
        if (algorithmButton != null) {
            algorithmButton.defaultButtonProperty().setValue(false);
            algorithmButton.setText(ButtonText.ALGORITHMOFF.toString());
            mainLerntiaService.setLearnAlgorithmStatus(false);
            try {
                mainLerntiaService.stopAlgorithm();
            } catch (ServiceException e) {
                LOG.error("Learn algorithm has an error in shutting down.");
            }
        }

    }
}
