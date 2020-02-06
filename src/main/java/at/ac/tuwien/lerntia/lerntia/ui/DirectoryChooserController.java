package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.exception.ControllerException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
class DirectoryChooserController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PDFPATH = System.getProperty("user.dir") + File.separator + "ergebnisse" + File.separator;

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

    String getExamPath() throws ControllerException {
        if (!Files.exists(Paths.get(PDFPATH))) {
            if (new File(String.valueOf(PDFPATH)).mkdir()) { //result directory created
                LOG.info("Result folder created.");
            } else {
                throw new ControllerException("Das Anlegen eines Ergebnis-Ordners ist fehlgeschlagen!");
            }
        }
        return PDFPATH + "prfg_" +  new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss'.pdf'").format(new Date());
    }
}
