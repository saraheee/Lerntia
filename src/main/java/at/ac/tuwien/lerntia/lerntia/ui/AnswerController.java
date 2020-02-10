package at.ac.tuwien.lerntia.lerntia.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnswerController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AudioController audioController;
    @FXML
    private CheckBox answer;

    @Autowired
    AnswerController(AudioController audioController) {
        this.audioController = audioController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public String getAnswerText() {
        return answer.getText();
    }

    public void setAnswerText(String text) {
        answer.setMnemonicParsing(false);
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

    @FXML
    private void onMouseClicked() {
        LOG.debug("Answer selected by mouse!");
        if (this.isSelected()) {
            audioController.readSingleAnswer(this.getAnswerText());
        } else {
            audioController.stopReading();
        }

    }
}
