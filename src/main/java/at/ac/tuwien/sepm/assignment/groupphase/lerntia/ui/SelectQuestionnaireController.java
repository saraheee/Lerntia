package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleLearningQuestionnaireService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Controller
public class SelectQuestionnaireController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SimpleLearningQuestionnaireService learningQuestionnaireService;
    private final IQuestionnaireService iQuestionnaireService;
    private final LerntiaMainController lerntiaMainController;
    private final WindowController windowController;
    private final AlertController alertController;

    private List<LearningQuestionnaire> learningQuestionnaireList;

    @FXML
    private ComboBox<String> cb_questionnaire;

    public SelectQuestionnaireController(
        SimpleLearningQuestionnaireService learningQuestionnaireService,
        IQuestionnaireService iQuestionnaireService,
        LerntiaMainController lerntiaMainController,
        WindowController windowController,
        AlertController alertController
    ) {
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.iQuestionnaireService = iQuestionnaireService;
        this.lerntiaMainController = lerntiaMainController;
        this.windowController = windowController;
        this.alertController = alertController;
    }

    @FXML
    private void initialize() {

        try {
            learningQuestionnaireList = learningQuestionnaireService.readAll();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebögen lesen fehlgeschlagen",
                "Error", "Die Fragebögen konnten nicht aus der Datenbank gelesen werden!");
        }

        for (int i = 0; i < learningQuestionnaireList.size(); i++) {
            cb_questionnaire.getItems().add(learningQuestionnaireList.get(i).getName());
        }

        cb_questionnaire.getSelectionModel().selectFirst();
    }

    void showSelectQuestionnaireWindow() {

        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionnaire.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowController.openNewWindow("Fragebogen auswählen", fxmlLoader);

    }

    public void selectQuestionnaire(ActionEvent actionEvent) {

        int selectedQuestionnaireIndex = cb_questionnaire.getSelectionModel().getSelectedIndex();
        LearningQuestionnaire selectedQuestionnaire = learningQuestionnaireList.get(selectedQuestionnaireIndex);

        // unselect all questionnaires

        try {
            iQuestionnaireService.deselectAllQuestionnaires();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen vergessen fehlgeschlagen",
                "Error", "Der zuvor ausgewählte Fragebogen konnte nicht vergessen werden.");
        }

        // select questionnaire

        try {
            learningQuestionnaireService.select(selectedQuestionnaire);
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen auswählen fehlgeschlagen",
                "Error", "Der Fragebogen konnte nicht ausgewählt werden!");
        }

        // show first question of new questionnaire

        try {
            lerntiaMainController.getAndShowTheFirstQuestion();
        } catch (ControllerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen anzeigen fehlgeschlagen",
                "Error", "Der ausgewählte Fragebogen kann nicht angezeigt werden");
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
