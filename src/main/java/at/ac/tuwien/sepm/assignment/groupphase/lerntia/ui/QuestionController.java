package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class QuestionController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());



    //private Label questionLabel;

    QuestionController() {

    }


   /* public String getQuestion() {
        return qLabel.getText();
    }

    public void setQuestion(String value) {
        qLabel.setText(value);
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //notNull(qLabel, "'qLabel' should not be null");
    }



    /* //workaround to access label nodes from their parent nodes
    public String getQuestionText(HBox question) {
        if (checkQuestion(question)) {
            return questionLabel.getText();
        }
        return null;
    }

    public void setQuestionText(HBox question, String text) {
        if (checkQuestion(question)) {
            questionLabel.setText(text);
        }
    }

    private boolean checkQuestion(HBox question) {
        if (question.getChildren().get(0) instanceof Label) {
            this.questionLabel = (Label) question.getChildren().get(0);
            LOG.debug("Successfully checked the question.");
            return true;
        } else {
            LOG.error("Checking question failed. Not an instance of 'label'.");
            return false;
        }
    } */

}



