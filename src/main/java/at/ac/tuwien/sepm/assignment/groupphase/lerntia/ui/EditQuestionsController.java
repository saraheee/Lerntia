package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.lang.invoke.MethodHandles;

@Controller
public class EditQuestionsController {


    @FXML public TextField tf_question;
    @FXML public TextField tf_answer2;
    @FXML public TextField tf_answer3;
    @FXML public TextField tf_answer4;
    @FXML public TextField tf_answer5;
    @FXML public TextField tf_answer1;
    @FXML public ImageView iv_image;
    @FXML public TextField tf_currectAnswer;
    @FXML public TextField tf_optionalFeedback;

    private final LerntiaMainController lerntiaMainController;
    private final IQuestionService questionDAO;
    private final WindowController windowController;

    private Question selectedQuestion;
    private Stage stage;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public EditQuestionsController(LerntiaMainController lerntiaMainController, IQuestionService questionDAO, WindowController windowController) {
        this.lerntiaMainController = lerntiaMainController;
        this.questionDAO = questionDAO;
        this.windowController = windowController;
    }


   public void initialize(){
        tf_question.setText("Frage");
        tf_answer1.setText("Antwort 1");
        tf_answer2.setText("Antwort 2");
        tf_answer3.setText("Antwort 3");
        tf_answer4.setText("Antwort 4");
        tf_answer5.setText("Antwort 5");
        tf_currectAnswer.setText("Currect Answer");
        tf_optionalFeedback.setText("Optional Feedback");
    }
    /**
     * Opens the first Window in the SelectQuestionAdministrate operation.
     * Opens a window in which the user can See all the Questions .
     */
    public void showEditQuestionsControllerWindow(Question selectedQuestion){
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/editQuestion.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Frage bearbeiten", fxmlLoader);
        this.selectedQuestion = selectedQuestion;
        tf_question.setText(selectedQuestion.getQuestionText());
        tf_answer1.setText(selectedQuestion.getAnswer1());
        tf_answer2.setText(selectedQuestion.getAnswer2());
        tf_answer3.setText(selectedQuestion.getAnswer3());
        tf_answer4.setText(selectedQuestion.getAnswer4());
        tf_answer5.setText(selectedQuestion.getAnswer5());
        tf_currectAnswer.setText(selectedQuestion.getCorrectAnswers());
        tf_optionalFeedback.setText(selectedQuestion.getOptionalFeedback());
        //Todo Load the Current Image
    }

    @FXML
    public void editButton(ActionEvent actionEvent) {
        LOG.info("Edit Button Clicked");
        Question newData = new Question();
        newData.setQuestionText(tf_question.getText());
        newData.setAnswer1(tf_answer1.getText());
        newData.setAnswer2(tf_answer2.getText());
        newData.setAnswer3(tf_answer3.getText());
        newData.setAnswer4(tf_answer4.getText());
        newData.setAnswer5(tf_answer5.getText());
        newData.setCorrectAnswers(tf_currectAnswer.getText());
        newData.setOptionalFeedback(tf_optionalFeedback.getText());
        newData.setId(selectedQuestion.getId());
        newData.setPicture(selectedQuestion.getPicture());
        AlertController alertController = new AlertController();
        try {
            questionDAO.update(newData);
            LOG.info("Editing Completed - Refeshing Learning");
            lerntiaMainController.getAndShowTheFirstQuestion();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.WARNING,"Bearbeitungsfehler",e.getMessage(),null);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        this.stage.close();
        alertController.showStandardAlert(Alert.AlertType.INFORMATION,"Frage Bearbeiten", "Die Frage wurde erfolgreich bearbeitet", null);
    }

    @FXML
    public void imageButton(ActionEvent actionEvent) {
        //Todo - Change Image
    }
}
