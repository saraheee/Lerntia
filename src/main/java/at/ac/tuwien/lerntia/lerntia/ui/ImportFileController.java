package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.lerntia.dto.Course;
import at.ac.tuwien.lerntia.lerntia.dto.ImportQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.service.ICourseService;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionnaireImportService;
import at.ac.tuwien.lerntia.exception.ServiceException;
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
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImportFileController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String CSVPATH = System.getProperty("user.dir") + File.separator + "csv" + File.separator;
    private static final String IMGPATH = System.getProperty("user.dir") + File.separator + "img_original" + File.separator;
    private final ICourseService cService;
    private final IQuestionnaireImportService qService;
    private final AlertController alertController;
    private final WindowController windowController;
    private final ObservableList<String> choices = FXCollections.observableArrayList();
    private File file;
    private File directory;
    private List<Course> courseData = new ArrayList<>();
    private ObservableList<Course> courses;

    @FXML
    private Text t_filename;
    @FXML
    private Text t_directoryName;
    @FXML
    private TextField tf_questionnaire;
    @FXML
    private ComboBox<String> cb_course;
    @FXML
    private CheckBox questionnaireIsExam;

    @Autowired
    public ImportFileController(
        ICourseService simpleCourseService,
        IQuestionnaireImportService simpleQuestionnaireImportService,
        WindowController windowController,
        AlertController alertController
    ) {
        cService = simpleCourseService;
        qService = simpleQuestionnaireImportService;
        this.windowController = windowController;
        this.alertController = alertController;
    }

    @FXML
    private void initialize() throws ServiceException {
        LOG.debug("Initialize ImportFileController");
        courseData.clear();
        courseData = cService.readAll();
        courses = FXCollections.observableArrayList(courseData);
        choices.removeAll();
        choices.clear();
        for (Course course : courses) {
            choices.add(course.getName());
        }
        cb_course.getItems().clear();
        cb_course.setItems(choices);
        cb_course.getSelectionModel().select(0);
    }

    @FXML
    public void selectFile() {
        LOG.info("Open new FileChooser.");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("[Lerntia] Verzeichnis");
        if (!Files.exists(Paths.get(CSVPATH))) {
            if (new File(String.valueOf(CSVPATH)).mkdir()) { //initial directory created
                fileChooser.setInitialDirectory(new File(CSVPATH));
            }
        } else { //initial directory exists
            fileChooser.setInitialDirectory(new File(CSVPATH));
        }
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        Stage stage = new Stage();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if (file.getName().length() > 20) {
                t_filename.setText(file.getName().substring(0, 20) + "..");
            } else {
                t_filename.setText(file.getName());
            }
        }
    }

    @FXML
    public void selectDirectory() {
        LOG.info("Open new DirectoryChooser.");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("[Lerntia] Bildordner");
        if (!Files.exists(Paths.get(IMGPATH))) {
            if (new File(String.valueOf(IMGPATH)).mkdir()) { //initial directory created
                directoryChooser.setInitialDirectory(new File(IMGPATH));
            }
        } else { //initial directory exists
            directoryChooser.setInitialDirectory(new File(IMGPATH));
        }
        Stage stage = new Stage();
        directory = directoryChooser.showDialog(stage);
        if (directory != null) {
            if (directory.getName().length() > 20) {
                t_directoryName.setText(directory.getName().substring(0, 20) + "..");
            } else {
                t_directoryName.setText(directory.getName());
            }
        }
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {
        LOG.info("Import file.");
        String name = tf_questionnaire.getText().trim();

        if (name.trim().equals("")) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fehlerhafter Name", "Fehler", "Bitte einen gültigen Namen angeben!");
            return;
        }

        if (directory != null) {
            try {
                qService.importPictures(directory, name);
            } catch (IOException e) {
                alertController.showStandardAlert(Alert.AlertType.ERROR, "Import fehlgeschlagen", "Fehler", e.getMessage());
                return;
            } catch (ServiceException e) {
                alertController.showStandardAlert(Alert.AlertType.ERROR, "Import fehlgeschlagen", "Import fehlgeschlagen!", "Der Import konnte nicht durchgeführt werden!");
            }
        }
        if (file != null) {
            try {

                int cb_courseIndex = cb_course.getSelectionModel().getSelectedIndex();
                Course selectedCourse = courses.get(cb_courseIndex);

                ImportQuestionnaire iq = new ImportQuestionnaire(file, selectedCourse, name, questionnaireIsExam.isSelected());
                qService.importQuestionnaire(iq);
                alertController.showStandardAlert(Alert.AlertType.INFORMATION, "Import erfolgreich", "Erfolgreich", "Alle Fragen wurden erfolgreich importiert!");
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            } catch (ServiceException e) {
                qService.deletePictures(new File(System.getProperty("user.dir") + File.separator + "img" + File.separator + name));
                alertController.showStandardAlert(Alert.AlertType.ERROR, "Import fehlgeschlagen", "Fehler", e.getMessage());
            }
        } else {
            qService.deletePictures(new File(System.getProperty("user.dir") + File.separator + "img" + File.separator + name));
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Kein File ausgewählt", "Fehler", "Bitte zuerst eine csv-Datei auswählen!");
        }
    }

    void showImportWindow() {

        // make sure there is at least one course before opening the window.

        try {
            courseData = cService.readAll();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Import Fenster kann nicht angezeigt werden", "Fehler", e.getCustomMessage());
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/importFile.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowController.openNewWindow("Fragebogen importieren", fxmlLoader);
    }
}
