package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.springframework.util.Assert.notNull;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnswerCheckBoxController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @FXML
    private CheckBox answer1;
    @FXML
    private CheckBox answer2;
    @FXML
    private CheckBox answer3;
    @FXML
    private CheckBox answer4;
    @FXML
    private CheckBox answer5;

    private ArrayList<CheckBox> answerList = new ArrayList<>();

    public String getAnswer(int answer) {
        return answerList.get(answer-1).getText();
    }

    public void setAnswer(String text, int answer) {
        answerList.get(answer-1).setText(text);
    }

    public void selectAnswer(boolean isSelected, int answer) {
        answerList.get(answer-1).setSelected(isSelected);
    }

    public void setVisible(boolean isVisible, int answer) {
        answerList.get(answer-1).setVisible(isVisible);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        notNull(answer1, "'answer1' should not be null");
        notNull(answer2, "'answer1' should not be null");
        */

        answerList.add(answer1);
        answerList.add(answer2);
        answerList.add(answer3);
        answerList.add(answer4);
        answerList.add(answer5);
    }
}
