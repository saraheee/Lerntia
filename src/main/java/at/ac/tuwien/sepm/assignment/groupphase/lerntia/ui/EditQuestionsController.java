package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

@Controller
public class EditQuestionsController {


    @FXML public TextField tf_question;
    @FXML public TextField tf_answer2;
    @FXML public TextField tf_answer3;
    @FXML public TextField tf_answer4;
    @FXML public TextField tf_answer5;
    @FXML public TextField tf_answer1;
    @FXML public ImageView iv_image;

    private final LerntiaMainController lerntiaMainController;
    private final IQuestionService questionDAO;
    private final WindowController windowController;
    private Question selectedQuestion;
    private Stage stage;



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
    }

    @FXML
    public void editButton(ActionEvent actionEvent) {
    }

    @FXML
    public void imageButton(ActionEvent actionEvent) {
    }
}
