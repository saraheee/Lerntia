package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
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
public class ExportQuestionnaireController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML public ComboBox<String> cb_questionnaire;
    private Stage stage;
    private List<LearningQuestionnaire> learningQuestionnaires;
    private LearningQuestionnaire selectedLearningQuestionnaire;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final WindowController windowController;

    @Autowired
    public ExportQuestionnaireController(
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
        WindowController windowController
    ){
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.windowController = windowController;
    }


    @FXML
    public void initialize() {
        try {
            this.learningQuestionnaires = simpleLearningQuestionnaireService.readAll();
        } catch (ServiceException e) {
            LOG.error("Failed to initialize AdministrateQuestionnaireController");
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
    }
}
