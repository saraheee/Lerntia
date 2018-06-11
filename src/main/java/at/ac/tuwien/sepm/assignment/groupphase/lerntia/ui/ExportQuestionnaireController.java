package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireExportService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleLearningQuestionnaireService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Controller
public class ExportQuestionnaireController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final WindowController windowController;
    private final IQuestionnaireExportService exportService;
    private final IMainLerntiaService lerntiaService;
    private final IQuestionnaireService iQuestionnaireService;
    @FXML
    public ComboBox<String> cb_questionnaire;
    private Stage stage;
    private List<LearningQuestionnaire> learningQuestionnaires;
    private List learningQuestionnaireList;
    private AlertController alertController;

    @Autowired
    public ExportQuestionnaireController(
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
        WindowController windowController,
        IQuestionnaireExportService iQuestionnaireExportService,
        AlertController alertController,
        IMainLerntiaService lerntiaService,
        IQuestionnaireService iQuestionnaireService
    ) {
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.windowController = windowController;
        this.exportService = iQuestionnaireExportService;
        this.alertController = alertController;
        this.lerntiaService = lerntiaService;
        this.iQuestionnaireService = iQuestionnaireService;
    }


    @FXML
    public void initialize() {
        try {
            this.learningQuestionnaires = simpleLearningQuestionnaireService.readAll();
        } catch (ServiceException e) {
            LOG.error("Failed to initialize ExportQuestionnaireController");
        }

        for (var learningQuestionnaire : learningQuestionnaires) {
            cb_questionnaire.getItems().add(learningQuestionnaire.getName());
        }
        cb_questionnaire.getSelectionModel().selectFirst();
    }


    /**
     * Opens the first window in the ExportQuestionnaire operation.
     * Opens a window in which the user is allowed to choose a Questionnaire.
     */
    public void showExportQuestionnaireWindow() {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/exportQuestionnaire.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Fragebogen exportieren", fxmlLoader);
    }

    @FXML
    public void selectQuestionnaire() {
        try {
            learningQuestionnaireList = simpleLearningQuestionnaireService.readAll();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Lesen der Fragebögen fehlgeschlagen",
                "Fehler beim Lesen der Fragebögen!", "");
        }
        //Check if there are questionnaires
        if (learningQuestionnaireList.isEmpty()) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen Auswahl kann nicht angezeigt werden",
                "Fehler!", "Es ist noch kein Fragebogen vorhanden!");
            stage.close();
            return;
        }

        //Get the Selected Item /Study Mode.
        var selectedLearningQuestionnaire = learningQuestionnaires.get(cb_questionnaire.getSelectionModel().getSelectedIndex());
        LearningQuestionnaire studyMode = null;
        try {
            studyMode = simpleLearningQuestionnaireService.getSelected();
            exportService.exportSelectedQuestionnaire(selectedLearningQuestionnaire);

            stage.close();
            LOG.info("Successfully exported the questionnaire.");
            alertController.showStandardAlert(Alert.AlertType.INFORMATION,
                "Fragenbogen exportieren", "Der Fragebogen '" + selectedLearningQuestionnaire.getName()
                    + "' wurde erfolgreich exportiert!", "");

        } catch (ServiceException e) {
            LOG.error("Failed to export the questionnaire.");
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen kann nicht exportiert werden",
                "Fehler!", "Der ausgewählte Fragebogen konnte nicht exportiert werden!");

        } catch (FileExistsException e) {
            LOG.info("File already exists exported the questionnaire.");
            if (alertController.showStandardConfirmationAlert("Fragenbogen exportieren", "Ein Fragebogen mit dem Namen '"
                + selectedLearningQuestionnaire.getName() + "' existiert bereits. Soll dieser überschrieben werden?", "")) {

                try { //overwrite file
                    exportService.overwriteFile(selectedLearningQuestionnaire);

                    stage.close();
                    LOG.info("Successfully exported the questionnaire.");
                    alertController.showStandardAlert(Alert.AlertType.INFORMATION,
                        "Fragenbogen exportieren", "Der Fragebogen '" + selectedLearningQuestionnaire.getName()
                            + "' wurde erfolgreich exportiert!", "");

                } catch (ServiceException | FileExistsException e1) {
                    LOG.error("Failed to overwrite the export questionnaire.");
                    alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen kann nicht überschrieben werden",
                        "Fehler!", "Der ausgewählte Fragebogen konnte beim Exportieren nicht überschrieben werden!");
                }
            }
        }
        if (studyMode != null) {
            LOG.info("Study: " + studyMode.getName() + " Selected: " + selectedLearningQuestionnaire.getName());
        }

        //Refresh the StudyMode again.
        //StudyMode is starting from the beginning.
        try {
            iQuestionnaireService.deselectAllQuestionnaires();
            simpleLearningQuestionnaireService.select(studyMode);
            lerntiaService.loadQuestionnaireAndGetFirstQuestion();
        } catch (ServiceException e) {
            LOG.error("Learning questionnaire can't be retrieved.");
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen kann nicht angezeigt werden",
                "Fehler!", "Der aktuelle Lernfragebogen konnte nicht geladen werden!");
        }
    }
}
