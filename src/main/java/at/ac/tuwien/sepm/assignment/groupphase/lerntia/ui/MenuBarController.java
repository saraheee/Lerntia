package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class MenuBarController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ImportFileController importFileController;

    @Autowired
    MenuBarController(ImportFileController importFileController) {
        this.importFileController = importFileController;
    }

    @FXML
    private void importQuestions() {
        importFileController.showImportWindow();
    }
}