package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleCourseService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionnaireImportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
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
    private final AlertController alertController;

    private final WindowController windowController;
    private File file;
    private File directory;
    private List<Course> coursedata = new ArrayList<>();
    private ObservableList<String> choices = FXCollections.observableArrayList();
    private static final String CSVPATH = System.getProperty("user.dir") + File.separator + "csv" + File.separator;
    private static final String IMGPATH = System.getProperty("user.dir") + File.separator + "img_original" + File.separator;
    private ObservableList<Course> courses;

    @FXML
    private Text t_filename;
    @FXML

    private Text t_directoryname;
    @FXML
    private TextField tf_questionnaire;
    @FXML
    private ComboBox<String> cb_course;
    @FXML
    private CheckBox questionnaireIsExam;

    @Autowired
    public ImportFileController(
        SimpleCourseService simpleCourseService,
        SimpleQuestionnaireImportService simpleQuestionnaireImportService,
        WindowController windowController,
        AlertController alertController
    ) {
        cservice = simpleCourseService;
        qservice = simpleQuestionnaireImportService;
        this.windowController = windowController;
        this.alertController = alertController;
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
        fileChooser.setInitialDirectory(new File(CSVPATH));
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
        directoryChooser.setTitle("[Lerntia] Bildordner");
        directoryChooser.setInitialDirectory(new File(IMGPATH));
        Stage stage = new Stage();
        directory = directoryChooser.showDialog(stage);
        if (directory != null) {
            t_directoryname.setText(directory.getName());
        }
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {

        String name = tf_questionnaire.getText().trim();

        if (name.equals("")) {
            alertController.showStandardAlert(Alert.AlertType.INFORMATION, "Fehlerhafter Name", "Warnung", "Bitte gib einen gültigen Namen an!");
            return;
        }

        if (directory != null) {
            try {
                qservice.importPictures(directory, name);
            } catch (ServiceException e) {
                // TODO - e.getMessage()
                alertController.showStandardAlert(Alert.AlertType.ERROR, "Import fehlgeschlagen", "Fehler", e.getMessage());
                return;
            }
        }
        if (file != null) {
            try {
                String course = cb_course.getSelectionModel().getSelectedItem();

                int cb_courseIndex = cb_course.getSelectionModel().getSelectedIndex();
                Course selectedCourse = courses.get(cb_courseIndex);

                qservice.importQuestionnaire(file, selectedCourse, name, questionnaireIsExam.isSelected());
                alertController.showStandardAlert(Alert.AlertType.INFORMATION, "Import erfolgreich", "Erfolgreich", "Alle Fragen wurden erfolgreich importiert");
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            } catch (ServiceException e) {
                // TODO - e.getMessage()
                qservice.deletePictures(new File(System.getProperty("user.dir") + File.separator + "img" + File.separator + name));
                alertController.showStandardAlert(Alert.AlertType.ERROR, "Import fehlgeschlagen", "Fehler", e.getMessage());
                return;
            }
        } else {
            qservice.deletePictures(new File(System.getProperty("user.dir") + File.separator + "img" + File.separator + name));
            alertController.showStandardAlert(Alert.AlertType.WARNING, "Kein File ausgewählt", "Achtung", "Bitte wähle zuerst eine csv-Datei aus!");
            return;
        }
    }

    void showImportWindow() {

        // make sure there is at least one course before opening the window.

        try {
            coursedata = cservice.readAll();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Import Fenster kann nicht angezeigt werden", "Fehler", e.getMessage());
            return;
        }

        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/importFile.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowController.openNewWindow("Fragebogen importieren", fxmlLoader);
    }
}
