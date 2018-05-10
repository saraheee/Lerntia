package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleCourseService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleLearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleQuestionnaireImportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImportFileController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final SimpleCourseService cservice;
    private final SimpleQuestionnaireImportService qservice;
    private File file;
    private List<Course> coursedata = new ArrayList<>();
    private ObservableList<String> choices = FXCollections.observableArrayList();

    @FXML
    private Text t_filename;
    @FXML
    private TextField tf_questionnaire;
    @FXML
    private ChoiceBox<String> cb_course;

    public ImportFileController() throws PersistenceException {
        CourseDAO c = new CourseDAO();
        cservice = new SimpleCourseService(c);
        QuestionnaireImportDAO i = new QuestionnaireImportDAO();
        QuestionDAO d = new QuestionDAO();
        SimpleQuestionService q = new SimpleQuestionService(d);
        ILearningQuestionnaireDAO a = new LearningQuestionnaireDAO();
        SimpleLearningQuestionnaireService l = new SimpleLearningQuestionnaireService(a);
        qservice = new SimpleQuestionnaireImportService(i, q, l);
    }

    @Autowired
    public ImportFileController(SimpleCourseService cservice, SimpleQuestionnaireImportService qservice) {
        this.cservice = cservice;
        this.qservice = qservice;
    }

    @FXML
    private void initialize() throws ServiceException {
        coursedata = cservice.readAll();
        ObservableList<Course> courses = FXCollections.observableArrayList(coursedata);
        for (int i = 0; i < courses.size(); i++) {
            choices.add(courses.get(i).getMark());
        }
        cb_course.setItems(choices);
        cb_course.getSelectionModel().select(0);
    }

    @FXML
    public void selectFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("[Lerntia] Verzeichnis");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        Stage stage = new Stage();
        file = fileChooser.showOpenDialog(stage);
        t_filename.setText(file.getName());
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {
        if (file != null) {
            try {
                String name = tf_questionnaire.getText();
                if (!name.equals("")) {
                    qservice.importQuestionnaire(file);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("[Lerntia] Import erfolgreich");
                    alert.setHeaderText("Erfolgreich");
                    alert.setContentText("Alle Fragen wurden erfolgreich importiert");
                    alert.setResizable(true);
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("[Lerntia] Fehlerhafter Name");
                    alert.setHeaderText("Warnung");
                    alert.setContentText("Bitte gib einen gültigen Namen an!");
                    alert.setResizable(true);
                    alert.showAndWait();
                }
            }
            catch (ServiceException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("[Lerntia] Import fehlgeschlagen");
                alert.setHeaderText("Fehler");
                alert.setContentText(e.getMessage());
                alert.setResizable(true);
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("[Lerntia] Kein File ausgewählt");
            alert.setHeaderText("Achtung");
            alert.setContentText("Bitte wähle zuerst eine csv-Datei aus!");
            alert.setResizable(true);
            alert.showAndWait();
        }
    }
}
