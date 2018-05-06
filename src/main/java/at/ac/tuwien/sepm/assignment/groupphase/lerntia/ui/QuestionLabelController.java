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

import static org.springframework.util.Assert.notNull;

@Controller
public class QuestionLabelController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private Label question;

    public String getQuestion() {
        return question.getText();
    }

    public void setQuestion(String value) {
        question.setText(value);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //notNull(question, "'question' should not be null");
    }
}


