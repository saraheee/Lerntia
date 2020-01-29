package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.exception.ControllerException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
class DirectoryChooserController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public String showFileSaveDirectoryChooser() throws ControllerException {

        LOG.info("Open new FileChooser to save a file.");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("[Lerntia] Verzeichnis");
        // fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        Stage stage = new Stage();
        String filePath;

        try {
            filePath = fileChooser.showSaveDialog(stage).toString();
        } catch (NullPointerException e) {
            throw new ControllerException("Keine Datei angegeben!");
        }
        if (filePath == null) {
            throw new ControllerException("Keine Datei angegeben!");
        }
        return filePath;
    }
}
