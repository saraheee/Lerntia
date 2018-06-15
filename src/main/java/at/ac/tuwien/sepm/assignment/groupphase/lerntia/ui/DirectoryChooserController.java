package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class DirectoryChooserController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public String showFileSaveDirectoryChooser() throws ControllerException {

        LOG.info("Open new FileChooser to save a file.");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("[Lerntia] Verzeichnis");

        // actually here we do not know if the report is a pdf file.
        // the pdf is only specified in the DAO layer.
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        Stage stage = new Stage();

        String filePath;

        // because of the .toString() we get a NullPointerException if the dialog is cancelled.

        try {
            filePath = fileChooser.showSaveDialog(stage).toString();
        } catch (NullPointerException e) {
            throw new ControllerException("Keine Datei angegeben");
        }

        if (filePath == null) {
            throw new ControllerException("Keine Datei angegeben");
        }

        return filePath;
    }
}
