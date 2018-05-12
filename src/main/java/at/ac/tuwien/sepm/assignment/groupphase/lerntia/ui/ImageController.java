package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

import static org.springframework.util.Assert.notNull;

@Controller
public class ImageController { // todo rename to zoomButtonController

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IMainLerntiaService lerntiaService;
    private final LerntiaMainController lerntiaMainController;

    @FXML
    private Button zoomButton;

    @Autowired
    public ImageController(IMainLerntiaService lerntiaService, LerntiaMainController lerntiaMainController) {
        notNull(lerntiaService, "'lerntiaService' should not be null");
        notNull(lerntiaMainController, "'lerntiaMainController' should not be null");
        this.lerntiaService = lerntiaService;
        this.lerntiaMainController = lerntiaMainController;
    }

    @FXML
    private void onZoomButtonClicked() {
        LOG.debug("Zoom button clicked");
        //TODO: maximize image
    }

    void setSelected() {
        if (zoomButton.isDefaultButton()) {
            zoomButton.defaultButtonProperty().setValue(false);
            //TODO: minimize image
        } else {
            zoomButton.defaultButtonProperty().setValue(true);
            onZoomButtonClicked();
        }

    }

    void setVisible(boolean visible) {zoomButton.setVisible(visible);}
}
