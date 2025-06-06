package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.dto.Question;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionService;
import at.ac.tuwien.lerntia.exception.ServiceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;

@Controller
public class EditQuestionsController {


    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static String PATH;
    private final IQuestionService questionService;
    private final WindowController windowController;
    private final AlertController alertController;
    private final LerntiaMainController lerntiaMainController;
    @FXML
    private TextField tf_question;
    @FXML
    private TextField tf_answer2;
    @FXML
    private TextField tf_answer3;
    @FXML
    private TextField tf_answer4;
    @FXML
    private TextField tf_answer5;
    @FXML
    private TextField tf_answer1;
    @FXML
    private ImageView iv_image;
    @FXML
    private TextField tf_correctAnswer;
    @FXML
    private TextField tf_optionalFeedback;
    private Question selectedQuestion;
    private Stage stage;
    private LearningQuestionnaire learningQuestionnaire;
    private SelectQuestionAdministrateController selectQuestionAdministrateController;
    @FXML
    private Label noImageLabel;
    private String imageName;
    @FXML
    private Label imgNameLabel;

    @Autowired
    public EditQuestionsController(IQuestionService questionService,
                                   WindowController windowController,
                                   AlertController alertController,
                                   LerntiaMainController lerntiaMainController) {
        this.questionService = questionService;
        this.windowController = windowController;
        this.alertController = alertController;
        this.lerntiaMainController = lerntiaMainController;
    }


    public void initialize() {
        LOG.debug("Initialize EditQuestionsController");
        tf_question.setText("Frage");
        tf_answer1.setText("Antwort 1");
        tf_answer2.setText("Antwort 2");
        PATH = System.getProperty("user.dir") + File.separator + "img" + File.separator +
            learningQuestionnaire.getName() + File.separator;
    }

    /**
     * Opens the first Window in the SelectQuestionAdministrate operation.
     * Opens a window in which the user can See all the Questions .
     */
    public void showEditQuestionsControllerWindow(Question selectedQuestion, SelectQuestionAdministrateController selectQuestionAdministrateController) {
        this.selectQuestionAdministrateController = selectQuestionAdministrateController;
        LOG.info("Open Edit Questions Controller Window.");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/editQuestion.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Frage bearbeiten", fxmlLoader);
        this.selectedQuestion = selectedQuestion;
        tf_question.setText(selectedQuestion.getQuestionText());
        tf_answer1.setText(selectedQuestion.getAnswer1());
        tf_answer2.setText(selectedQuestion.getAnswer2());
        tf_answer3.setText(selectedQuestion.getAnswer3());
        tf_answer4.setText(selectedQuestion.getAnswer4());
        tf_answer5.setText(selectedQuestion.getAnswer5());
        imgNameLabel.setText(selectedQuestion.getPicture());
        tf_correctAnswer.setText(selectedQuestion.getCorrectAnswers());
        tf_optionalFeedback.setText(selectedQuestion.getOptionalFeedback());

        if (selectedQuestion.getPicture() != null && selectedQuestion.getPicture().trim().length() > 0) {
            noImageLabel.setVisible(false);
            imageName = selectedQuestion.getPicture();
            loadImage(PATH + selectedQuestion.getPicture(), iv_image);
        } else {
            noImageLabel.setVisible(true);
        }
    }

    public LearningQuestionnaire getQuestionnaire() {
        return this.learningQuestionnaire;
    }

    public void setQuestionnaire(LearningQuestionnaire learningQuestionnaire) {
        this.learningQuestionnaire = learningQuestionnaire;
    }

    @FXML
    public void editButton() {
        LOG.info("Edit Button Clicked");
        if (notEmpty(tf_question.getText()) && notEmpty(tf_answer1.getText()) && notEmpty(tf_answer2.getText())
            && notEmpty(tf_correctAnswer.getText())) {
            Question newData = new Question();
            newData.setQuestionText(tf_question.getText().trim());
            newData.setAnswer1(tf_answer1.getText().trim());
            newData.setAnswer2(tf_answer2.getText().trim());
            newData.setAnswer3(tf_answer3.getText().trim());
            newData.setAnswer4(tf_answer4.getText().trim());
            newData.setAnswer5(tf_answer5.getText().trim());
            newData.setCorrectAnswers(tf_correctAnswer.getText().trim());
            newData.setOptionalFeedback(tf_optionalFeedback.getText().trim());
            newData.setId(selectedQuestion.getId());
            newData.setPicture(imageName);
            LOG.debug("image name: " + imageName);

            try {
                questionService.update(newData);
                LOG.info("Editing Completed - Refreshing learning view");
                alertController.showStandardAlert(Alert.AlertType.INFORMATION, "Erfolgreich bearbeitet",
                    "Die Frage wurde erfolgreich bearbeitet.", null);
                this.stage.close();
                lerntiaMainController.getAndShowTheFirstQuestion();

                //Show the Last Scene
                selectQuestionAdministrateController.showSelectQuestionAdministrateWindow(selectQuestionAdministrateController.getAdministrateMode());
                selectQuestionAdministrateController.refresh();
            } catch (ServiceException e) {
                alertController.showStandardAlert(Alert.AlertType.ERROR, "Bearbeitung fehlgeschlagen",
                    "Die Bearbeitung ist fehlgeschlagen!", e.getCustomMessage());
            }
        } else {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Bearbeitung fehlgeschlagen",
                "Mindestens eines der Pflichtfelder ist leer.", null);
        }
    }

    private boolean notEmpty(String text) {
        return (text != null && text.trim().length() > 0);
    }

    @FXML
    public void imageButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("[Lerntia] Bild auswählen");
        fileChooser.setInitialDirectory(new File(PATH));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG und JPG Files (*.png, *.jpg)", "*.PNG", "*.JPG");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            imageName = file.getName();
            loadImage(PATH + imageName, iv_image);
            LOG.debug("Selected image: " + imageName);
            imgNameLabel.setText(imageName);
            noImageLabel.setVisible(false);
        } else {
            LOG.debug("Canceled image selection.");
        }
    }

    private void loadImage(String PATH, ImageView view) {
        File file = new File(PATH);
        try {
            Image img = new Image(file.toURI().toURL().toExternalForm());
            view.setImage(img);
            centerImage();
            LOG.trace("Successfully loaded the image.");
        } catch (MalformedURLException e) {
            LOG.error("Failed to load the image with path: " + PATH);
            noImageLabel.setVisible(true);
        }
    }

    private void centerImage() {
        Image img = iv_image.getImage();
        if (img != null) {
            double ratioX = iv_image.getFitWidth() / img.getWidth();
            double ratioY = iv_image.getFitHeight() / img.getHeight();
            double min;
            if (ratioX >= ratioY) {
                min = ratioY;
            } else {
                min = ratioX;
            }
            double width = img.getWidth() * min;
            double height = img.getHeight() * min;
            iv_image.setX((iv_image.getFitWidth() - width) / 2);
            iv_image.setY((iv_image.getFitHeight() - height) / 2);
        }
    }

}