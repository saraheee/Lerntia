package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamResultsWriterService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.util.List;

import static org.springframework.util.Assert.notNull;

@Controller
public class LerntiaMainController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IMainLerntiaService lerntiaService;
    private final ZoomedImageController zoomedImageController;
    private final AudioController audioController;
    private final AlertController alertController;
    private final IQuestionnaireService questionnaireService;
    private final ILearningQuestionnaireService learningQuestionnaireService;
    private final IExamResultsWriterService iExamResultsWriterService;
    private boolean learnAlgorithmStatus;

    private ConfigReader configReaderSpeech = new ConfigReader("speech");
    private final String BREAK = configReaderSpeech.getValue("break");

    @FXML
    private GridPane mainWindow;
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
    private LearnAlgorithmController learnAlgorithmController;
    @FXML
    private ButtonBar buttonBar;
    @FXML
    private Button checkAnswerButton;
    @FXML
    private Button previousQuestionButton;
    @FXML
    private Button nextQuestionButton;
    @FXML
    private Button handInButton;
    @FXML
    private Button algorithmButton;

    // question to be displayed and to be used for checking whether the selected answers were correct
    private Question question;
    private File imageFile;

    private boolean examMode;

    @Autowired
    public LerntiaMainController(
        IMainLerntiaService lerntiaService,
        AudioController audioController,
        AlertController alertController,
        ILearningQuestionnaireService learningQuestionnaireService,
        ZoomedImageController zoomedImageController,
        IQuestionnaireService questionnaireService,
        IExamResultsWriterService iExamResultsWriterService,
        LearnAlgorithmController learnAlgorithmController
    ) {
        notNull(lerntiaService, "'lerntiaService' should not be null");
        notNull(audioController, "'audioController' should not be null");
        notNull(alertController, "'alertController' should not be null");
        notNull(zoomedImageController, "'zoomedImageController' should not be null");
        notNull(learnAlgorithmController, "learnAlgorithmController should not be null");
        this.lerntiaService = lerntiaService;
        this.audioController = audioController;
        this.alertController = alertController;
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.zoomedImageController = zoomedImageController;
        this.questionnaireService = questionnaireService;
        this.iExamResultsWriterService = iExamResultsWriterService;
        this.learnAlgorithmController = learnAlgorithmController;

        this.learnAlgorithmStatus = false;
    }

    @FXML
    private void initialize() {

        LOG.debug("Main window's min width before: {}", mainWindow.getMinWidth());

        mainImage.fitWidthProperty().bind(mainWindowLeft.widthProperty()); // todo replace

        LOG.debug("Main image fit width after setting it: {} ", mainImage.getFitWidth());

        LOG.debug("Main window's min width set to: {}", mainWindow.getMinWidth());
        LOG.debug("Main window's width set to: {}", mainWindow.getWidth());
        LOG.debug("Main window's PREF width set to: {}", mainWindow.getPrefWidth());
        LOG.debug("Main window LEFT's min width set to: {}", mainWindowLeft.getMinWidth());
        LOG.debug("Main window LEFT's width set to: {}", mainWindowLeft.getWidth());
        LOG.debug("Main window LEFT's PREF width set to: {}", mainWindowLeft.getPrefWidth());
        LOG.debug("mainImage's fit width set to: {}", mainImage.getFitWidth());

        buttonBar.getButtons().remove(handInButton);

        try {
            getAndShowTheFirstQuestion();
        } catch (ControllerException e) {
            //showNoQuestionsAvailable();
            LOG.warn("No first answer. Loop stopped.");
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
                zoomedImageController.onZoomButtonClicked();
            }
            if (e.getCode() == KeyCode.N) {
                LOG.debug("N key was pressed");
                audioController.stopReading();
                audioController.deselectAudioButton();
                getAndShowNextQuestion();
            }
            if (e.getCode() == KeyCode.P) {
                LOG.debug("P key was pressed");
                audioController.stopReading();
                audioController.deselectAudioButton();
                getAndShowPreviousQuestion();
            }
            if (e.getCode() == KeyCode.C) {
                LOG.debug("C key was pressed");
                audioController.stopReading();
                audioController.deselectAudioButton();
                if (!examMode) {
                    checkIfQuestionWasCorrect();
                } else {
                    handIn(null);
                }
            }
            if (e.getCode() == KeyCode.G) {
                LOG.info("G key was pressed.");
                learnAlgorithmController.onAlgorithmButtonPressed();
                if (learnAlgorithmController.isSelected()) {
                    LOG.info("Learn Algorithm is now running");
                } else {
                    LOG.info("Learn Algorithm is now not running.");
                }
                learnAlgorithmStatus = learnAlgorithmController.isSelected();
            }
            if (e.getCode() == KeyCode.NUMPAD1 || e.getCode() == KeyCode.DIGIT1) {
                LOG.debug("1 key was pressed");
                answer1Controller.setSelected(!answer1Controller.isSelected());
                if (answer1Controller.isSelected()) {
                    audioController.readSingleAnswer(answer1Controller.getAnswerText());
                } else {
                    audioController.stopReading();
                }
            }
            if (e.getCode() == KeyCode.NUMPAD2 || e.getCode() == KeyCode.DIGIT2) {
                LOG.debug("2 key was pressed");
                answer2Controller.setSelected(!answer2Controller.isSelected());
                if (answer2Controller.isSelected()) {
                    audioController.readSingleAnswer(answer2Controller.getAnswerText());
                } else {
                    audioController.stopReading();
                }
            }
            if (e.getCode() == KeyCode.NUMPAD3 || e.getCode() == KeyCode.DIGIT3) {
                LOG.debug("3 key was pressed");
                answer3Controller.setSelected(!answer3Controller.isSelected());
                if (answer3Controller.isSelected()) {
                    audioController.readSingleAnswer(answer3Controller.getAnswerText());
                } else {
                    audioController.stopReading();
                }
            }
            if (e.getCode() == KeyCode.NUMPAD4 || e.getCode() == KeyCode.DIGIT4) {
                LOG.debug("4 key was pressed");
                answer4Controller.setSelected(!answer4Controller.isSelected());
                if (answer4Controller.isSelected()) {
                    audioController.readSingleAnswer(answer4Controller.getAnswerText());
                } else {
                    audioController.stopReading();
                }
            }
            if (e.getCode() == KeyCode.NUMPAD5 || e.getCode() == KeyCode.DIGIT5) {
                LOG.debug("5 key was pressed");
                answer5Controller.setSelected(!answer5Controller.isSelected());
                if (answer5Controller.isSelected()) {
                    audioController.readSingleAnswer(answer5Controller.getAnswerText());
                } else {
                    audioController.stopReading();
                }
            }

        }));
        mainImage.setOnMouseClicked((MouseEvent e) -> zoomedImageController.onZoomButtonClicked());
    }


    @FXML
    private void checkIfQuestionWasCorrect() {
        // gather the info about the checked answers
        try {
            String checkedAnswers = getCheckedAnswers();

            boolean answersCorrect = checkedAnswers.equals(question.getCorrectAnswers());
            //LOG.trace("Correct answers: {} ; selected answers: {} ; selected is correct: {}", question.getCorrectAnswers(), checkedAnswers, answersCorrect);
            LOG.info("Save values to Algorithm");
            if (answersCorrect) {
                try {

                    lerntiaService.recordCheckedAnswers(question, answersCorrect);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }

                if (question.getCorrectAnswers().length() == 1) { // only one answer is correct
                    var feedbackPrefix = "Korrekt beantwortet! Folgende Antwortnummer ist richtig: " + checkedAnswers;
                    audioController.readFeedbackText(feedbackPrefix + " " + BREAK + BREAK +
                        question.getOptionalFeedback());

                    alertController.showCorrectAnswerAlert("Antwort richtig!", feedbackPrefix,
                        question.getOptionalFeedback());
                    audioController.stopReading();

                } else {
                    var feedbackPrefix = "Korrekt beantwortet! Folgende Antwortnummern sind richtig: "
                        + formatAnswerNumbers(question.getCorrectAnswers());
                    audioController.readFeedbackText(feedbackPrefix + " " + BREAK + BREAK +
                        question.getOptionalFeedback());

                    alertController.showCorrectAnswerAlert("Antworten richtig!", feedbackPrefix,
                        question.getOptionalFeedback());
                    audioController.stopReading();
                }
            } else {
                try {
                    lerntiaService.recordCheckedAnswers(question, answersCorrect);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                if (question.getCorrectAnswers().length() == 1) { // only one answer is correct
                    var feedbackPrefix = "Falsch beantwortet! Folgende Antwortnummer wäre richtig gewesen: "
                        + formatAnswerNumbers(question.getCorrectAnswers());
                    audioController.readFeedbackText(feedbackPrefix + " " + BREAK + BREAK +
                        question.getOptionalFeedback());

                    alertController.showWrongAnswerAlert("Antwort nicht richtig.", feedbackPrefix,
                        question.getOptionalFeedback());
                    audioController.stopReading();

                } else {
                    var feedbackPrefix = "Falsch beantwortet! Folgende Antwortnummern wären richtig gewesen: "
                        + formatAnswerNumbers(question.getCorrectAnswers());
                    audioController.readFeedbackText(feedbackPrefix + " " + BREAK + BREAK + question.getOptionalFeedback());

                    alertController.showWrongAnswerAlert("Antworten nicht richtig.", feedbackPrefix,
                        question.getOptionalFeedback());
                    audioController.stopReading();
                }
            }
            // send checked answers to service (in order to use it for statistics and learning algorithm)
            try {
                Question mockQuestion = new Question();
                mockQuestion.setId(question.getId());
                mockQuestion.setCorrectAnswers(checkedAnswers);
                LOG.info("Trying to send {} answers on question \"{}\"",
                    mockQuestion.getCorrectAnswers(), mockQuestion.getId());
                lerntiaService.recordCheckedAnswers(mockQuestion, answersCorrect);
            } catch (ServiceException e) {
                LOG.error("Could not check whether the answer was correct");
                alertController.showBigAlert(Alert.AlertType.ERROR, "Überprüfung fehlgeschlagen",
                    "Das Resultat konnte nicht zur Serviceschicht geschickt werden", e.getLocalizedMessage());
            }
            getAndShowNextQuestion();
        } catch (NullPointerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Keine Frage vorhanden", "Fehler",
                "Überprüfen ist nicht möglich da keine Frage angezeigt wurde.");
        }
    }

    private String formatAnswerNumbers(String answers) {
        return answers.replaceAll("(.)", "$1, ").substring(0, answers.length() * 3 - 2);
    }

    public void getAndShowTheFirstQuestion() throws ControllerException {
        try {
            question = lerntiaService.loadQuestionnaireAndGetFirstQuestion();
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
            // save checked answers
            saveAnswerState();

            // get next questions
            question = lerntiaService.getNextQuestionFromList();
            showQuestionAndAnswers();
        } catch (ServiceException e1) {
            LOG.warn("No next question to be displayed.");
            // todo add statistics after that is implemented

            alertController.showBigAlert(Alert.AlertType.INFORMATION, "Keine weiteren Fragen",
                "Du bist am Ende angelangt.", "Die erste Frage wird wieder angezeigt.");

            try {
                question = lerntiaService.getFirstQuestion();
                showQuestionAndAnswers();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void getAndShowPreviousQuestion() {
        try {
            // save checked answers
            saveAnswerState();

            // get next questions
            question = lerntiaService.getPreviousQuestionFromList();
            showQuestionAndAnswers();
        } catch (ServiceException e1) {
            LOG.warn("No previous question to be displayed.");
            // todo add statistics after that is implemented

            alertController.showBigAlert(Alert.AlertType.ERROR, "Keine früheren Fragen",
                "Du bist am Anfang.", "");

            // TODO - code can probably be deleted.
            /*
            try {
                getAndShowTheFirstQuestion();
            } catch (ControllerException e) {
                e.printStackTrace();
            }
            */
        }
    }

    private void showQuestionAndAnswers() {
        if (question == null) {
            LOG.error("ShowQuestionAndAnswers method was called, although the controller did not get a valid Question.");
            alertController.showBigAlert(Alert.AlertType.ERROR, "Keine Fragen verfügbar", "", "");
            return;
        }

        qLabelController.setQuestionText(question.getQuestionText());
        audioController.setQuestion(qLabelController.getQuestionText());
        resetAnswerController();
        setAnswerText(answer1Controller, question.getAnswer1());
        setAnswerText(answer2Controller, question.getAnswer2());
        setAnswerText(answer3Controller, question.getAnswer3());
        setAnswerText(answer4Controller, question.getAnswer4());
        setAnswerText(answer5Controller, question.getAnswer5());

        var checkedAnswers = question.getCheckedAnswers();

        try {
            answer1Controller.setSelected(checkedAnswers.contains("1"));
            answer2Controller.setSelected(checkedAnswers.contains("2"));
            answer3Controller.setSelected(checkedAnswers.contains("3"));
            answer4Controller.setSelected(checkedAnswers.contains("4"));
            answer5Controller.setSelected(checkedAnswers.contains("5"));
        } catch (NullPointerException e) {
            // nothing has been selected if the question is shown for the first time.
            // this can be ignored.
        }

        audioController.setAnswer1(answer1Controller.getAnswerText());
        audioController.setAnswer2(answer2Controller.getAnswerText());
        audioController.setAnswer3(answer3Controller.getAnswerText());
        audioController.setAnswer4(answer4Controller.getAnswerText());
        audioController.setAnswer5(answer5Controller.getAnswerText());

        // show image in the main window or hide the zoom button if there is no image to be shown
        if (question.getPicture() == null || question.getPicture().trim().isEmpty()) {
            mainImage.setVisible(false);
            zoomedImageController.setImageFile(null);
            LOG.debug("No image to be displayed for this question");
        } else {
            try {
                LearningQuestionnaire selectedLearningQuestionnaire = null;

                try {
                    selectedLearningQuestionnaire = learningQuestionnaireService.getSelected();
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                if (selectedLearningQuestionnaire != null) {
                    String imagePath =
                        System.getProperty("user.dir") + File.separator + "img" + File.separator +
                            selectedLearningQuestionnaire.getName() + File.separator +
                            question.getPicture();

                    LOG.debug("Image path: " + imagePath);
                    File imageFile = new File(imagePath);
                    zoomedImageController.setImageFile(imageFile);
                    Image image = new Image(imageFile.toURI().toURL().toExternalForm());
                    mainImage.setImage(image);
                    mainImage.setVisible(true);
                    LOG.info("Image for this question is displayed: '{}'", question.getPicture());
                }
            } catch (MalformedURLException e) {
                LOG.debug("Exception while trying to display image " + e.getMessage());
            }
        }
    }

    private void resetAnswerController() {
        answer1Controller.setAnswerText("");
        answer2Controller.setAnswerText("");
        answer3Controller.setAnswerText("");
        answer4Controller.setAnswerText("");
        answer5Controller.setAnswerText("");
    }

    // this method should be called if the DB does not contain any questions that could be displayed
    private void showNoQuestionsAvailable() {
        qLabelController.setQuestionText("Keine Fragen gefunden. Sind die Fragebögen schon in der Datenbank importiert?");
        audioController.setQuestion("Keine Fragen gefunden. Sind die Fragebögen schon in der Datenbank importiert?");

        setAnswerText(answer1Controller, null);
        setAnswerText(answer2Controller, null);
        setAnswerText(answer3Controller, null);
        setAnswerText(answer4Controller, null);
        setAnswerText(answer5Controller, null);

        buttonBar.getButtons().remove(previousQuestionButton);
        buttonBar.getButtons().remove(nextQuestionButton);
        buttonBar.getButtons().remove(checkAnswerButton);
        buttonBar.getButtons().remove(algorithmButton);
    }

    private void setAnswerText(AnswerController answerController, String answerText) {
        answerController.setSelected(false);
        // if answer is not provided the whole box will be invisible
        if (answerText == null || answerText.trim().isEmpty()) {
            answerController.setVisible(false);
            return;
        }
        answerController.setVisible(true);
        answerController.setAnswerText(answerText);
    }

    public void stopAudio() {
        LOG.debug("Stop Audio");
        audioButtonController.stopReading();
    }

    public void switchToExamMode() throws ServiceException {
        buttonBar.getButtons().remove(checkAnswerButton);

        buttonBar.getButtons().add(handInButton);
        buttonBar.getButtons().remove(algorithmButton);

        lerntiaService.stopAlgorithm();
    }

    public void handIn(ActionEvent actionEvent) {

        // the state of the current question has to be saved here as well.
        saveAnswerState();

        boolean handInConfirmation = alertController.showBigConfirmationAlert("Prüfung abgeben",
            "Soll die Prüfung jetzt abgegeben werden?", "Diese Aktion kann nicht rückgängig gemacht werden.");

        if (handInConfirmation) {
            evaluateExam();
        }
    }

    public void evaluateExam() {

        List<Question> questionList = null;
        try {
            questionList = lerntiaService.getQuestions();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Die Prüfung kann nicht verarbeitet werden",
                "Error", "Die Prüfung kann nicht verarbeitet werden");
            return;
        }

        // TODO - ask the user where the report should be saved

        try {
            iExamResultsWriterService.writeExamResults(questionList, "");
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Datei konnte nicht gespeichert werden",
                "Error", e.getMessage());
        }
    }

    public boolean isExamMode() {
        return examMode;
    }

    public void setExamMode(boolean examMode) {
        this.examMode = examMode;
    }

    private String getCheckedAnswers() {
        String checkedAnswers = "";
        if (answer1Controller.isSelected()) {
            checkedAnswers += "1";
        }
        if (answer2Controller.isSelected()) {
            checkedAnswers += "2";
        }
        if (answer3Controller.isSelected()) {
            checkedAnswers += "3";
        }
        if (answer4Controller.isSelected()) {
            checkedAnswers += "4";
        }
        if (answer5Controller.isSelected()) {
            checkedAnswers += "5";
        }
        return checkedAnswers;
    }

    private void saveAnswerState() {
        String checkedAnswers = getCheckedAnswers();
        question.setCheckedAnswers(checkedAnswers);
    }

    private String getMethod(String str) {
        switch (str) {
            case "1":
                return question.getAnswer1() + "\n";
            case "2":
                return question.getAnswer2() + "\n";
            case "3":
                return question.getAnswer3() + "\n";
            case "4":
                return question.getAnswer4() + "\n";
            case "5":
                return question.getAnswer5() + "\n";
            default:
                return "";
        }
    }

    public void stopAlgorithm() throws ServiceException {
        lerntiaService.stopAlgorithm();
    }
}
