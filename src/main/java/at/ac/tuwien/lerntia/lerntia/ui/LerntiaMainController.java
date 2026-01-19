package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.exception.ControllerException;
import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dto.*;
import at.ac.tuwien.lerntia.lerntia.service.IExamResultsWriterService;
import at.ac.tuwien.lerntia.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.lerntia.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.lerntia.lerntia.service.IUserService;
import at.ac.tuwien.lerntia.util.ConfigReader;
import at.ac.tuwien.lerntia.util.JDBCConnectionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static org.springframework.util.Assert.notNull;


@Controller
public class LerntiaMainController implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IMainLerntiaService lerntiaService;
    private final ZoomedImageController zoomedImageController;
    private final AudioController audioController;
    private final AlertController alertController;
    private final ILearningQuestionnaireService learningQuestionnaireService;
    private final IUserService simpleUserService;
    private final IExamResultsWriterService iExamResultsWriterService;
    private final DirectoryChooserController directoryChooserController;
    private final JDBCConnectionManager connectionManager;
    @FXML
    private final LearnAlgorithmController learnAlgorithmController;
    private final ReentrantLock lock = new ReentrantLock();
    private final String BREAK = "....";
    private final String SPACE = "                                                            ";

    // Ratio constants for layout - maintain 3:2:2:2:2:2 ratio
    private static final double QUESTION_LINE_RATIO = 3.0;
    private static final double ANSWER_LINE_RATIO = 2.0;

    // Font sizing constants
    private static final double MIN_FONT_SIZE = 6.0;
    private static final double MAX_FONT_SIZE = 28.0;
    // Realistic line height - actual rendered line height in JavaFX
    private static final double LINE_HEIGHT_FACTOR = 1.7; // JavaFX default line spacing

    // Spacing and padding constants
    private static final double BOX_PADDING = 10.0; // Total vertical padding per box (top + bottom)
    private static final double TOP_PADDING = 30.0;
    private static final double MIN_SPACING = 1.0;
    private static final double PREFERRED_SPACING = 8.0;
    private static final double MIN_BUTTON_BAR_HEIGHT = 50.0;
    private static final double PREFERRED_BUTTON_BAR_HEIGHT = 105.0;
    public VBox upperButtonSpace2;

    private boolean onlyWrongQuestions = false;
    private boolean showAllQuestionsStatistic = false;
    private ConfigReader configReaderSpeech = null;

    // Track last calculated values to prevent unnecessary updates
    private double lastCalculatedHeight = 0;
    private static final double BOTTOM_PADDING = 27.0;

    @FXML
    private VBox mainWindowLeft;
    @FXML
    private VBox rightPanel;
    @FXML
    private ImageView mainImage;
    @FXML
    private HBox questionBox;
    @FXML
    private HBox answer1Box;
    @FXML
    private HBox answer2Box;
    @FXML
    private HBox answer3Box;
    @FXML
    private HBox answer4Box;
    @FXML
    private HBox answer5Box;
    @FXML
    private HBox buttonBox;
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
    private ButtonBar buttonBar;
    @FXML
    private Button checkAnswerButton;
    @FXML
    private Button handInButton;
    @FXML
    private HBox upperButtonBox;
    @FXML
    private VBox upperButtonSpace;
    @FXML
    private Button algorithmButton;
    @FXML
    private Button audioButton;

    // question to be displayed and to be used for checking whether the selected answers were correct
    private Question question;
    private boolean examMode;
    private boolean questionColored = false;
    private String examName;
    private String examStart;

    @Autowired
    public LerntiaMainController(
        IMainLerntiaService lerntiaService,
        AudioController audioController,
        AlertController alertController,
        ILearningQuestionnaireService learningQuestionnaireService,
        ZoomedImageController zoomedImageController,
        IExamResultsWriterService iExamResultsWriterService,
        LearnAlgorithmController learnAlgorithmController,
        DirectoryChooserController directoryChooserController,
        IUserService simpleUserService,
        JDBCConnectionManager connectionManager) {

        notNull(lerntiaService, "'lerntiaService' should not be null");
        notNull(audioController, "'audioController' should not be null");
        notNull(alertController, "'alertController' should not be null");
        notNull(learnAlgorithmController, "learnAlgorithmController should not be null");
        notNull(zoomedImageController, "'zoomedImageController' should not be null");
        this.lerntiaService = lerntiaService;
        this.audioController = audioController;
        this.alertController = alertController;
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.zoomedImageController = zoomedImageController;
        this.iExamResultsWriterService = iExamResultsWriterService;
        this.learnAlgorithmController = learnAlgorithmController;
        this.directoryChooserController = directoryChooserController;
        this.simpleUserService = simpleUserService;
        this.connectionManager = connectionManager;
    }

    @FXML
    private void initialize() {
        mainImage.fitWidthProperty().bind(mainWindowLeft.widthProperty()); // *necessary* in order to bind the image width to the width of the left pane
        buttonBar.getButtons().remove(handInButton);

        // Setup responsive sizing
        setupResponsiveSizing();

        try {
            getAndShowTheFirstQuestionFirstTime();
        } catch (ControllerException e) {
            //showNoQuestionsAvailable();
            LOG.warn("No first answer. Loop stopped.");
        }
    }


    /**
     * Sets up listeners to automatically adjust sizes based on available space
     */
    private void setupResponsiveSizing() {
        if (rightPanel != null) {
            rightPanel.heightProperty().addListener((obs, oldVal, newVal) -> {
                double newHeight = newVal.doubleValue();
                if (Math.abs(newHeight - lastCalculatedHeight) > 3 && newHeight > 0) {
                    Platform.runLater(this::adjustSizes);
                }
            });

            rightPanel.widthProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() > 0) {
                    Platform.runLater(this::adjustSizes);
                }
            });

            Platform.runLater(() -> {
                Platform.runLater(this::adjustSizes);
            });
        }
    }

    /**
     * Calculates and applies appropriate heights and font sizes to fit everything on screen
     */
    private void adjustSizes() {
        if (rightPanel == null || rightPanel.getHeight() <= 0) {
            return;
        }

        double panelHeight = rightPanel.getHeight();
        lastCalculatedHeight = panelHeight;

        // Calculate adaptive values
        double spacing = calculateAdaptiveSpacing(panelHeight);
        double buttonBarHeight = calculateAdaptiveButtonBarHeight(panelHeight);
        double topPadding = calculateAdaptiveTopPadding(panelHeight);
        double bottomPadding = BOTTOM_PADDING; //

        // 6 spacing gaps total
        double totalSpacing = spacing * 6;

        // Calculate available height for content boxes
        double availableHeight = panelHeight - topPadding - bottomPadding - buttonBarHeight - totalSpacing;

        if (availableHeight < 150) {
            LOG.warn("Very limited space: {}px. Using minimum configuration.", availableHeight);
            // Recalculate with minimum values
            spacing = MIN_SPACING;
            buttonBarHeight = MIN_BUTTON_BAR_HEIGHT;
            topPadding = 5;
            totalSpacing = spacing * 6;
            availableHeight = panelHeight - topPadding - bottomPadding - buttonBarHeight - totalSpacing;

            if (availableHeight < 100) {
                availableHeight = 100; // Absolute minimum
            }
        }

        // Total ratio units: 3 (question) + 2*5 (answers) = 13
        double totalRatioUnits = QUESTION_LINE_RATIO + (ANSWER_LINE_RATIO * 5);
        double unitHeight = availableHeight / totalRatioUnits;

        // Calculate heights maintaining 3:2:2:2:2:2 ratio
        double questionHeight = unitHeight * QUESTION_LINE_RATIO;
        double answerHeight = unitHeight * ANSWER_LINE_RATIO;

        // Ensure minimum heights
        double minQuestionHeight = calculateMinHeightForLines(QUESTION_LINE_RATIO, MIN_FONT_SIZE);
        double minAnswerHeight = calculateMinHeightForLines(ANSWER_LINE_RATIO, MIN_FONT_SIZE);

        questionHeight = Math.max(questionHeight, minQuestionHeight);
        answerHeight = Math.max(answerHeight, minAnswerHeight);

        // Calculate font sizes that fit the available space
        double questionFontSize = calculateFontSizeForHeight(questionHeight, QUESTION_LINE_RATIO);
        double answerFontSize = calculateFontSizeForHeight(answerHeight, ANSWER_LINE_RATIO);

        // Clamp font sizes
        questionFontSize = clampFontSize(questionFontSize);
        answerFontSize = clampFontSize(answerFontSize);

        // Apply styles and heights - now with direct label access
        applyBoxStyleAndHeight(questionBox, questionFontSize, questionHeight);
        applyBoxStyleAndHeight(answer1Box, answerFontSize, answerHeight);
        applyBoxStyleAndHeight(answer2Box, answerFontSize, answerHeight);
        applyBoxStyleAndHeight(answer3Box, answerFontSize, answerHeight);
        applyBoxStyleAndHeight(answer4Box, answerFontSize, answerHeight);
        applyBoxStyleAndHeight(answer5Box, answerFontSize, answerHeight);

        if (buttonBox != null) {
            buttonBox.setPrefHeight(buttonBarHeight);
            buttonBox.setMinHeight(buttonBarHeight);
            buttonBox.setMaxHeight(buttonBarHeight);
        }

        if (rightPanel != null) {
            rightPanel.setSpacing(spacing);
        }

        LOG.debug("Layout: Panel={}h, Q={:.1f}px/{:.0f}h, A={:.1f}px/{:.0f}h, Btn={:.0f}h, Sp={:.1f}px",
            Math.round(panelHeight), questionFontSize, questionHeight,
            answerFontSize, answerHeight, buttonBarHeight, spacing);
    }

    private double calculateAdaptiveSpacing(double panelHeight) {
        if (panelHeight >= 800) {
            return PREFERRED_SPACING;
        } else if (panelHeight >= 500) {
            double ratio = (panelHeight - 500) / 300.0;
            return MIN_SPACING + (PREFERRED_SPACING - MIN_SPACING) * ratio;
        } else {
            return MIN_SPACING;
        }
    }

    private double calculateAdaptiveButtonBarHeight(double panelHeight) {
        if (panelHeight >= 800) {
            return PREFERRED_BUTTON_BAR_HEIGHT;
        } else if (panelHeight >= 400) {
            double ratio = (panelHeight - 400) / 400.0;
            return MIN_BUTTON_BAR_HEIGHT + (PREFERRED_BUTTON_BAR_HEIGHT - MIN_BUTTON_BAR_HEIGHT) * ratio;
        } else {
            return MIN_BUTTON_BAR_HEIGHT;
        }
    }

    private double calculateAdaptiveTopPadding(double panelHeight) {
        if (panelHeight >= 800) {
            return TOP_PADDING;
        } else if (panelHeight >= 400) {
            double ratio = (panelHeight - 400) / 400.0;
            return 5 + (TOP_PADDING - 5) * ratio;
        } else {
            return 5;
        }
    }

    /**
     * Calculates the minimum height needed to display the specified number of lines
     * at the given font size
     */
    private double calculateMinHeightForLines(double numberOfLines, double fontSize) {
        double lineHeight = fontSize * LINE_HEIGHT_FACTOR;
        double textHeight = numberOfLines * lineHeight;
        return textHeight + BOX_PADDING;
    }

    /**
     * Calculates font size that will fit the specified number of lines in the given height
     */
    private double calculateFontSizeForHeight(double boxHeight, double numberOfLines) {
        double availableForText = boxHeight - BOX_PADDING;

        if (availableForText <= 0) {
            return MIN_FONT_SIZE;
        }

        double fontSize = availableForText / (numberOfLines * LINE_HEIGHT_FACTOR);
        return fontSize;
    }

    private double clampFontSize(double fontSize) {
        return Math.max(MIN_FONT_SIZE, Math.min(MAX_FONT_SIZE, fontSize));
    }

    /**
     * Applies font size to all labels within a box and sets box height
     */
    private void applyBoxStyleAndHeight(HBox box, double fontSize, double height) {
        if (box == null) {
            return;
        }

        // Apply font size to the box itself
        String boxStyle = String.format("-fx-font-size: %.2fpx;", fontSize);
        box.setStyle(boxStyle);

        // IMPORTANT: Apply font size to ALL children recursively to override any FXML styles
        applyFontSizeToAllChildren(box, fontSize);

        // Set exact height
        box.setPrefHeight(height);
        box.setMinHeight(height);
        box.setMaxHeight(height);
    }

    /**
     * Recursively applies font size to all children nodes
     * This ensures that labels in included FXML files also get the font size
     */
    private void applyFontSizeToAllChildren(javafx.scene.Parent parent, double fontSize) {
        String style = String.format("-fx-font-size: %.2fpx;", fontSize);

        for (Node child : parent.getChildrenUnmodifiable()) {
            // Apply to labels and other text-containing controls
            if (child instanceof Labeled) {
                child.setStyle(style);
            } else if (child instanceof javafx.scene.Parent) {
                // Recursively apply to children
                applyFontSizeToAllChildren((javafx.scene.Parent) child, fontSize);
            }
        }
    }

    public void update(Scene scene) {
        Platform.runLater(() -> scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A) {
                LOG.info("A key was pressed");
                audioButtonController.setSelected();
            }
            if (e.getCode() == KeyCode.Z) {
                LOG.info("Z key was pressed");
                Thread zoomedImageThread = new Thread(this);
                zoomedImageThread.start();
                try {
                    zoomedImageController.onZoomButtonClicked();
                } catch (ControllerException e1) {
                    alertController.showStandardAlert(Alert.AlertType.ERROR, "Zoomen fehlgeschlagen",
                        "Das Bild kann nicht angezeigt werden.",
                        e1.getCustomMessage());
                }
            }
            if (e.getCode() == KeyCode.N) {
                LOG.info("N key was pressed");
                getAndShowNextQuestion();
            }
            if (e.getCode() == KeyCode.P) {
                LOG.info("P key was pressed");
                getAndShowPreviousQuestion();
            }
            if (e.getCode() == KeyCode.C) {
                LOG.info("C key was pressed");
                if (!examMode) {
                    checkIfQuestionWasCorrect();
                } else {
                    handIn();
                }
            }
            if (e.getCode() == KeyCode.G) {
                LOG.info("G key was pressed.");
                learnAlgorithmController.onAlgorithmButtonPressed();
                if (learnAlgorithmController.isSelected()) {
                    LOG.info("Learn algorithm is now running");
                } else {
                    LOG.info("Learn algorithm is not running anymore.");
                }
            }
            if (e.getCode() == KeyCode.NUMPAD1 || e.getCode() == KeyCode.DIGIT1) {
                LOG.info("1 key was pressed");
                handleAnswer(answer1Controller);
            }
            if (e.getCode() == KeyCode.NUMPAD2 || e.getCode() == KeyCode.DIGIT2) {
                LOG.info("2 key was pressed");
                handleAnswer(answer2Controller);
            }
            if (e.getCode() == KeyCode.NUMPAD3 || e.getCode() == KeyCode.DIGIT3) {
                LOG.info("3 key was pressed");
                handleAnswer(answer3Controller);
            }
            if (e.getCode() == KeyCode.NUMPAD4 || e.getCode() == KeyCode.DIGIT4) {
                LOG.info("4 key was pressed");
                handleAnswer(answer4Controller);
            }
            if (e.getCode() == KeyCode.NUMPAD5 || e.getCode() == KeyCode.DIGIT5) {
                LOG.info("5 key was pressed");
                handleAnswer(answer5Controller);
            }

        }));
        mainImage.setOnMouseClicked((MouseEvent e) -> {
            try {
                zoomedImageController.onZoomButtonClicked();
            } catch (ControllerException e1) {
                alertController.showStandardAlert(Alert.AlertType.ERROR, "Zoomen fehlgeschlagen",
                    "Das Bild kann nicht angezeigt werden.",
                    e1.getCustomMessage());
            }
        });
        mainImage.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            mainImage.getStyleClass().clear();
            mainImage.getStyleClass().add("image-enter");
            event.consume();
        });
        mainImage.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            mainImage.getStyleClass().clear();
            mainImage.getStyleClass().add("image-exit");
            event.consume();
        });
    }

    private void handleAnswer(AnswerController answerController) {
        if (answerController.getAnswerText().trim().equals("")) {
            alertController.showBigAlert(Alert.AlertType.ERROR, "Keine Antwort vorhanden",
                "Keine Antwort vorhanden!", "");
            return;
        }
        if (answerController.isDisabled()) { // enable reading but not selecting of answers
            audioController.readSingleAnswer(answerController.getAnswerText());

        } else { //answer is not disabled
            answerController.setSelected(!answerController.isSelected());
            if (answerController.isSelected()) {
                audioController.readSingleAnswer(answerController.getAnswerText());
            } else {
                audioController.stopReading();
            }
        }
    }

    @FXML
    private void checkIfQuestionWasCorrect() {
        audioController.stopReading();
        audioController.deselectAudioButton();

        // gather the info about the checked answers
        try {
            String checkedAnswers = getCheckedAnswers();
            boolean goToNextQuestion = true;
            boolean answersCorrect = checkedAnswers.trim().equals(question.getCorrectAnswers().trim());
            LOG.info(question.toStringGUI());
            LOG.info("Answers given: " + (checkedAnswers.trim().length() > 0 ? checkedAnswers.trim() : "-"));
            LOG.info(answersCorrect ? "Correctly answered.\n" : "Wrongly answered.\n");

            // No correct answers specified, just read the feedback-text
            if (question.getCorrectAnswers().equals("-1")) {
                String feedbackPrefix = "Feedbacktext :" + SPACE;
                if (question.getOptionalFeedback() != null && !question.getOptionalFeedback().trim().equals("")) {
                    audioController.readFeedbackText(question.getOptionalFeedback());
                }
                if (alertController.showUndefinedAnswerAlert("Antwortfeedback!", feedbackPrefix,
                    question.getOptionalFeedback())) {
                    goToNextQuestion = false;
                }
                audioController.stopReading();

            } else if (answersCorrect) {
                if (!questionColored) {
                    try {
                        lerntiaService.recordCheckedAnswers(question, true);
                    } catch (ServiceException e) {
                        alertController.showBigAlert(Alert.AlertType.WARNING, "Speichern fehlgeschlagen",
                            "Antworten nicht gespeichert!", "Die gegebenen Antworten konnten nicht gespeichert werden!");
                        LOG.error("Given answers are correct, but failed to save answers: " + e.getLocalizedMessage());
                    }
                }

                if (question.getCorrectAnswers().length() == 1) { // only one answer is correct
                    String feedbackPrefix = "Korrekt beantwortet! Richtige Antwort: " + checkedAnswers + "       ";
                    audioController.readFeedbackText(feedbackPrefix + " | " + BREAK + BREAK +
                        question.getOptionalFeedback());

                    if (alertController.showCorrectAnswerAlert("Antwort richtig!", feedbackPrefix,
                        question.getOptionalFeedback())) {
                        goToNextQuestion = false;
                    }
                    audioController.stopReading();

                } else {
                    String feedbackPrefix = "Korrekt beantwortet! Richtige Antworten: "
                        + formatAnswerNumbers(question.getCorrectAnswers()) + "     ";
                    audioController.readFeedbackText(feedbackPrefix + " | " + BREAK + BREAK +
                        question.getOptionalFeedback());

                    if (alertController.showCorrectAnswerAlert("Antworten richtig!", feedbackPrefix,
                        question.getOptionalFeedback())) {
                        goToNextQuestion = false;
                    }
                    audioController.stopReading();
                }
            } else {
                if (!questionColored) {
                    try {
                        lerntiaService.recordCheckedAnswers(question, false);
                    } catch (ServiceException e) {
                        alertController.showBigAlert(Alert.AlertType.WARNING, "Speichern fehlgeschlagen",
                            "Antworten nicht gespeichert!", "Die gegebenen Antworten konnten nicht gespeichert werden!");
                        LOG.error("Given answers are wrong, and failed to save answers: " + e.getLocalizedMessage());
                    }
                }
                if (question.getCorrectAnswers().length() == 1) { // only one answer is correct
                    String feedbackPrefix = "Falsch beantwortet! Die richtige Antwort\nlautet: "
                        + formatAnswerNumbers(question.getCorrectAnswers()) + "    ";
                    audioController.readFeedbackText(feedbackPrefix + " | " + BREAK + BREAK +
                        question.getOptionalFeedback());

                    if (alertController.showWrongAnswerAlert("Antwort nicht richtig.", feedbackPrefix,
                        question.getOptionalFeedback())) {
                        goToNextQuestion = false;
                    }
                    audioController.stopReading();

                } else {
                    String feedbackPrefix = "Falsch beantwortet! Die richtigen Antworten\nlauten: "
                        + formatAnswerNumbers(question.getCorrectAnswers()) + " ";
                    audioController.readFeedbackText(feedbackPrefix + " | " + BREAK + BREAK + question.getOptionalFeedback());

                    if (alertController.showWrongAnswerAlert("Antworten nicht richtig.", feedbackPrefix,
                        question.getOptionalFeedback())) {
                        goToNextQuestion = false;
                    }
                    audioController.stopReading();
                }
            }
            if (goToNextQuestion) {
                removeColorsAndEnableAnswers();
                getAndShowNextQuestion();
            } else {
                if (!question.getCorrectAnswers().equals("-1")) {
                    showColorsAndDisableAnswers(question.getCorrectAnswers());
                }
            }
        } catch (NullPointerException e) {
            alertController.showBigAlert(Alert.AlertType.ERROR, "Keine Frage vorhanden", "Fehler",
                "Überprüfen nicht möglich, da keine Frage vorhanden ist.");
            LOG.error("Failed to check answers: " + e.getLocalizedMessage());
        }
    }

    private void showColorsAndDisableAnswers(String correctAnswers) {
        questionColored = true;
        LOG.debug("Answers colored");
        if (correctAnswers.contains(String.valueOf(1))) {
            answer1Controller.markGreen();
        } else {
            answer1Controller.markRed();
        }
        if (correctAnswers.contains(String.valueOf(2))) {
            answer2Controller.markGreen();
        } else {
            answer2Controller.markRed();
        }
        if (correctAnswers.contains(String.valueOf(3))) {
            answer3Controller.markGreen();
        } else {
            answer3Controller.markRed();
        }
        if (correctAnswers.contains(String.valueOf(4))) {
            answer4Controller.markGreen();
        } else {
            answer4Controller.markRed();
        }
        if (correctAnswers.contains(String.valueOf(5))) {
            answer5Controller.markGreen();
        } else {
            answer5Controller.markRed();
        }
        answer1Controller.setDisabled(true);
        answer2Controller.setDisabled(true);
        answer3Controller.setDisabled(true);
        answer4Controller.setDisabled(true);
        answer5Controller.setDisabled(true);

    }

    public void removeColorsAndEnableAnswers() {
        LOG.debug("Answers uncolored");
        answer1Controller.markBlack();
        answer2Controller.markBlack();
        answer3Controller.markBlack();
        answer4Controller.markBlack();
        answer5Controller.markBlack();
        questionColored = false;

        if (!examMode) {
            answer1Controller.setDisabled(false);
            answer2Controller.setDisabled(false);
            answer3Controller.setDisabled(false);
            answer4Controller.setDisabled(false);
            answer5Controller.setDisabled(false);

            answer1Controller.setSelected(false);
            answer2Controller.setSelected(false);
            answer3Controller.setSelected(false);
            answer4Controller.setSelected(false);
            answer5Controller.setSelected(false);
        }
    }

    private String formatAnswerNumbers(String answers) {
        return answers.replaceAll("(.)", "$1, ").substring(0, answers.length() * 3 - 2);
    }

    public void getAndShowTheFirstExamQuestion() throws ControllerException {
        try {
            question = lerntiaService.getFirstExamQuestion();
        } catch (ServiceException e) {
            throw new ControllerException("Es gibt noch keine Prüfungsfragen!");
        }
        showQuestionAndAnswers();
    }

    public void getAndShowTheFirstQuestion() {
        try {
            question = null;
            question = lerntiaService.loadQuestionnaireAndGetFirstQuestion();
            if (question == null) {
                showNoQuestionsAvailable();
            }
        } catch (ServiceException e) {
            showNoQuestionsAvailable();
        }
        showQuestionAndAnswers();
    }

    private void getAndShowTheFirstQuestionFirstTime() throws ControllerException {
        try {
            question = lerntiaService.loadQuestionnaireAndGetFirstQuestion();
        } catch (ServiceException e) {

            showNoQuestionsAvailable();

            throw new ControllerException("Es gibt noch keine Fragen!");
        }
        showQuestionAndAnswers();
    }


    @FXML
    private void getAndShowNextQuestion() {
        audioController.stopReading();
        audioController.deselectAudioButton();
        if (questionColored) {
            removeColorsAndEnableAnswers();
        }

        try {
            // save checked answers
            saveAnswerState();

            // get next questions
            question = lerntiaService.getNextQuestionFromList();
            showQuestionAndAnswers();
        } catch (ServiceException e1) {
            if (examMode) {
                if (lerntiaService.getIgnoredExamAnswers() == 0) {
                    alertController.showBigAlert(Alert.AlertType.WARNING, "Ende der Fragenliste", "Letzte Frage der Prüfung erreicht",
                        "Die letzte Frage wurde erreicht. Es wurden keine Fragen ausgelassen. Die Antworten können jetzt noch überprüft werden.\n\n" +
                            "Nicht vergessen die Prüfung am Ende abzugeben!");
                } else if (lerntiaService.getIgnoredExamAnswers() == 1) {
                    alertController.showBigAlert(Alert.AlertType.WARNING, "Ende der Fragenliste", "Letzte Frage der Prüfung erreicht",
                        "Die letzte Frage wurde erreicht. Es wurde genau eine Frage ausgelassen, zu der jetzt noch zurücknavigiert werden kann.\n\n" +
                            "Nicht vergessen die Prüfung am Ende abzugeben!");
                } else {
                    alertController.showBigAlert(Alert.AlertType.WARNING, "Ende der Fragenliste", "Letzte Frage der Prüfung erreicht",
                        "Die letzte Frage wurde erreicht. Es wurden " + lerntiaService.getIgnoredExamAnswers() + " Fragen ausgelassen, zu denen jetzt noch zurücknavigiert werden kann.\n\n" +
                            "Nicht vergessen die Prüfung am Ende abzugeben!");
                }

            } else {
                LOG.warn("No next question to be displayed.");
                if (!onlyWrongQuestions) {
                    showAllQuestionsStatistic = true;
                    alertController.showBigAlertWithDiagram(Alert.AlertType.CONFIRMATION, "Keine weiteren Fragen",
                        "Die letzte Frage wurde erreicht.\nRichtig: " + lerntiaService.getCorrectAnswers()
                            + "\n" + "Falsch: " + lerntiaService.getWrongAnswers() + "\n"
                            + lerntiaService.getPercent() + "% der gestellten Fragen wurden\n korrekt beantwortet.  \n"
                            + "Übersprungen: " + lerntiaService.getIgnoredAnswers(),
                        "Sollen nur falsch beantwortete Fragen erneut angezeigt werden, oder alle Fragen?\n", createPieChart());
                    lerntiaService.resetCounter();

                } else if (!e1.getMessage().contains("List of wrong questions is empty")) {
                    showAllQuestionsStatistic = false;
                    alertController.showBigAlertWithDiagram(Alert.AlertType.CONFIRMATION, "Keine weiteren Fragen",
                        "Die letzte Frage wurde erreicht.\nRichtig: " + lerntiaService.getCorrectAnswers()
                            + "\n" + "Falsch: " + lerntiaService.getWrongAnswers() + "\n"
                            + lerntiaService.getPercent() + "% der gestellten Fragen wurden\n korrekt beantwortet.  \n"
                            + "Übersprungen: " + lerntiaService.getWrongIgnoredAnswers(),
                        "Sollen nur falsch beantwortete Fragen erneut angezeigt werden, oder alle Fragen?\n", createPieChart());
                    lerntiaService.resetCounter();
                }
                if (e1.getMessage().contains("List of wrong questions is empty")) {
                    showAllQuestionsStatistic = false;
                    alertController.showBigAlertWithDiagram(Alert.AlertType.INFORMATION, "Keine weiteren Fragen",
                        "Die letzte Frage wurde erreicht.\nRichtig: " + lerntiaService.getCorrectAnswers()
                            + "\n" + "Falsch: " + lerntiaService.getWrongAnswers() + "\n"
                            + lerntiaService.getPercent() + "% der gestellten Fragen wurden\n korrekt beantwortet.  \n"
                            + "Übersprungen: " + lerntiaService.getWrongIgnoredAnswers(),
                        "Es gibt keine falsch beantworteten Fragen mehr. Die erste Frage wird wieder angezeigt!\n", createPieChart());
                    lerntiaService.resetCounter();
                    alertController.setOnlyWrongQuestions(false);
                }
                Boolean onlyWrongQuestionsHelp = alertController.isOnlyWrongQuestions();

                onlyWrongQuestions = onlyWrongQuestionsHelp;
                try {
                    lerntiaService.setOnlyWrongQuestions(onlyWrongQuestionsHelp);
                    question = lerntiaService.getFirstQuestion();
                    showQuestionAndAnswers();
                } catch (ServiceException e) {
                    if (e.getCustomMessage().contains("No wrong questions available")) {
                        alertController.showBigAlert(Alert.AlertType.INFORMATION, "Keine Fragen",
                            "Keine falsch beantworteten Fragen vorhanden", "Es gibt keine falsch beantworteten Fragen. "
                                + "Daher werden alle Fragen angezeigt.");
                        lerntiaService.resetCounter();
                        alertController.setOnlyWrongQuestions(false);
                        onlyWrongQuestions = false;
                        question = lerntiaService.restoreQuestionsAndGetFirst();
                        showQuestionAndAnswers();
                    } else if (e.getCustomMessage().contains("List of wrong questions is empty.")) {
                        lerntiaService.resetCounter();
                        alertController.showBigAlert(Alert.AlertType.WARNING, "Keine Fragen mehr", "Keine falsch beantworteten Fragen mehr.",
                            "Es gibt keine falsch beantworteten Fragen mehr. Die erste Frage wird wieder angezeigt.");
                        lerntiaService.resetCounter();
                        alertController.setOnlyWrongQuestions(false);
                        onlyWrongQuestions = false;

                        getAndShowTheFirstQuestion();
                    }
                }
            }
        }
    }

    @FXML
    private void getAndShowPreviousQuestion() {
        audioController.stopReading();
        audioController.deselectAudioButton();
        if (questionColored) {
            removeColorsAndEnableAnswers();
        }

        try {
            // save checked answers
            saveAnswerState();

            // get next questions
            question = lerntiaService.getPreviousQuestionFromList();
            showQuestionAndAnswers();
        } catch (ServiceException e1) {
            LOG.warn("No previous question to be displayed.");

            alertController.showBigAlert(Alert.AlertType.ERROR, "Keine vorherigen Fragen",
                "Das ist die erste Frage.", "Keine vorherigen Fragen vorhanden.");
        }
    }

    private void showQuestionAndAnswers() {
        if (question == null) {
            LOG.error("ShowQuestionAndAnswers method was called, although the controller did not get a valid Question.");
            showNoQuestionsAvailable();
        } else {

            qLabelController.setQuestionText(question.getQuestionText());
            audioController.setQuestion(qLabelController.getQuestionText());
            resetAnswerController();
            setAnswerText(answer1Controller, question.getAnswer1());
            setAnswerText(answer2Controller, question.getAnswer2());
            setAnswerText(answer3Controller, question.getAnswer3());
            setAnswerText(answer4Controller, question.getAnswer4());
            setAnswerText(answer5Controller, question.getAnswer5());

            String checkedAnswers = question.getCheckedAnswers();

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
                        LOG.error("Failed to get selected questionnaire!");
                        alertController.showStandardAlert(Alert.AlertType.ERROR, "Auswahl fehlgeschlagen",
                            "Auswahl eines Fragenbogens fehlgeschlagen", "Zurzeit ist noch kein Lernfragebogen ausgewählt.\n"
                                + "Bitte einen Fragebogen imoprtieren!");
                    }
                    if (selectedLearningQuestionnaire != null) {
                        String imagePath =
                            System.getProperty("user.dir") + File.separator + "img" + File.separator +
                                selectedLearningQuestionnaire.getName() + File.separator + question.getPicture();

                        LOG.debug("Image path: " + imagePath);
                        File imageFile = new File(imagePath);
                        zoomedImageController.setImageFile(imageFile);
                        Image image = new Image(imageFile.toURI().toURL().toExternalForm());
                        mainImage.setImage(image);
                        mainImage.setVisible(true);
                        LOG.info("Image for this question is displayed: '{}'", question.getPicture());
                    }
                } catch (MalformedURLException e) {
                    LOG.debug("Exception while trying to display image");
                }
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
        qLabelController.setQuestionText("Keine Fragen gefunden. Bitte einen Fragebogen importieren und auswählen!\n" +
            "(Menü -> \"Fragen von CSV importieren\"   Menü -> \"Fragebogen auswählen\")");
        audioController.setQuestion("Keine Fragen gefunden. Bitte einen Fragebogen importieren und auswählen!");

        setAnswerText(answer1Controller, "Keine Antwort vorhanden");
        setAnswerText(answer2Controller, "Keine Antwort vorhanden");
        setAnswerText(answer3Controller, "Keine Antwort vorhanden");
        setAnswerText(answer4Controller, "Keine Antwort vorhanden");
        setAnswerText(answer5Controller, "Keine Antwort vorhanden");

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

    public void switchToExamMode() throws ServiceException {
        upperButtonBox.getChildren().remove(algorithmButton);
        buttonBar.getButtons().remove(checkAnswerButton);
        buttonBar.getButtons().add(handInButton);
        lerntiaService.stopAlgorithm();
        learnAlgorithmController.reset();
    }

    public void switchToLearnMode() {
        upperButtonBox.getChildren().remove(audioButton);
        upperButtonBox.getChildren().remove(upperButtonSpace);
        upperButtonBox.getChildren().add(algorithmButton);
        upperButtonBox.getChildren().add(upperButtonSpace);
        upperButtonBox.getChildren().add(audioButton);
        buttonBar.getButtons().add(checkAnswerButton);
        buttonBar.getButtons().remove(handInButton);
    }

    @FXML
    private void handIn() {

        // the state of the current question has to be saved here as well.
        saveAnswerState();

        boolean handInConfirmation = alertController.showBigConfirmationAlert("Prüfung abgeben",
            "Soll die Prüfung jetzt abgegeben werden?", "Diese Aktion kann nicht rückgängig gemacht werden.");

        if (handInConfirmation) {
            evaluateExam();
        }
    }

    private void evaluateExam() {

        List<Question> questionList;
        questionList = lerntiaService.getQuestions();

        String filePath = null;
        try {
            filePath = directoryChooserController.getExamPath();
        } catch (ControllerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Datei konnte nicht gespeichert werden",
                "Fehler", e.getCustomMessage());
        }
        LOG.info("Exam result saved in " + filePath);
        try {
            if (filePath != null) {

                User student = null;
                try {
                    student = simpleUserService.read();
                } catch (ServiceException e) {
                    alertController.showStandardAlert(Alert.AlertType.ERROR, "Ergebnis konnte nicht gespeichert werden!",
                        "Fehler", "Die Daten des Studenten konnten nicht geladen werden!");
                }

                iExamResultsWriterService.writeExamResults(new ExamWriter(questionList, student, this.getExamName(), filePath));
                alertController.showStandardAlert(Alert.AlertType.INFORMATION, "Ergebnis gespeichert!",
                    "Ergebnis gespeichert!", "Das Ergebnis wurde erfolgreich gespeichert!");


                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File(filePath);
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        alertController.showStandardAlert(Alert.AlertType.ERROR, "Ergebnis konnte nicht geöffnet werden!",
                            "Fehler", "Das Ergebnis-PDF konnte nicht geöffnet werden!");
                    }
                }
                iExamResultsWriterService.sendExamResultsPerEmail(filePath);
            }
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Datei konnte nicht gespeichert oder gesendet werden",
                "Fehler", e.getLocalizedMessage());
        }
    }

    public boolean isExamMode() {
        return examMode;
    }

    public void setExamMode(boolean examMode) {
        this.examMode = examMode;
        lerntiaService.setExamMode(examMode);
        onlyWrongQuestions = false;
        alertController.setOnlyWrongQuestions(false);
        lerntiaService.setOnlyWrongQuestions(false);
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
        if (question == null) {
            return;
        }
        question.setCheckedAnswers(checkedAnswers);
    }

    private String getExamName() {
        return this.examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public void prepareExamQuestionnaire(ExamQuestionnaire selectedQuestionnaire) {
        try {
            lerntiaService.getQuestionsFromExamQuestionnaire(selectedQuestionnaire);
            getAndShowTheFirstExamQuestion();
        } catch (ControllerException | ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfung anzeigen fehlgeschlagen",
                "Fehler", "Die ausgewählte Prüfung kann nicht angezeigt werden!");
        }
    }

    private ImageView createPieChart() {
        ObservableList<PieChart.Data> pieChartData;
        if (showAllQuestionsStatistic) {
            pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Richtig", lerntiaService.getCorrectAnswers()),
                new PieChart.Data("Falsch", lerntiaService.getWrongAnswers()),
                new PieChart.Data("Übersprungen", lerntiaService.getIgnoredAnswers()));
        } else {
            pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Richtig", lerntiaService.getCorrectAnswers()),
                new PieChart.Data("Falsch", lerntiaService.getWrongAnswers()),
                new PieChart.Data("Übersprungen", lerntiaService.getWrongIgnoredAnswers()));
        }
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setClockwise(false);
        pieChart.setLegendVisible(false);

        Stage stage = new Stage();
        Scene scene = new Scene(pieChart);
        stage.setScene(scene);
        stage.show();

        scene.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        pieChartData.get(0).getNode().getStyleClass().add("default-color0.chart-pie");
        pieChartData.get(1).getNode().getStyleClass().add("default-color1.chart-pie");
        pieChartData.get(2).getNode().getStyleClass().add("default-color2.chart-pie");

        WritableImage snapShot = scene.snapshot(null);
        try {
            File f = new File(System.getProperty("user.dir") + File.separator + "statistic.png");
            ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", f);
            Image image = new Image(new FileInputStream(System.getProperty("user.dir") + File.separator + "statistic.png"));
            return new ImageView(image);
        } catch (IOException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Statistik anzeigen fehlgeschlagen",
                "Fehler", "Die Statistik kann nicht angezeigt werden!");
            LOG.error("Failed to show statistic: " + e.getLocalizedMessage());
            return null;
        } finally {
            stage.close();
        }
    }

    @Override
    public void run() {
        resetZoomedImageEvents();
        while (!zoomedImageController.getImageClosed()) {
            lock.lock();
            try {
                if (zoomedImageController.isKey1pressed()) {
                    handleAnswer(answer1Controller);
                    zoomedImageController.setKey1pressed(false);
                    return;
                }
                if (zoomedImageController.isKey2pressed()) {
                    handleAnswer(answer2Controller);
                    zoomedImageController.setKey2pressed(false);
                    return;
                }
                if (zoomedImageController.isKey3pressed()) {
                    handleAnswer(answer3Controller);
                    zoomedImageController.setKey3pressed(false);
                    return;
                }
                if (zoomedImageController.isKey4pressed()) {
                    handleAnswer(answer4Controller);
                    zoomedImageController.setKey4pressed(false);
                    return;
                }
                if (zoomedImageController.isKey5pressed()) {
                    handleAnswer(answer5Controller);
                    zoomedImageController.setKey5pressed(false);
                    return;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private void resetZoomedImageEvents() {
        zoomedImageController.setImageClosed(false);
        zoomedImageController.setKey1pressed(false);
        zoomedImageController.setKey2pressed(false);
        zoomedImageController.setKey3pressed(false);
        zoomedImageController.setKey4pressed(false);
        zoomedImageController.setKey5pressed(false);
    }

    public void stopAudio() {
        LOG.info("Stopping audio.");
        audioButtonController.stopReading();
    }

    public void stopAlgorithm() throws ServiceException {
        LOG.info("Stopping algorithm.");
        lerntiaService.stopAlgorithm();
    }

    public void closeConnection() {
        LOG.info("Closing connection.");
        connectionManager.closeConnection();
    }

    void setExamStart(String examStart) {
        this.examStart = examStart;
    }
}
