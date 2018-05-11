package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
    private QuestionController qLabelController;
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
    private AudioController audioButtonController;
    @FXML
    private ImageController zoomButtonController;

    @Autowired
    public LerntiaMainController(LerntiaService lerntiaService) {
        notNull(lerntiaService, "'lerntiaService' should not be null");
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void initialize() {
        mainWindowLeft.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(25));
        mainWindowRight.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(75));

        //Example for setting some answer text
        //TODO: delete me, when we have real questions and answers
        answer1Controller.setAnswerText("Sehr gut. Eine bessere Stimme hätte ich mir nie vorstellen können.");
        answer2Controller.setAnswerText("Sie ist ok, aber gibt es keine bessere?");
        answer3Controller.setAnswerText("Ich kann sie gut verstehen!");
        answer4Controller.setAnswerText("Sie spricht sehr deutlich.");
        answer5Controller.setAnswerText("Ich weiß nicht, wie ich sie auf Dauer ertragen soll.");

        //Example for setting a question text
        qLabelController.setQuestionText("Wie findest du meine Stimme?");


    }

    public void update(Scene scene) {
        Platform.runLater(() -> scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A) {
                LOG.debug("A key was pressed");
                audioButtonController.setSelected();
            }
            if (e.getCode() == KeyCode.Z) {
                LOG.debug("Z key was pressed");
                zoomButtonController.setSelected();
            }
            if (e.getCode() == KeyCode.N) {
                LOG.debug("N key was pressed");
                //TODO: handle next question key event (nextQuestionButton)
            }
            if (e.getCode() == KeyCode.P) {
                LOG.debug("P key was pressed");
                //TODO: handle previous question key event (previousQuestionButton)
            }
            if (e.getCode() == KeyCode.C) {
                LOG.debug("C key was pressed");
                //TODO: handle check answer key event (checkAnswerButton)
            }
            if (e.getCode() == KeyCode.NUMPAD1 || e.getCode() == KeyCode.DIGIT1) {
                LOG.debug("1 key was pressed");
                //TODO: handle answer1 key event
            }
            if (e.getCode() == KeyCode.NUMPAD2 || e.getCode() == KeyCode.DIGIT2) {
                LOG.debug("2 key was pressed");
                //TODO: handle answer2 key event
            }
            if (e.getCode() == KeyCode.NUMPAD3 || e.getCode() == KeyCode.DIGIT3) {
                LOG.debug("3 key was pressed");
                //TODO: handle answer3 key event
            }
            if (e.getCode() == KeyCode.NUMPAD4 || e.getCode() == KeyCode.DIGIT4) {
                LOG.debug("4 key was pressed");
                //TODO: handle answer4 key event
            }
            if (e.getCode() == KeyCode.NUMPAD5 || e.getCode() == KeyCode.DIGIT5) {
                LOG.debug("5 key was pressed");
                //TODO: handle answer5 key event
            }

        }));

    }


    public String getAudioText() {
        return qLabelController.getQuestionText() + '\n'
            + answer1Controller.getAnswerText() + '\n'
            + answer2Controller.getAnswerText() + '\n'
            + answer3Controller.getAnswerText() + '\n'
            + answer4Controller.getAnswerText() + '\n'
            + answer5Controller.getAnswerText();
    }


}
