package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleLearningQuestionnaireService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Controller
public class SelectQuestionnaireController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SimpleLearningQuestionnaireService learningQuestionnaireService;
    private final LerntiaMainController lerntiaMainController;

    private List<LearningQuestionnaire> learningQuestionnaireList;

    @FXML
    private ChoiceBox<String> cb_questionnaire;

    public SelectQuestionnaireController(SimpleLearningQuestionnaireService learningQuestionnaireService, LerntiaMainController lerntiaMainController) {
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.lerntiaMainController = lerntiaMainController;
    }

    @FXML
    private void initialize() {

        try {
            learningQuestionnaireList = learningQuestionnaireService.readAll();

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < learningQuestionnaireList.size(); i++){
            cb_questionnaire.getItems().add(learningQuestionnaireList.get(i).getName());
        }

        cb_questionnaire.getSelectionModel().selectFirst();
    }

    void showSelectQuestionnaireWindow() {

        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionnaire.fxml"));
        var stage = new Stage();

        try {
            fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("[Lerntia] Fragebogen auswählen");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            LOG.debug("Successfully opened a window for selection a questionnaire.");
        } catch (IOException e) {
            LOG.error("Failed to open a window for selecting a questionnaire. " + e.getMessage());
        }
    }

    public void selectQuestionnaire(ActionEvent actionEvent) {

        int selectedQuestionnaireIndex = cb_questionnaire.getSelectionModel().getSelectedIndex();
        LearningQuestionnaire selectedQuestionnaire = learningQuestionnaireList.get(selectedQuestionnaireIndex);

        // unselect all questionnaires

        for (int i = 0; i < learningQuestionnaireList.size(); i++){
            try {
                learningQuestionnaireService.deselect(learningQuestionnaireList.get(i));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

        // select questionnaire

        try {
            learningQuestionnaireService.select(selectedQuestionnaire);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        // show first question of new questionnaire

        try {
            lerntiaMainController.getAndShowTheFirstQuestion();
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
