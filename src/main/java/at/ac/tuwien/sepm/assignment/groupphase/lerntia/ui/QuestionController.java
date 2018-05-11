package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class QuestionController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private Label question;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public String getQuestionText() {
        return question.getText();
    }

    public void setQuestionText(String text) {
        question.setText(text);
    }
}



