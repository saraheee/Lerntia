package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;

@Controller
public class ZoomedImageController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AlertController alertController;
    private File imageFile;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double screenWidth = screenSize.getWidth();
    private double screenHeight = screenSize.getHeight();
    private Scene imageScene;

    @Autowired
    public ZoomedImageController(AlertController alertController) {
        this.alertController = alertController;
    }

    @FXML
    void onZoomButtonClicked() {
        LOG.debug("Zoom button clicked");
        if (imageFile == null || !imageFile.exists()) {
            LOG.debug("Zooming was selected, but there was no image to be shown.");
            alertController.showBigAlert(Alert.AlertType.WARNING, "Bild nicht gefunden", "Diese Frage hat kein verbundenes Bild", "");
            return;
        }
        Image image = null;
        try {
            image = (new Image(imageFile.toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        var stage = new Stage();
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("[Lerntia] Bild");
        var imageView = new ImageView();
        if (image != null) {
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(Math.min(image.getWidth(), screenWidth));
            imageView.setFitHeight(Math.min(image.getHeight(), screenHeight));
        }
        var pane = new BorderPane();
        pane.setCenter(imageView);
        imageScene = new Scene(pane);
        stage.setScene(imageScene);
        stage.show();
        LOG.debug("Successfully opened a window for the zoomed image");

        Platform.runLater(() -> imageScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.Z || e.getCode() == KeyCode.S || e.getCode() == KeyCode.C || e.getCode() == KeyCode.ESCAPE) {
                LOG.debug("Key pressed, closing");
                closeZoomedImageWindows();
            }
        }));
    }

    @FXML
    private void closeZoomedImageWindows() {
        LOG.debug("Trying to close the zoomed image window.");
        ((Stage) imageScene.getWindow()).close();
    }

    void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

}
