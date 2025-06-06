package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.exception.ControllerException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
class ZoomedImageController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AlertController alertController;
    private final WindowController windowController;
    private final AudioController audioController;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final double screenWidth = screenSize.getWidth();
    private final double screenHeight = screenSize.getHeight();
    private final double eps = (screenHeight / 100) * 11;
    private static final double k = 2;
    private static final int audioOnImageButtonHeight = 120;
    private File imageFile;
    private Scene imageScene;

    private boolean imageClosed = false;
    private boolean key1pressed = false;
    private boolean key2pressed = false;
    private boolean key3pressed = false;
    private boolean key4pressed = false;
    private boolean key5pressed = false;

    @Autowired
    public ZoomedImageController(AlertController alertController, WindowController windowController, AudioController audioController) {
        this.alertController = alertController;
        this.windowController = windowController;
        this.audioController = audioController;
    }

    @FXML
    void onZoomButtonClicked() throws ControllerException {
        LOG.debug("Zoom button clicked");
        if (imageFile == null || !imageFile.exists()) {
            LOG.debug("Zooming was selected, but there was no image to be shown.");
            alertController.showBigAlert(Alert.AlertType.WARNING, "Kein Bild gefunden", "Diese Frage hat kein verbundenes Bild.", "");
            return;
        }
        Image image;
        try {
            image = (new Image(imageFile.toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            throw new ControllerException("URL vom Bild nicht lesbar.");
        }
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(Math.min(image.getWidth(), screenWidth - (k * eps)));
        imageView.setFitHeight(Math.min(image.getHeight(), screenHeight - audioOnImageButtonHeight - eps));
        BorderPane pane = new BorderPane();
        pane.setCenter(imageView);

        final Button audioOnImage = new Button();
        audioOnImage.getStylesheets().add(this.getClass().getResource("/css/audio-on-image-button.css").toExternalForm());
        audioOnImage.setMinSize(0, audioOnImageButtonHeight);
        audioOnImage.setMaxSize((Math.min(image.getWidth(), screenWidth - eps)), audioOnImageButtonHeight);
        audioOnImage.setOnAction(event -> audioController.setSelected());
        pane.setTop(audioOnImage);
        BorderPane.setAlignment(audioOnImage, Pos.TOP_CENTER);
        imageScene = new Scene(pane);
        windowController.openNewWindow("Bild", imageScene);
        LOG.debug("Successfully opened a window for the zoomed image");

        Platform.runLater(() -> imageScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.Z || e.getCode() == KeyCode.S || e.getCode() == KeyCode.C || e.getCode() == KeyCode.ESCAPE) {
                LOG.info("Z key pressed while image is open");
                imageClosed = true;
                closeZoomedImageWindows();
            }
            if (e.getCode() == KeyCode.A) {
                LOG.info("A key pressed while image is open");
                audioController.setSelected();
            }
            if (e.getCode() == KeyCode.NUMPAD1 || e.getCode() == KeyCode.DIGIT1) {
                LOG.info("Key 1 pressed, while image is open");
                closeZoomedImageWindows();
                key1pressed = true;
            }
            if (e.getCode() == KeyCode.NUMPAD2 || e.getCode() == KeyCode.DIGIT2) {
                LOG.info("Key 2 pressed, while image is open");
                closeZoomedImageWindows();
                key2pressed = true;
            }
            if (e.getCode() == KeyCode.NUMPAD3 || e.getCode() == KeyCode.DIGIT3) {
                LOG.info("Key 3 pressed, while image is open");
                closeZoomedImageWindows();
                key3pressed = true;
            }
            if (e.getCode() == KeyCode.NUMPAD4 || e.getCode() == KeyCode.DIGIT4) {
                LOG.info("Key 4 pressed, while image is open");
                closeZoomedImageWindows();
                key4pressed = true;
            }
            if (e.getCode() == KeyCode.NUMPAD5 || e.getCode() == KeyCode.DIGIT5) {
                LOG.info("Key 5 pressed, while image is open");
                closeZoomedImageWindows();
                key5pressed = true;
            }

        }));
        imageView.setOnMouseClicked((MouseEvent e) -> closeZoomedImageWindows());
        imageView.setOnMouseEntered((MouseEvent e) -> imageScene.setCursor(javafx.scene.Cursor.HAND));
        imageView.setOnMouseExited((MouseEvent e) -> imageScene.setCursor(Cursor.DEFAULT));
    }

    @FXML
    private void closeZoomedImageWindows() {
        LOG.debug("Trying to close the zoomed image window.");
        ((Stage) imageScene.getWindow()).close();
    }

    void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public boolean getImageClosed() {
        return imageClosed;
    }

    public void setImageClosed(boolean imageClosed) {
        this.imageClosed = imageClosed;
    }

    public boolean isKey1pressed() {
        return key1pressed;
    }

    public void setKey1pressed(boolean key1pressed) {
        this.key1pressed = key1pressed;
    }

    public boolean isKey2pressed() {
        return key2pressed;
    }

    public void setKey2pressed(boolean key2pressed) {
        this.key2pressed = key2pressed;
    }

    public boolean isKey3pressed() {
        return key3pressed;
    }

    public void setKey3pressed(boolean key3pressed) {
        this.key3pressed = key3pressed;
    }

    public boolean isKey4pressed() {
        return key4pressed;
    }

    public void setKey4pressed(boolean key4pressed) {
        this.key4pressed = key4pressed;
    }

    public boolean isKey5pressed() {
        return key5pressed;
    }

    public void setKey5pressed(boolean key5pressed) {
        this.key5pressed = key5pressed;
    }

}
