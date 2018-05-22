package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import javafx.event.ActionEvent;
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
    private final LerntiaMainController lerntiaMainController;
    private final IQuestionService questionDAO;
    private final WindowController windowController;
    private final AlertController alertController;
    @FXML
    public TextField tf_question;
    @FXML
    public TextField tf_answer2;
    @FXML
    public TextField tf_answer3;
    @FXML
    public TextField tf_answer4;
    @FXML
    public TextField tf_answer5;
    @FXML
    public TextField tf_answer1;
    @FXML
    public ImageView iv_image;
    @FXML
    public TextField tf_correctAnswer;
    @FXML
    public TextField tf_optionalFeedback;
    private Question selectedQuestion;
    private Stage stage;
    private LearningQuestionnaire learningQuestionnaire;
    @FXML
    private Label noImageLabel;
    private String imageName;

    @Autowired
    public EditQuestionsController(LerntiaMainController lerntiaMainController, IQuestionService questionDAO,
                                   WindowController windowController, AlertController alertController) {
        this.lerntiaMainController = lerntiaMainController;
        this.questionDAO = questionDAO;
        this.windowController = windowController;
        this.alertController = alertController;
    }


    public void initialize() {
        tf_question.setText("Frage");
        tf_answer1.setText("Antwort 1");
        tf_answer2.setText("Antwort 2");
        tf_answer3.setText("Antwort 3");
        tf_answer4.setText("Antwort 4");
        tf_answer5.setText("Antwort 5");
        tf_correctAnswer.setText("Correct Answer");
        tf_optionalFeedback.setText("Optional Feedback");
        PATH = System.getProperty("user.dir") + File.separator + "img" + File.separator +
            learningQuestionnaire.getName() + File.separator;
    }

    /**
     * Opens the first Window in the SelectQuestionAdministrate operation.
     * Opens a window in which the user can See all the Questions .
     */
    public void showEditQuestionsControllerWindow(Question selectedQuestion) {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/editQuestion.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Frage bearbeiten", fxmlLoader);
        this.selectedQuestion = selectedQuestion;
        tf_question.setText(selectedQuestion.getQuestionText());
        tf_answer1.setText(selectedQuestion.getAnswer1());
        tf_answer2.setText(selectedQuestion.getAnswer2());
        tf_answer3.setText(selectedQuestion.getAnswer3());
        tf_answer4.setText(selectedQuestion.getAnswer4());
        tf_answer5.setText(selectedQuestion.getAnswer5());
        tf_correctAnswer.setText(selectedQuestion.getCorrectAnswers());
        tf_optionalFeedback.setText(selectedQuestion.getOptionalFeedback());
        if (selectedQuestion.getPicture() != null && selectedQuestion.getPicture().trim().length() > 0) {
            noImageLabel.setVisible(false);
            loadImage(PATH + selectedQuestion.getPicture(), iv_image);
        } else {
            noImageLabel.setVisible(true);
        }
    }

    public void setQuestionnaire(LearningQuestionnaire learningQuestionnaire) {
        this.learningQuestionnaire = learningQuestionnaire;
    }

    public LearningQuestionnaire getQuestionnaire() {
        return this.learningQuestionnaire;
    }

    @FXML
    public void editButton(ActionEvent actionEvent) {
        LOG.info("Edit Button Clicked");
        Question newData = new Question();
        newData.setQuestionText(tf_question.getText());
        newData.setAnswer1(tf_answer1.getText());
        newData.setAnswer2(tf_answer2.getText());
        newData.setAnswer3(tf_answer3.getText());
        newData.setAnswer4(tf_answer4.getText());
        newData.setAnswer5(tf_answer5.getText());
        newData.setCorrectAnswers(tf_correctAnswer.getText());
        newData.setOptionalFeedback(tf_optionalFeedback.getText());
        newData.setId(selectedQuestion.getId());
        newData.setPicture(imageName);
        try {
            questionDAO.update(newData);
            LOG.info("Editing Completed - Refreshing learning view");
            lerntiaMainController.getAndShowTheFirstQuestion();

        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.WARNING, "Bearbeitungsfehler", "Die Bearbeitung ist fehlgeschlagen!", null);
        } catch (ControllerException e) {
            LOG.debug("Bearbeitung fehlgeschlagen: " + e.getMessage());
            alertController.showStandardAlert(Alert.AlertType.WARNING, "Bearbeitungsfehler", "Die Bearbeitung ist fehlgeschlagen!", null);
        }
        this.stage.close();
        alertController.showStandardAlert(Alert.AlertType.INFORMATION, "Erfolgreich bearbeitet", "Die Frage wurde erfolgreich bearbeitet.", null);
    }

    @FXML
    public void imageButton() {
        var fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(PATH));
        var extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        var extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        var file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageName = file.getName();
            loadImage(PATH + imageName, iv_image);
            LOG.debug("Selected image: " + imageName);
        } else {
            LOG.debug("Canceled image selection.");
        }
    }


    private void loadImage(String PATH, ImageView view) {
        var file = new File(PATH);
        try {
            var img = new Image(file.toURI().toURL().toExternalForm());
            view.setImage(img);
            centerImage();
            noImageLabel.setVisible(false);
            LOG.trace("Successfully loaded the image.");
        } catch (MalformedURLException e) {
            LOG.error("Failed to load the image with path: " + PATH);

        }
    }


    private void centerImage() {
        var img = iv_image.getImage();
        if (img != null) {
            var ratioX = iv_image.getFitWidth() / img.getWidth();
            var ratioY = iv_image.getFitHeight() / img.getHeight();
            double min;
            if (ratioX >= ratioY) {
                min = ratioY;
            } else {
                min = ratioX;
            }
            var width = img.getWidth() * min;
            var height = img.getHeight() * min;
            iv_image.setX((iv_image.getFitWidth() - width) / 2);
            iv_image.setY((iv_image.getFitHeight() - height) / 2);
        }
    }

}