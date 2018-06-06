package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
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
    private final SelectExamController selectExamController;

    private final LerntiaMainController lerntiaMainController;

    private final AdministrateQuestionnaireController administrateQuestionnaireController;
    private final AboutSectionController showAboutSectionController;
    private final AlertController alertController;


    @FXML
    private MenuItem examToLearnButton;
    @FXML
    private MenuItem learnToExamButton;

    @Autowired
    MenuBarController(
        ImportFileController importFileController,
        CreateCourseController createCourseController,
        SelectQuestionnaireController selectQuestionnaireController,
        SelectExamController selectExamController,
        LerntiaMainController lerntiaMainController,
        AdministrateQuestionnaireController administrateQuestionnaireController,
        AboutSectionController showAboutSectionController,
        AlertController alertController
    )
    {
        this.importFileController = importFileController;
        this.createCourseController = createCourseController;
        this.selectQuestionnaireController = selectQuestionnaireController;
        this.selectExamController = selectExamController;
        this.lerntiaMainController = lerntiaMainController;
        this.administrateQuestionnaireController = administrateQuestionnaireController;
        this.showAboutSectionController = showAboutSectionController;
        this.alertController = alertController;
    }

    @FXML
    private void initialize(){
        examToLearnButton.setVisible(false);
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
    public void switchToExamMode(ActionEvent actionEvent) {
        selectExamController.showSelectExamWindow();
        examToLearnButton.setVisible(true);
        learnToExamButton.setVisible(false);

    }
    @FXML
    public void switchToLearnMode(ActionEvent actionEvent) {
        try {
            boolean clicked = alertController.showStandardConfirmationAlert("In dem Lernmodus ändern.","Sie verlassen gerade den Prüfungsmodus!",
                "Sie sind gerade im Prozess den Prüfungsmodus zu verlassen.\nSind Sie sich sicher? Fragen und Antworten werden zurückgesetzt");

            if (clicked) {
                lerntiaMainController.setExamMode(false);
                lerntiaMainController.switchToLearnMode();
                lerntiaMainController.getAndShowTheFirstQuestion();
                learnToExamButton.setVisible(true);
                examToLearnButton.setVisible(false);
            }
        } catch (ControllerException e) {
            e.printStackTrace();
        }


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
        alertController.showBigAlert(Alert.AlertType.INFORMATION,
            "Nicht verfügbar",
            "Diese Funktionalität ist noch nicht verfügbar.",
            "Bitte bis zur nächsten Version 'Lerntia 3.0' gedulden.");
    }


}