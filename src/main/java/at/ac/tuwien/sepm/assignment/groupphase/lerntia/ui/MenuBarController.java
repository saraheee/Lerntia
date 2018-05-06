package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.lang.invoke.MethodHandles;

@Controller
public class MenuBarController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    //private final SimpleQuestionaireImportService service;
    private Stage stage;

    /*
    public MenuBarController(SimpleQuestionaireImportService service) {
        this.service = service;
    }
    */

    @FXML
    private void importQuestions() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Verzeichnis");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            /*
            try {
                service.importQuestionaire(file);
            }
            catch (ServiceException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Fehler");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            */
        }
    }
}
