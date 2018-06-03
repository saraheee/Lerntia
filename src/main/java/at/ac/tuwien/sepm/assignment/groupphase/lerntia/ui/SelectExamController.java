package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleExamQuestionnaireService;
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
public class SelectExamController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SimpleExamQuestionnaireService examQuestionnaireService;
    private final IQuestionnaireService iQuestionnaireService;
    private final LerntiaMainController lerntiaMainController;
    private final WindowController windowController;
    private final AlertController alertController;

    private List<ExamQuestionnaire> examQuestionnaireList;

    @FXML
    private ComboBox<String> cb_exam;

    public SelectExamController(
        SimpleExamQuestionnaireService examQuestionnaireService,
        IQuestionnaireService iQuestionnaireService,
        LerntiaMainController lerntiaMainController,
        WindowController windowController,
        AlertController alertController
    )
    {
        this.examQuestionnaireService = examQuestionnaireService;
        this.iQuestionnaireService = iQuestionnaireService;
        this.lerntiaMainController = lerntiaMainController;
        this.windowController = windowController;
        this.alertController = alertController;
    }

    @FXML
    private void initialize() {

        try {
            examQuestionnaireList = examQuestionnaireService.readAll();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfungen lesen fehlgeschlagen",
                "Fehler", "Die Prüfungen konnten nicht aus der Datenbank gelesen werden!");
        }

        for (ExamQuestionnaire anExamQuestionnaireList : examQuestionnaireList) {
            cb_exam.getItems().add(anExamQuestionnaireList.getName());
        }

        cb_exam.getSelectionModel().selectFirst();
    }

    void showSelectExamWindow() {

        try {
            examQuestionnaireList = examQuestionnaireService.readAll();
        } catch (ServiceException e) {
            // TODO - finish exception handling
        }

        if (examQuestionnaireList.isEmpty()){
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfungs Auswahl kann nicht angezeigt werden",
                "Fehler", "Es ist noch keine Prüfung vorhanden");
            return;
        }

        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectExam.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowController.openNewWindow("Fragebogen auswählen", fxmlLoader);
    }

    public void selectExam(ActionEvent actionEvent) {

        int selectedQuestionnaireIndex = cb_exam.getSelectionModel().getSelectedIndex();
        ExamQuestionnaire selectedQuestionnaire = examQuestionnaireList.get(selectedQuestionnaireIndex);

        // unselect all questionnaires

        try {
            iQuestionnaireService.deselectAllQuestionnaires();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen vergessen fehlgeschlagen",
                "Fehler", "Der zuvor ausgewählte Fragebogen konnte nicht vergessen werden.");
        }

        // select questionnaire

        try {
            examQuestionnaireService.select(selectedQuestionnaire);
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfung auswählen fehlgeschlagen",
                "Fehler", "Die Prüfung konnte nicht ausgewählt werden!");
        }

        // show first question of new questionnaire

        try {
            lerntiaMainController.setExamMode(true);
            lerntiaMainController.switchToExamMode();
            lerntiaMainController.getAndShowTheFirstQuestion();
        } catch (ControllerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfung anzeigen fehlgeschlagen",
                "Fehler", "Die ausgewählte Prüfung kann nicht angezeigt werden");
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfungs modus anzeigen fehlgeschlagen.",
                "Fehler","Nicht möglich in den Prüfungs Modus zu wechseln!");
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
