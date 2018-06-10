package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.IQuestionnaireExportService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleLearningQuestionnaireService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Controller
public class ExportQuestionnaireController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML public ComboBox<String> cb_questionnaire;
    private Stage stage;
    private List<LearningQuestionnaire> learningQuestionnaires;
    private LearningQuestionnaire selectedLearningQuestionnaire;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final WindowController windowController;
    private List learningQuestionnaireList;
    private AlertController alertController;
    private final IQuestionnaireExportService exportService;
    private final IMainLerntiaService lerntiaService;
    private final IQuestionnaireService iQuestionnaireService;
    private final LerntiaMainController lerntiaMainController;
    @Autowired
    public ExportQuestionnaireController(
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
        WindowController windowController,
        IQuestionnaireExportService iQuestionnaireExportService,
        AlertController alertController,
        IMainLerntiaService lerntiaService,
        IQuestionnaireService iQuestionnaireService,
        LerntiaMainController lerntiaMainController
    ){
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.windowController = windowController;
        this.exportService = iQuestionnaireExportService;
        this.alertController = alertController;
        this.lerntiaService = lerntiaService;
        this.iQuestionnaireService = iQuestionnaireService;
        this.lerntiaMainController = lerntiaMainController;
    }


    @FXML
    public void initialize() {
        try {
            this.learningQuestionnaires = simpleLearningQuestionnaireService.readAll();
        } catch (ServiceException e) {
            LOG.error("Failed to initialize ExportQuestionnaireController");
        }

        for (LearningQuestionnaire learningQuestionnaire : learningQuestionnaires) {
            cb_questionnaire.getItems().add(learningQuestionnaire.getName());
        }
        cb_questionnaire.getSelectionModel().selectFirst();
    }


    /**
     * Opens the first window in the AdministrateQuestionnaire operation.
     * Opens a window in which the user is allowed to choose a Questionnaire.
     */
    public void showExportQuestionnaireWindow() {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/exportQuestionnaire.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Fragebogen exportieren", fxmlLoader);
    }

    @FXML
    public void selectQuestionnaire(ActionEvent actionEvent) {
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
        selectedLearningQuestionnaire = learningQuestionnaires.get(cb_questionnaire.getSelectionModel().getSelectedIndex());
        LearningQuestionnaire studyMode = null;
        try {
            studyMode = simpleLearningQuestionnaireService.getSelected();
        } catch (ServiceException e) {
            LOG.error("Selected Questionnaire can't be retrieved.");
        }
        LOG.info("Study: " + studyMode.getName() + " Selected: " + selectedLearningQuestionnaire.getName());
        alertController.showStandardAlert(Alert.AlertType.INFORMATION,
            "Fragenbogen Exportieren",selectedLearningQuestionnaire.getName()+" wurde erfolgreich exportiert",
            "");
        exportService.exportSelectedQuestionnaire();

        //Refresh the StudyMode again.
        //SutdyMode is beginning Starting from the Beginning.
        try {
            iQuestionnaireService.deselectAllQuestionnaires();
            simpleLearningQuestionnaireService.select(studyMode);
            lerntiaService.loadQuestionnaireAndGetFirstQuestion();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
