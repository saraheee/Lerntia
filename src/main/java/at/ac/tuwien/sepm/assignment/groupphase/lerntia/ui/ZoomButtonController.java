package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    private final IMainLerntiaService lerntiaService;
    private final LerntiaMainController lerntiaMainController;
    private File imageFile;

    @FXML
    private Button zoomButton;
    @FXML
    private ImageView zoomedImage;

    @Autowired
    public ZoomButtonController(IMainLerntiaService lerntiaService, LerntiaMainController lerntiaMainController) {
        notNull(lerntiaService, "'lerntiaService' should not be null");
        notNull(lerntiaMainController, "'lerntiaMainController' should not be null");
        this.lerntiaService = lerntiaService;
        this.lerntiaMainController = lerntiaMainController;
    }

    @FXML
    private void onZoomButtonClicked() {
        LOG.debug("Zoom button clicked");
        setSelected();
    }

    void setSelected( ) {
        if (zoomButton.isDefaultButton()) {
            LOG.debug("Zoom button will be set to false.");
            zoomButton.defaultButtonProperty().setValue(false);
            closeZoomedImageWindows();
        } else {
            LOG.debug("Zoom button will be set to true.");
            zoomButton.defaultButtonProperty().setValue(true);
            openZoomedImageWindows();
        }
    }

    void setVisible(boolean visible) {zoomButton.setVisible(visible);}

    private void openZoomedImageWindows() {
        if(imageFile == null) {
            LOG.warn("No image to be zoomed");
            return;
        }
        Image image = null;
        try {
            image = (new Image(imageFile.toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/zoomedImage.fxml"));
        var stage = new Stage();
        try {
            fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Bild");
            Scene newScene = new Scene(fxmlLoader.load());
            Platform.runLater(() -> newScene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.Z || e.getCode() == KeyCode.S || e.getCode() == KeyCode.C || e.getCode() == KeyCode.ESCAPE) {
                    LOG.debug("Key pressed, closing");
                    closeZoomedImageWindows();
                }
            }));
            stage.setScene(newScene);
            stage.show();
            LOG.debug("Successfully opened a window for the zoomed image");
            zoomedImage.setImage(image);
        } catch (IOException e) {
            LOG.error("Failed to open a window for the zoomed image. " + e.getMessage());
        }
    }

    @FXML
    private void closeZoomedImageWindows() {
        LOG.debug("Trying to close the zoomed image window.");
        ((Stage) zoomedImage.getScene().getWindow()).close();
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
