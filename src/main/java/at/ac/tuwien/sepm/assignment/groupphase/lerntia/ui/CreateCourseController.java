package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleCourseService;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.lang.invoke.MethodHandles;

@Controller
public class CreateCourseController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SimpleCourseService courseService;
    private final WindowController windowController;
    private final AlertController alertController;

    @FXML
    private TextField tf_courseName;
    @FXML
    private TextField tf_courseMark;
    @FXML
    private ComboBox<String> cb_semester;
    @FXML
    private TextField tf_semesterYear;

    @Autowired
    public CreateCourseController(SimpleCourseService courseService, WindowController windowController, AlertController alertController) {
        this.courseService = courseService;
        this.windowController = windowController;
        this.alertController = alertController;
    }

    @FXML
    private void initialize() {
        LOG.debug("Initialize for CreateCourseController");
        cb_semester.getItems().add(Semester.WS.toString());
        cb_semester.getItems().add(Semester.SS.toString());
        cb_semester.getSelectionModel().selectFirst();
    }

    void showCreateCourseWindow() {
        LOG.info("Show Create Course window.");
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/createCourse.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowController.openNewWindow("LVA erstellen", fxmlLoader);
    }

    public void createCourse(ActionEvent actionEvent) {
        try {
            LOG.info("Create Course Button clicked.");
            String mark = tf_courseMark.getText().trim();
            String name = tf_courseName.getText().trim();
            String semester = cb_semester.getSelectionModel().getSelectedItem();
            String semesterYear = tf_semesterYear.getText().trim();

            Course course = new Course(mark, semester + semesterYear, name, false);
            LOG.info("Validate inserted course values");
            courseService.validate(course);
            LOG.info("Send the new course to the next Layer for creation");
            courseService.create(course);

            alertController.showStandardAlert(Alert.AlertType.INFORMATION, "LVA erstellen erfolgreich", "Erfolg",
                "LVA erfolgreich angelegt.");

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "LVA erstellen fehlgeschlagen", "Fehler",
                e.getCustomMessage());
        }
    }
}
