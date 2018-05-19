package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Controller
public class AboutSectionController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ConfigReader configReaderAbout = new ConfigReader("about");
    private final String ABOUTTEXT = configReaderAbout.getValue("aboutText");

    @FXML
    private Label about;

    void showAboutSection() {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/showAboutSection.fxml"));
        var stage = new Stage();

        try {
            fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("[Lerntia] Über Lerntia");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

            about.setText(ABOUTTEXT);
            LOG.debug("about: " + ABOUTTEXT);
            LOG.debug("Successfully opened a window for showing the about section.");

        } catch (IOException e) {
            LOG.error("Failed to open a window for showing the about section.");
        }
    }
}
