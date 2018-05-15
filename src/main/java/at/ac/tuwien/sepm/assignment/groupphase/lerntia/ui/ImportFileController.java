package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImportFileController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final SimpleCourseService cservice;
    private final SimpleQuestionnaireImportService qservice;
    private File file;
    private File directory;
    private List<Course> coursedata = new ArrayList<>();
    private ObservableList<String> choices = FXCollections.observableArrayList();

    private ObservableList<Course> courses;

    @FXML
    private Text t_filename;
    @FXML
    private Text t_directoryname;
    @FXML
    private TextField tf_questionnaire;
    @FXML
    private ChoiceBox<String> cb_course;

    @Autowired
    public ImportFileController(SimpleCourseService simpleCourseService, SimpleQuestionnaireImportService simpleQuestionnaireImportService) throws PersistenceException {
        cservice = simpleCourseService;
        qservice = simpleQuestionnaireImportService;
    }

    @FXML
    private void initialize() throws ServiceException {
        coursedata = cservice.readAll();
        courses = FXCollections.observableArrayList(coursedata);

        choices.removeAll(choices);
        for (int i = 0; i < courses.size(); i++) {
            choices.add(courses.get(i).getName());
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
        if (file != null) {
            t_filename.setText(file.getName());
        }
    }

    @FXML
    public void selectDirectory(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("[Lerntia] Ordner");
        Stage stage = new Stage();
        directory = directoryChooser.showDialog(stage);
        if (directory != null) {
            t_directoryname.setText(directory.getName());
        }
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {
        if (directory != null) {
            try {
                qservice.importPictures(directory, tf_questionnaire.getText());
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
        if (file != null) {
            try {
                String name = tf_questionnaire.getText();

                if (!name.equals("")) {

                    String course = cb_course.getSelectionModel().getSelectedItem();

                    int cb_courseIndex = cb_course.getSelectionModel().getSelectedIndex();
                    Course selectedCourse = courses.get(cb_courseIndex);

                    qservice.importQuestionnaire(file, selectedCourse, name);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("[Lerntia] Import erfolgreich");
                    alert.setHeaderText("Erfolgreich");
                    alert.setContentText("Alle Fragen wurden erfolgreich importiert");
                    alert.setResizable(true);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("[Lerntia] Fehlerhafter Name");
                    alert.setHeaderText("Warnung");
                    alert.setContentText("Bitte gib einen gültigen Namen an!");
                    alert.setResizable(true);
                    alert.showAndWait();
                }
            } catch (ServiceException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("[Lerntia] Import fehlgeschlagen");
                alert.setHeaderText("Fehler");
                alert.setContentText(e.getMessage());
                alert.setResizable(true);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("[Lerntia] Kein File ausgewählt");
            alert.setHeaderText("Achtung");
            alert.setContentText("Bitte wähle zuerst eine csv-Datei aus!");
            alert.setResizable(true);
            alert.showAndWait();
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    void showImportWindow() {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/importFile.fxml"));
        var stage = new Stage();
        try {
            fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("[Lerntia] Import Infos");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            LOG.debug("Successfully opened a window for importing questions.");
        } catch (IOException e) {
            LOG.error("Failed to open a window for importing questions. " + e.getMessage());
        }
    }
}
