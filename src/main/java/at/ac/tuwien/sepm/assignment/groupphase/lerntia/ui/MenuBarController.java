package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class MenuBarController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ImportFileController importFileController;
    private final CreateCourseController createCourseController;
    private final SelectQuestionnaireController selectQuestionnaireController;
    private final AdministrateQuestionnaireController administrateQuestionnaireController;
    private final AboutSectionController showAboutSectionController;
    private final AlertController alertController;

    @Autowired
    MenuBarController(
        ImportFileController importFileController,
        CreateCourseController createCourseController,
        SelectQuestionnaireController selectQuestionnaireController,
        AdministrateQuestionnaireController administrateQuestionnaireController,
        AboutSectionController showAboutSectionController,
        AlertController alertController)
    {
        this.importFileController = importFileController;
        this.createCourseController = createCourseController;
        this.selectQuestionnaireController = selectQuestionnaireController;
        this.administrateQuestionnaireController = administrateQuestionnaireController;
        this.showAboutSectionController = showAboutSectionController;
        this.alertController = alertController;
    }

    @FXML
    private void importQuestions() {
        importFileController.showImportWindow();
    }

    @FXML
    public void createCourse(ActionEvent actionEvent) { createCourseController.showCreateCourseWindow(); }

    @FXML
    public void selectQuestionnaire(ActionEvent actionEvent) {
        selectQuestionnaireController.showSelectQuestionnaireWindow();
    }

    @FXML
    public void administrateQuestionnaire(ActionEvent actionEvent) {
        administrateQuestionnaireController.showAdministrateQuestionnaireWindow();
    }

    @FXML
    private void showAboutSection(ActionEvent actionEvent) {
        showAboutSectionController.showAboutSection();
    }

    @FXML
    private void exportQuestions(ActionEvent actionEvent) {
        //TODO: remove alert, when it's implemented
        alertController.showStandardAlert(Alert.AlertType.INFORMATION, "Nicht verfügbar", "Diese Funktionalität ist noch nicht verfügbar.", "Bitte bis zur nächsten Version 'Lerntia 3.0' gedulden.");
    }
}