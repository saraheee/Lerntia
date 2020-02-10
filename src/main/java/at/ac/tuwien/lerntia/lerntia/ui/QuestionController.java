package at.ac.tuwien.lerntia.lerntia.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class QuestionController implements Initializable {

    @FXML
    private Label question;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public String getQuestionText() {
        return question.getText();
    }

    public void setQuestionText(String text) {
        question.setMnemonicParsing(false);
        question.setText(text);
    }
}



