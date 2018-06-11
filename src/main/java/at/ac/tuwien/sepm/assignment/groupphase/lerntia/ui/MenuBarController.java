package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.locks.ReentrantLock;

@Controller
public class MenuBarController implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ImportFileController importFileController;
    private final CreateCourseController createCourseController;
    private final SelectQuestionnaireController selectQuestionnaireController;
    private final SelectExamController selectExamController;
    private final EditExamController editExamController;
    private final ExportQuestionnaireController exportQuestionnaireController;

    private final LerntiaMainController lerntiaMainController;

    private final AdministrateQuestionnaireController administrateQuestionnaireController;
    private final AboutSectionController showAboutSectionController;
    private final AlertController alertController;
    private ReentrantLock lock = new ReentrantLock();
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
        EditExamController editExamController,
        LerntiaMainController lerntiaMainController,
        AdministrateQuestionnaireController administrateQuestionnaireController,
        AboutSectionController showAboutSectionController,
        AlertController alertController,
        ExportQuestionnaireController exportQuestionnaireController
    ) {
        this.importFileController = importFileController;
        this.createCourseController = createCourseController;
        this.selectQuestionnaireController = selectQuestionnaireController;
        this.selectExamController = selectExamController;
        this.editExamController = editExamController;
        this.lerntiaMainController = lerntiaMainController;
        this.administrateQuestionnaireController = administrateQuestionnaireController;
        this.showAboutSectionController = showAboutSectionController;
        this.alertController = alertController;
        this.exportQuestionnaireController = exportQuestionnaireController;
    }

    @FXML
    private void initialize() {
        examToLearnButton.setDisable(true);
        learnToExamButton.setDisable(false);
    }

    @FXML
    private void importQuestions() {
        importFileController.showImportWindow();
    }

    @FXML
    public void createCourse() {
        createCourseController.showCreateCourseWindow();
    }

    @FXML
    public void selectQuestionnaire() {
        selectQuestionnaireController.showSelectQuestionnaireWindow();
    }

    @FXML
    public void switchToExamMode() {

        try {
            selectExamController.showSelectExamWindow();
            examToLearnButton.setDisable(false);
            learnToExamButton.setDisable(true);

            selectExamController.setSelectingCanceled(false);
            editExamController.setEditingCanceled(false);
            var examEditThread = new Thread(this);
            examEditThread.start();

        } catch (ControllerException e) {
            alertController.showBigAlert(Alert.AlertType.ERROR, "Keine Prüfungsfragebögen",
                "Keine Prüfungsfragebögen vorhanden",
                "Es gibt noch keine Prüfungsfragenbögen. Bitte zuerst einen Fragebogen importieren\n" +
                    "Hinweis: Beim Import nicht vergessen, die entsprechende Checkbox zu markieren!");
        }
    }


    @Override
    public void run() {
        LOG.debug("Edit exam listener thread started!");
        while (!lerntiaMainController.isExamMode()) {
            lock.lock();
            try {
                if (selectExamController.getSelectingCanceled() || editExamController.getEditingCanceled()) {
                    examToLearnButton.setDisable(true);
                    learnToExamButton.setDisable(false);
                    LOG.debug("Exam canceled!");
                    return;
                }
            } finally {
                lock.unlock();
            }
        }
    }


    @FXML
    public void switchToLearnMode() {
        try {
            boolean clicked = alertController.showStandardConfirmationAlert("In den Lernmodus wechseln.",
                "Soll der Prüfungsmodus wirklich verlassen werden?",
                "Alle Fragen und Antworten werden zurückgesetzt!");

            if (clicked) {
                learnToExamButton.setDisable(false);
                examToLearnButton.setDisable(true);
                lerntiaMainController.setExamMode(false);
                lerntiaMainController.switchToLearnMode();
                lerntiaMainController.getAndShowTheFirstQuestion();

            }
        } catch (ControllerException e) {
            // TODO - show alert or throw new exception
        }
    }

    @FXML
    public void administrateQuestionnaire() {
        administrateQuestionnaireController.showAdministrateQuestionnaireWindow();
    }

    @FXML
    private void showAboutSection() {
        showAboutSectionController.showAboutSection();
    }

    @FXML
    private void exportQuestions() {
        exportQuestionnaireController.showExportQuestionnaireWindow();
    }

    @FXML
    private void onMenuClicked() {
        lerntiaMainController.removeColorsAndEnableAnswers();
        lerntiaMainController.stopAudio();
    }

    @FXML
    private void onHelpClicked() {
        lerntiaMainController.removeColorsAndEnableAnswers();
        lerntiaMainController.stopAudio();
    }
}