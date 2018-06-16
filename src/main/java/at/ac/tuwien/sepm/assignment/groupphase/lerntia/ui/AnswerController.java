package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnswerController implements Initializable {

    @FXML
    private CheckBox answer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public String getAnswerText() {
        return answer.getText();
    }

    public void setAnswerText(String text) {
        answer.setText(text);
    }

    public boolean isSelected() {
        return answer.isSelected();
    }

    public void setSelected(boolean isSelected) {
        answer.setSelected(isSelected);
    }

    public void setVisible(boolean isVisible) {
        answer.setVisible(isVisible);
    }


    void markRed() {
        answer.setTextFill(Color.web("DARKRED"));
    }

    void markGreen() {
        answer.setTextFill(Color.web("DARKGREEN"));
    }

    void markBlack() {
        answer.setTextFill(Color.web("#333333"));
    }

    public boolean isDisabled() {
        return answer.isDisabled();
    }

    void setDisabled(boolean disabled) {
        answer.setDisable(disabled);
    }

}
