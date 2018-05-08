package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import javafx.fxml.FXML;
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

import static org.springframework.util.Assert.notNull;

@Controller
public class LerntiaMainController {

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
    private AnswerController answer1Controller;
    @FXML
    private AnswerController answer2Controller;
    @FXML
    private AnswerController answer3Controller;
    @FXML
    private AnswerController answer4Controller;
    @FXML
    private AnswerController answer5Controller;
    @FXML
    private QuestionController qLabelController;
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
    @FXML
    private Label qLabel;
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


    @Autowired
    public LerntiaMainController(LerntiaService lerntiaService) {
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
        notNull(qLabel, "'qLabel' should not be null");
        notNull(answer1, "'answer1' should not be null");
        notNull(answer2, "'answer2' should not be null");
        notNull(answer3, "'answer3' should not be null");
        notNull(answer4, "'answer4' should not be null");
        notNull(answer5, "'answer5' should not be null");
        //Example for selecting the fourth answer
        answer4Controller.setSelected(fourthAnswer, true);
        qLabel.setText("Hallo");
        answer1.setText("Hiiiiiii");

    }


}
