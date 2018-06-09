package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
public class ZoomedImageController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AlertController alertController;
    private final WindowController windowController;
    private final AudioController audioController;
    private File imageFile;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double screenWidth = screenSize.getWidth();
    private double screenHeight = screenSize.getHeight();
    private double eps = (screenHeight / 100) * 10;
    private Scene imageScene;

    private boolean imageClosed = false;
    private boolean key1pressed = false;
    private boolean key2pressed = false;
    private boolean key3pressed = false;
    private boolean key4pressed = false;
    private boolean key5pressed = false;
    private boolean keyCpressed = false;

    @Autowired
    public ZoomedImageController(AlertController alertController, WindowController windowController, AudioController audioController) {
        this.alertController = alertController;
        this.windowController = windowController;
        this.audioController = audioController;
    }

    @FXML
    void onZoomButtonClicked() {
        LOG.debug("Zoom button clicked");
        if (imageFile == null || !imageFile.exists()) {
            LOG.debug("Zooming was selected, but there was no image to be shown.");
            alertController.showBigAlert(Alert.AlertType.WARNING, "Kein Bild gefunden", "Diese Frage hat kein verbundenes Bild.", "");
            return;
        }
        Image image = null;
        try {
            image = (new Image(imageFile.toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        var imageView = new ImageView();
        if (image != null) {
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(Math.min(image.getWidth(), screenWidth - eps));
            imageView.setFitHeight(Math.min(image.getHeight(), screenHeight - eps));
        }
        var pane = new BorderPane();
        pane.setCenter(imageView);
        imageScene = new Scene(pane);
        windowController.openNewWindow("Bild", imageScene);
        LOG.debug("Successfully opened a window for the zoomed image");

        Platform.runLater(() -> imageScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.Z || e.getCode() == KeyCode.S || e.getCode() == KeyCode.C || e.getCode() == KeyCode.ESCAPE) {
                LOG.debug("Key pressed, closing");
                imageClosed = true;
                closeZoomedImageWindows();
            }
            if (e.getCode() == KeyCode.A) {
                LOG.debug("A key pressed while image is open");
                audioController.setSelected();
            }
            if (e.getCode() == KeyCode.NUMPAD1 || e.getCode() == KeyCode.DIGIT1) {
                LOG.debug("Key 1 pressed, while image is open");
                closeZoomedImageWindows();
                key1pressed = true;
            }
            if (e.getCode() == KeyCode.NUMPAD2 || e.getCode() == KeyCode.DIGIT2) {
                LOG.debug("Key 2 pressed, while image is open");
                closeZoomedImageWindows();
                key2pressed = true;
            }
            if (e.getCode() == KeyCode.NUMPAD3 || e.getCode() == KeyCode.DIGIT3) {
                LOG.debug("Key 3 pressed, while image is open");
                closeZoomedImageWindows();
                key3pressed = true;
            }
            if (e.getCode() == KeyCode.NUMPAD4 || e.getCode() == KeyCode.DIGIT4) {
                LOG.debug("Key 4 pressed, while image is open");
                closeZoomedImageWindows();
                key4pressed = true;
            }
            if (e.getCode() == KeyCode.NUMPAD5 || e.getCode() == KeyCode.DIGIT5) {
                LOG.debug("Key 5 pressed, while image is open");
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

    boolean isKeyCpressed() {
        return keyCpressed;
    }
}
