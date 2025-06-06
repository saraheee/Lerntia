package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionnaireService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SelectQuestionnaireController {

    private final ILearningQuestionnaireService learningQuestionnaireService;
    private final IQuestionnaireService iQuestionnaireService;
    private final LerntiaMainController lerntiaMainController;
    private final WindowController windowController;
    private final AlertController alertController;

    private List<LearningQuestionnaire> learningQuestionnaireList;

    @FXML
    private ComboBox<String> cb_questionnaire;

    public SelectQuestionnaireController(
        ILearningQuestionnaireService learningQuestionnaireService,
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
                "Fehler", "Die Fragebögen konnten nicht aus der Datenbank gelesen werden!");
        }

        for (LearningQuestionnaire aLearningQuestionnaireList : learningQuestionnaireList) {
            cb_questionnaire.getItems().add(aLearningQuestionnaireList.getName());
        }

        cb_questionnaire.getSelectionModel().selectFirst();
    }

    void showSelectQuestionnaireWindow() {

        try {
            learningQuestionnaireList = learningQuestionnaireService.readAll();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Lesen der Fragebögen fehlgeschlagen",
                "Fehler beim Lesen der Fragebögen!", "");
        }

        if (learningQuestionnaireList == null || learningQuestionnaireList.isEmpty()) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen Auswahl kann nicht angezeigt werden",
                "Fehler!", "Es ist noch kein Fragebogen vorhanden");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionnaire.fxml"));
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
                "Fehler!", "Der zuvor ausgewählte Fragebogen konnte nicht vergessen werden.");
        }

        // select questionnaire

        try {
            learningQuestionnaireService.select(selectedQuestionnaire);
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen auswählen fehlgeschlagen",
                "Fehler!", "Der Fragebogen konnte nicht ausgewählt werden!");
        }

        // show first question of new questionnaire

        lerntiaMainController.getAndShowTheFirstQuestion();

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
