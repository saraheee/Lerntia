package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class LerntiaController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LerntiaService lerntiaService;
    @FXML
    private HBox questionBox;
    @FXML
    private HBox mainWindow;
    @FXML
    private VBox mainWindowLeft;
    @FXML
    private VBox mainWindowRight;
    @FXML
    private ImageView mainImage;
    @FXML
    private HBox firstAnswer;
    @FXML
    private HBox secondAnswer;
    @FXML
    private HBox thirdAnswer;
    @FXML
    private HBox fourthAnswer;
    @FXML
    private HBox fifthAnswer;


    @Autowired
    public LerntiaController(LerntiaService lerntiaService) {
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void initialize() {
        mainWindowLeft.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(25));
        //mainImage.setFitWidth(mainWindow.getWidth() / 100 * 15);
        mainWindowRight.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(75));

      //  setNextQuestionAndAnswers();
    }

    private void setNextQuestionAndAnswers() {
        try {
            Question question = lerntiaService.getQuestion();
            var questionnode = questionBox.getChildren().get(2);
            if(questionnode instanceof Label) {
                ((Label) questionnode).setText(question.getQuestionText());
            } else {
                LOG.error("UI Error: Returned question node not of type Label!");
            }
            setAnswerText(firstAnswer, question.getAnswer1());
            setAnswerText(secondAnswer, question.getAnswer2());
            setAnswerText(thirdAnswer, question.getAnswer3());
            setAnswerText(fourthAnswer, question.getAnswer4());
            setAnswerText(fifthAnswer, question.getAnswer5());
        } catch (ServiceException e) {
            LOG.warn("Could not load next question and answers.");
            // todo: add error alert for e
        }
    }

    private void setAnswerText(HBox answerBox, String answertText) {
        // if answer is not provided the whole box will be invisible
        if(answertText == null) {
            answerBox.setVisible(false);
            return;
        }
        answerBox.setVisible(true);
        var node = answerBox.getChildren().get(0);
        if (node instanceof CheckBox) {
            ((CheckBox) node).setSelected(true);
            ((CheckBox) node).setText(answertText);
        } else {
            LOG.error("UI Error: Returned answer node was not of type checkbox.");
        }
    }

    @FXML
    private void onPlaceHolderButtonClicked() {
        LOG.trace("Placeholder button clicked");
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("[Lerntia] Info");
        alert.setHeaderText("Dieser Button hat noch keine Funktionalität!");
        alert.setResizable(true);
        var dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
        /*try {
            lerntiaService.goToExamMode();
        } catch (ServiceException e) {
            LOG.error("An error occured {}", e.getMessage());
        }*/
    }
}
