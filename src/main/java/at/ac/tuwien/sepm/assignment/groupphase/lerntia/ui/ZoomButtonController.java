package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.lang.invoke.MethodHandles;

@Controller
public class ZoomButtonController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ZoomedImageController zoomedImageController;

    @FXML
    private Button zoomButton;

    @Autowired
    public ZoomButtonController(ZoomedImageController zoomedImageController) {
        this.zoomedImageController = zoomedImageController;
    }

    void setVisible(boolean visible) {
        zoomButton.setVisible(visible);
    }

    @FXML
    void onZoomButtonClicked() {
        LOG.debug("Clicked on zoom button");
        zoomedImageController.onZoomButtonClicked();
    }
}