package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class MenuBarController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ImportFileController importFileController;
    private final CreateCourseController createCourseController;
    private final SelectQuestionnaireController selectQuestionnaireController;
    private final SelectExamController selectExamController;

    private final LerntiaMainController lerntiaMainController;


    @Autowired
    MenuBarController(
        ImportFileController importFileController,
        CreateCourseController createCourseController,
        SelectQuestionnaireController selectQuestionnaireController,
        SelectExamController selectExamController,
        LerntiaMainController lerntiaMainController
    )
    {
        this.importFileController = importFileController;
        this.createCourseController = createCourseController;
        this.selectQuestionnaireController = selectQuestionnaireController;
        this.selectExamController = selectExamController;
        this.lerntiaMainController = lerntiaMainController;
    }

    @FXML
    private void importQuestions() {
        importFileController.showImportWindow();
    }

    @FXML
    public void createCourse(ActionEvent actionEvent) { createCourseController.showCreateCourseWindow(); }

    @FXML
    public void selectQuestionnaire(ActionEvent actionEvent) {
        selectQuestionnaireController.showSelectQuestionnaireWindow();
    }

    @FXML
    public void switchToExamMode(ActionEvent actionEvent) {
        selectExamController.showSelectExamWindow();
    }
}