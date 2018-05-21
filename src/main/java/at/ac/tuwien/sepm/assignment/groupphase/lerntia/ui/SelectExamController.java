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

    private List<ExamQuestionnaire> examQuestionnaireList;

    @FXML
    private ComboBox<String> cb_exam;

    public SelectExamController(
        SimpleExamQuestionnaireService examQuestionnaireService,
        IQuestionnaireService iQuestionnaireService,
        LerntiaMainController lerntiaMainController,
        WindowController windowController
    ) {
        this.examQuestionnaireService = examQuestionnaireService;
        this.iQuestionnaireService = iQuestionnaireService;
        this.lerntiaMainController = lerntiaMainController;
        this.windowController = windowController;
    }

    @FXML
    private void initialize() {

        try {
            examQuestionnaireList = examQuestionnaireService.readAll();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < examQuestionnaireList.size(); i++) {
            cb_exam.getItems().add(examQuestionnaireList.get(i).getName());
        }

        cb_exam.getSelectionModel().selectFirst();
    }

    void showSelectExamWindow() {

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
            e.printStackTrace();
        }

        // select questionnaire

        try {
            examQuestionnaireService.select(selectedQuestionnaire);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        // show first question of new questionnaire

        try {
            lerntiaMainController.setExamMode(true);
            lerntiaMainController.switchToExamMode();
            lerntiaMainController.getAndShowTheFirstQuestion();
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }
}
