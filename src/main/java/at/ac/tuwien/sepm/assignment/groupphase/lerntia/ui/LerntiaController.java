package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
    private QuestionLabelController questionController;
    @FXML
    private HBox firstAnswer;
    @FXML
    private HBox secondAnswer;
    @FXML
    private HBox thirdAnswer;
    @FXML
    private HBox fourthAnswer;


    //@Autowired
    public LerntiaController(LerntiaService lerntiaService) {
        notNull(lerntiaService, "'lerntiaService' should not be null");
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void initialize() {
        mainWindowLeft.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(25));
        mainWindowRight.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(75));
        /*
        notNull(answer1Controller, "'answer1Controller' should not be null");
        notNull(answer2Controller, "'answer2Controller' should not be null");
        notNull(answer3Controller, "'answer3Controller' should not be null");
        notNull(answer4Controller, "'answer4Controller' should not be null");
        notNull(answer5Controller, "'answer5Controller' should not be null");
        notNull(questionController, "'questionController' should not be null");
        */

        //Example for selecting the first answer
        LOG.debug("HERE:  {}", firstAnswer == null);
        var node = firstAnswer.getChildren().get(0);
        if (node instanceof CheckBox) {
            ((CheckBox) node).setSelected(true);
        }
    }

    @FXML
    private void onAudioButtonClicked() {
        LOG.debug("Audio button clicked");

    }


}
