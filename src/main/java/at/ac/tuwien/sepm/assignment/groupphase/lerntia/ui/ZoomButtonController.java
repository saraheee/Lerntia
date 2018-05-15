package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hsqldb.persist.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;

import static org.springframework.util.Assert.notNull;

@Controller
public class ZoomButtonController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ZoomedImageController zoomedImageController;
    private File imageFile;

    @FXML
    private Button zoomButton;
    @FXML
    private ImageView zoomedImage;

    @Autowired
    public ZoomButtonController(ZoomedImageController zoomedImageController) {
        this.zoomedImageController = zoomedImageController;
    }

    void setVisible(boolean visible) {zoomButton.setVisible(visible);}

    @FXML
    void onZoomButtonClicked() {
        LOG.debug("Clicked on zoom button");
        zoomedImageController.onZoomButtonClicked();
    }
}