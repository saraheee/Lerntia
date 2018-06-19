package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.application.MainApplication;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Controller
public class WindowController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static String LERNTIA = "[Lerntia] ";
    private static String ICON = "/icons/main.png";

    public Stage openNewWindow(String title, FXMLLoader fxmlLoader) throws ControllerException {
        var stage = new Stage();
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(LERNTIA + title);
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream(ICON)));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            LOG.debug("Successfully opened a new window with title: " + title);
            stage.show();
        } catch (IOException e) {
            throw new ControllerException("Konnte kein neues Fenster öffnen mit dem Titel: " + title);
        }
        return stage;
    }

    //For windows that are created without a fxml file
    public void openNewWindow(String title, Scene scene) {
        var stage = new Stage();
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(LERNTIA + title);
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream(ICON)));
        stage.setScene(scene);
        stage.show();
    }

}