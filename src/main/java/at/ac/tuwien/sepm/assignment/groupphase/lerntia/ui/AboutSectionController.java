package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ConfigReaderException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class AboutSectionController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final WindowController windowController;
    private ConfigReader configReaderAbout = null;
    private String ABOUTTEXT = null;

    @FXML
    private Label about;

    @Autowired
    AboutSectionController(WindowController windowController) {
        this.windowController = windowController;
    }

    void showAboutSection() {
        if(configReaderAbout == null || ABOUTTEXT == null) {
            try {
                configReaderAbout = new ConfigReader("about");
                ABOUTTEXT = configReaderAbout.getValue("aboutText");
            } catch (ConfigReaderException e) {
                LOG.error("About text could not be read: {} / {}", e.getCustomMessage(), e.getMessage());
            }

        }
        LOG.info("Showing the About Section");
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/showAboutSection.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);

        windowController.openNewWindow("Über Lerntia", fxmlLoader);
        about.setText(ABOUTTEXT);

    }
}
