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
import javafx.scene.control.Label;
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
    private final EditExamController editExamController;
    public Label studentNameLabel;
    public Label matriculationNumberLabel;
    public Label programNumberLabel;

    private List<ExamQuestionnaire> examQuestionnaireList;

    @FXML
    private ComboBox<String> cb_exam;
    private Stage windowStage;
    private boolean selectingCanceled = false;

    public SelectExamController(
        SimpleExamQuestionnaireService examQuestionnaireService,
        IQuestionnaireService iQuestionnaireService,
        LerntiaMainController lerntiaMainController,
        WindowController windowController,
        AlertController alertController,
        EditExamController editExamController
    ) {
        this.examQuestionnaireService = examQuestionnaireService;
        this.iQuestionnaireService = iQuestionnaireService;
        this.lerntiaMainController = lerntiaMainController;
        this.windowController = windowController;
        this.alertController = alertController;
        this.editExamController = editExamController;
    }

    @FXML
    private void initialize() {

        try {
            examQuestionnaireList = examQuestionnaireService.readAll();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfungen lesen fehlgeschlagen",
                "Fehler", "Die Prüfungen konnten nicht gelesen werden!");
        }

        for (ExamQuestionnaire anExamQuestionnaireList : examQuestionnaireList) {
            cb_exam.getItems().add(anExamQuestionnaireList.getName());
        }

        cb_exam.getSelectionModel().selectFirst();
    }

    void showSelectExamWindow() throws ControllerException {

        try {
            examQuestionnaireList = examQuestionnaireService.readAll();
        } catch (ServiceException e) {
            //Todo better exception handling
        }

        if (examQuestionnaireList.isEmpty()) {
            throw new ControllerException("No Exams available");
        }

        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectExam.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowStage = windowController.openNewWindow("Fragebogen auswählen und Studentendaten überprüfen", fxmlLoader);

        windowStage.setOnCloseRequest(event -> {
            var alertController = new AlertController();
            if (alertController.showStandardConfirmationAlert("Prüfungsauswahl abbrechen?",
                "Soll die Auswahl einer Prüfung wirklich abgebrochen werden?",
                "Bei einem Abbruch wird nicht in den Prüfungsmodus gewechselt!")) {
                LOG.debug("Canceled selecting an exam!");
                setSelectingCanceled(true);
                return;
            }
            event.consume();
        });
    }

    public boolean getSelectingCanceled() {
        return selectingCanceled;
    }

    public void setSelectingCanceled(boolean selectingCanceled) {
        this.selectingCanceled = selectingCanceled;
    }

    public void selectExam(ActionEvent actionEvent) {

        int selectedQuestionnaireIndex = cb_exam.getSelectionModel().getSelectedIndex();
        ExamQuestionnaire selectedQuestionnaire = examQuestionnaireList.get(selectedQuestionnaireIndex);

        // show first question of new questionnaire

        try {
            lerntiaMainController.setExamMode(true);
            lerntiaMainController.switchToExamMode();
            lerntiaMainController.prepareExamQuestionnaire(selectedQuestionnaire);
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfungsmodus anzeigen fehlgeschlagen.",
                "Fehler", "Es ist nicht möglich in den Prüfungsmodus zu wechseln!");
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void selectExamAndEdit(ActionEvent actionEvent) {
        int selectedQuestionnaireIndex = cb_exam.getSelectionModel().getSelectedIndex();
        ExamQuestionnaire selectedQuestionnaire = examQuestionnaireList.get(selectedQuestionnaireIndex);

        editExamController.showSelectExamWindow(selectedQuestionnaire);
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void changeStudentInfo(ActionEvent actionEvent) {
        // todo student data
        LOG.debug("Clicked on change student data.");
    }
}
