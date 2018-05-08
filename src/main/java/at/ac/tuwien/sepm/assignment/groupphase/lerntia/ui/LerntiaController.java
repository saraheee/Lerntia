package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

import static org.springframework.util.Assert.notNull;

@Controller
public class LerntiaController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LerntiaService lerntiaService;
    @FXML
    private HBox mainWindow;
    @FXML
    private VBox mainWindowLeft;
    @FXML
    private VBox mainWindowRight;
    @FXML
    private ImageView mainImage;
    @FXML
    private AnswerCheckBoxController answer1Controller;
    @FXML
    private AnswerCheckBoxController answer2Controller;
    @FXML
    private AnswerCheckBoxController answer3Controller;
    @FXML
    private AnswerCheckBoxController answer4Controller;
    @FXML
    private AnswerCheckBoxController answer5Controller;
    @FXML
    private QuestionLabelController qLabelController;
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
    @FXML
    private HBox question;

    @Autowired
    public LerntiaController(LerntiaService lerntiaService) {
        notNull(lerntiaService, "'lerntiaService' should not be null");
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void initialize() {
        mainWindowLeft.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(25));
        mainWindowRight.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(75));

        notNull(answer1Controller, "'answer1Controller' should not be null");
        notNull(answer2Controller, "'answer2Controller' should not be null");
        notNull(answer3Controller, "'answer3Controller' should not be null");
        notNull(answer4Controller, "'answer4Controller' should not be null");
        notNull(answer5Controller, "'answer5Controller' should not be null");
        notNull(qLabelController, "'questionController' should not be null");


        answer1Controller.setAnswerText(firstAnswer, "Hello");
        answer2Controller.setVisible(secondAnswer, false);
        LOG.debug("RE: " + answer3Controller.getAnswerText(thirdAnswer));
        answer4Controller.setSelected(fourthAnswer, true);
        qLabelController.setQuestionText(question, "HAAAAAL");
        LOG.debug("RESS: " + qLabelController.getQuestionText(question));
    }

    @FXML
    private void onAudioButtonClicked() {
        LOG.debug("Audio button clicked");

    }


}
