package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnswerCheckBoxController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CheckBox answerBox;

    public String getAnswerText(HBox answer) {
        if (checkAnswer(answer)) {
            return answerBox.getText();
        }
        return null;
    }

    public void setAnswerText(HBox answer, String text) {
        if (checkAnswer(answer)) {
            answerBox.setText(text);
        }
    }

    public void setSelected(HBox answer, boolean isSelected) {
        if (checkAnswer(answer)) {
            answerBox.setSelected(isSelected);
        }
    }

    public boolean isSelected(HBox answer) {
        return checkAnswer(answer) && answerBox.isSelected();
    }

    public void setVisible(HBox answer, boolean isVisible) {
        if (checkAnswer(answer)) {
            answerBox.setVisible(isVisible);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private boolean checkAnswer(HBox answer) {
        if (answer.getChildren().get(0) instanceof CheckBox) {
            this.answerBox = (CheckBox) answer.getChildren().get(0);
            LOG.debug("Successfully checked the answer.");
            return true;
        } else {
            LOG.error("Checking answer failed. Not an instance of 'checkbox'.");
            return false;
        }
    }
}
