package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;

import static org.springframework.util.Assert.notNull;

@Controller
public class LerntiaMainController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IMainLerntiaService lerntiaService;
    private final String BREAK = "....";

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
    private ZoomButtonController zoomButtonController;


    // question to be displayed and to be used for checking whether the selected answers were correct
    Question question;
    File imageFile;

    @Autowired
    public LerntiaMainController(IMainLerntiaService lerntiaService) {
        notNull(lerntiaService, "'lerntiaService' should not be null");
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void initialize() {
        mainWindowLeft.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(25));
        mainWindowRight.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(75));

        try {
            getAndShowTheFirstQuestion();
        } catch (ControllerException e) {

        }
    }

    public void update(Scene scene) {
        Platform.runLater(() -> scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A) {
                LOG.debug("A key was pressed");
                audioButtonController.setSelected();
            }
            if (e.getCode() == KeyCode.Z) {
                LOG.debug("Z key was pressed, imageFile = {}", imageFile);
                zoomButtonController.setSelected();
            }
            if (e.getCode() == KeyCode.N) {
                LOG.debug("N key was pressed");
                getAndShowNextQuestion();
            }
            if (e.getCode() == KeyCode.P) {
                LOG.debug("P key was pressed");
                getAndShowPreviousQuestion();
            }
            if (e.getCode() == KeyCode.C) {
                LOG.debug("C key was pressed");
                checkIfQuestionWasCorrect();
            }
            if (e.getCode() == KeyCode.NUMPAD1 || e.getCode() == KeyCode.DIGIT1) {
                LOG.debug("1 key was pressed");
                answer1Controller.setSelected(!answer1Controller.isSelected());
            }
            if (e.getCode() == KeyCode.NUMPAD2 || e.getCode() == KeyCode.DIGIT2) {
                LOG.debug("2 key was pressed");
                answer2Controller.setSelected(!answer2Controller.isSelected());
            }
            if (e.getCode() == KeyCode.NUMPAD3 || e.getCode() == KeyCode.DIGIT3) {
                LOG.debug("3 key was pressed");
                answer3Controller.setSelected(!answer3Controller.isSelected());
            }
            if (e.getCode() == KeyCode.NUMPAD4 || e.getCode() == KeyCode.DIGIT4) {
                LOG.debug("4 key was pressed");
                answer4Controller.setSelected(!answer4Controller.isSelected());
            }
            if (e.getCode() == KeyCode.NUMPAD5 || e.getCode() == KeyCode.DIGIT5) {
                LOG.debug("5 key was pressed");
                answer5Controller.setSelected(!answer5Controller.isSelected());
            }

        }));

    }

    @FXML
    private void checkIfQuestionWasCorrect() {
        // gather the info about the checked answers
        String checkedAnswers = "";
        if(answer1Controller.isSelected()) { checkedAnswers += "1"; }
        if(answer2Controller.isSelected()) { checkedAnswers += "2"; }
        if(answer3Controller.isSelected()) { checkedAnswers += "3"; }
        if(answer4Controller.isSelected()) { checkedAnswers += "4"; }
        if(answer5Controller.isSelected()) { checkedAnswers += "5"; }

        boolean answersCorrect = checkedAnswers.equals(question.getCorrectAnswers());
        LOG.debug("Correct answers: {} ; selected answers: {} ; selected is correct: {}", question.getCorrectAnswers(), checkedAnswers, answersCorrect);

        if(answersCorrect) {
            showAnAlert(Alert.AlertType.INFORMATION,"Antworten richtig!", "Alle Antworten sind richtig.", "Die nächste Frage wird angezeigt.");
        } else {
            showAnAlert(Alert.AlertType.WARNING,"Antworten nicht richtig.", "Richtige Antworten: " +
                question.getCorrectAnswers(), "Die nächste Frage wird angezeigt.");
        }
        // send checked answers to service (in order to use it for statistics and learning algorithm)
        try {
            Question mockQuestion = new Question();
            mockQuestion.setId(question.getId());
            mockQuestion.setCorrectAnswers(checkedAnswers);
            LOG.info("Trying to send {} answers on question \"{}\"",
                mockQuestion.getCorrectAnswers(), mockQuestion.getId());
            lerntiaService.recordCheckedAnswers(mockQuestion);
        } catch (ServiceException e) {
            LOG.error("Could not check whether the answer was correct");
            showAnAlert(Alert.AlertType.ERROR, "Überprüfung fehlgeschlagen", "Das Resultat konnte nicht zur" +
                " Serviceschicht geschickt werden", e.getLocalizedMessage());
        }
        getAndShowNextQuestion();
    }

    private void getAndShowTheFirstQuestion() throws ControllerException{
        try {
            question = lerntiaService.getFirstQuestion();
        } catch (ServiceException e) {
            //LOG.warn("Could not get the first question to be displayed: " + e.getLocalizedMessage());
            //showAnAlert(Alert.AlertType.WARNING, "Keine erste Frage", "Es wurden keine Fragen gefunden", "Sind die Fragen implementiert und mit einem Fragebogen verbunden?");

            throw new ControllerException("Es gibt noch keine Fragen");
        }
        showQuestionAndAnswers();
    }


    @FXML
    private void getAndShowNextQuestion() {
            try {
            question = lerntiaService.getNextQuestionFromList();
            showQuestionAndAnswers();
        } catch (ServiceException e1) {
            LOG.warn("No next question to be displayed.");
            // todo add statistics after that is implemented
            showAnAlert(Alert.AlertType.ERROR, "Keine weiteren Fragen", "Du bist am Ende angelangt.", "Statistiken: ");
                try {
                    getAndShowTheFirstQuestion();
                } catch (ControllerException e) {
                    e.printStackTrace();
                }
            }
    }

    @FXML
    private void getAndShowPreviousQuestion() {
        try {
            question = lerntiaService.getPreviousQuestionFromList();
            showQuestionAndAnswers();
        } catch (ServiceException e1) {
            LOG.warn("No previous question to be displayed.");
            // todo add statistics after that is implemented
            showAnAlert(Alert.AlertType.ERROR, "Keine früheren Fragen", "Du bist am Anfang.", "Statistiken: ");
            try {
                getAndShowTheFirstQuestion();
            } catch (ControllerException e) {
                e.printStackTrace();
            }
        }
    }

    private void showQuestionAndAnswers() {
        if(question == null) {
            LOG.error("ShowQuestionAndAnswers method was called, although the controller did not get a valid Question.");
            showAnAlert(Alert.AlertType.ERROR, "Keine Fragen verfügbar", "", "");
            return;
        }

        qLabelController.setQuestionText(question.getQuestionText());

        setAnswerText(answer1Controller, question.getAnswer1());
        setAnswerText(answer2Controller, question.getAnswer2());
        setAnswerText(answer3Controller, question.getAnswer3());
        setAnswerText(answer4Controller, question.getAnswer4());
        setAnswerText(answer5Controller, question.getAnswer5());

        // show image in the main window or hide the zoom button if there is no image to be shown
        if(question.getPicture() == null || question.getPicture().trim().isEmpty()) {
            mainImage.setVisible(false);
            zoomButtonController.setVisible(false);
            zoomButtonController.setImageFile(null);
            LOG.debug("No image to be displayed for this question");
        } else {
            try {
                String imagePath = System.getProperty("user.dir") + File.separator + "ss18_sepm_qse_08" + File.separator
                    + "img" + File.separator + question.getPicture();
                LOG.debug("Image path: " + imagePath); // todo revisit this path after discussing the format in which images are to be saved in
                File imageFile =  new File(imagePath);
                zoomButtonController.setImageFile(imageFile);
                Image image = new Image(imageFile.toURI().toURL().toExternalForm());
                mainImage.setImage(image);
                mainImage.setVisible(true);
                zoomButtonController.setVisible(true);
                LOG.info("Image for this vehicle is displayed: '{}'", question.getPicture());
            } catch (MalformedURLException e) {
                LOG.debug("Exception while trying to display image " + e.getMessage());
            }
        }
    }

    private void setAnswerText(AnswerController answerController, String answertText) {
        answerController.setSelected(false);
        // if answer is not provided the whole box will be invisible
        if(answertText == null) {
            answerController.setVisible(false);
            return;
        }
        answerController.setVisible(true);
        answerController.setAnswerText(answertText);
    }

    String getQuestion() {
        return qLabelController.getQuestionText();
    }
    String getAnswer1() {
        return answer1Controller.getAnswerText();
    }
    String getAnswer2() {
        return answer2Controller.getAnswerText();
    }
    String getAnswer3() {
        return answer3Controller.getAnswerText();
    }
    String getAnswer4() {
        return answer4Controller.getAnswerText();
    }
    String getAnswer5() {
        return answer5Controller.getAnswerText();
    }


    void showAnAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setResizable(true);
        alert.showAndWait();
    }

    public File getImageFile() {
        return imageFile;
    }
}
