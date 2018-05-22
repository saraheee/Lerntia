package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleLearningQuestionnaireService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Controller
public class AdministrateQuestionnaireController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final SelectQuestionAdministrateController selectQuestionAdministrateController;
    private final IMainLerntiaService lerntiaService;
    private final WindowController windowController;
    private final EditQuestionsController editQuestionsController;
    private Stage stage;

    @FXML
    public ComboBox cb_questionnaire;
    private List<LearningQuestionnaire> learningQuestionnaires;
    private LearningQuestionnaire selectedLearningQuestionnaire;

    @Autowired
    public AdministrateQuestionnaireController(
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
        SelectQuestionAdministrateController selectQuestionAdministrateController,
        IMainLerntiaService lerntiaService,
        WindowController windowController,
        EditQuestionsController editQuestionsController) {
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.selectQuestionAdministrateController = selectQuestionAdministrateController;
        this.lerntiaService = lerntiaService;
        this.windowController = windowController;
        this.editQuestionsController = editQuestionsController;
    }

    @FXML
    public void initialize() {
        try {
            this.learningQuestionnaires = simpleLearningQuestionnaireService.readAll();
        } catch (ServiceException e) {
            LOG.error("Failed to initialize AdministrateQuestionnaireController");
        }

        for (int i = 0; i < learningQuestionnaires.size(); i++) {
            cb_questionnaire.getItems().add(learningQuestionnaires.get(i).getName());
        }
        cb_questionnaire.getSelectionModel().selectFirst();
    }

    @FXML
    public void selectQuestionnaire(ActionEvent actionEvent) {
        //Get the Selected Item.
        selectedLearningQuestionnaire = learningQuestionnaires.get(cb_questionnaire.getSelectionModel().getSelectedIndex());
        editQuestionsController.setQuestionnaire(selectedLearningQuestionnaire);
        LearningQuestionnaire studyMode = null;
        try {
            studyMode = simpleLearningQuestionnaireService.getSelected();
        } catch (ServiceException e) {
            LOG.error("Selected Questionnaire can't be retrieved.");
        }

        LOG.info("Unselect all the Other Questionnaire");
        for (int i = 0; i < learningQuestionnaires.size(); i++) {
            try {
                simpleLearningQuestionnaireService.deselect(learningQuestionnaires.get(i));
            } catch (ServiceException e) {
                LOG.error("Failed to deselect a questionnaire.");
            }
        }

        LOG.info("Select the Questionnaire");
        try {
            simpleLearningQuestionnaireService.select(selectedLearningQuestionnaire);
        } catch (ServiceException e) {
            LOG.error("Can't select Questionnaire");
        }
        LOG.info("Open the New Window which contains a TableView and all Questions.");
        selectQuestionAdministrateController.showSelectQuestionAdministrateWindow(selectedLearningQuestionnaire);
        try {
            simpleLearningQuestionnaireService.deselect(selectedLearningQuestionnaire);
            simpleLearningQuestionnaireService.select(studyMode);
            lerntiaService.loadQuestionnaireAndGetFirstQuestion();
            //Delete this Line if not Needed
            LOG.info("Study: " + studyMode.getName() + " Selected: " + selectedLearningQuestionnaire.getName());

        } catch (ServiceException e) {
            LOG.error("Failed to open the Question managing window.");
        }
        stage.close();
    }

    public LearningQuestionnaire getSelectedQuestionnaire() {
        return this.selectedLearningQuestionnaire;
    }

    /**
     * Opens the first Window in the AdministrateQuestionnare operation.
     * Opens a window in which the user is allowed to choose a Questionnare.
     */
    public void showAdministrateQuestionnaireWindow() {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/administrateQuestionnaire.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Fragebogen verwalten", fxmlLoader);
    }

}
