package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleCourseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
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

    @FXML
    private TextField tf_courseName;
    @FXML
    private TextField tf_courseMark;
    @FXML
    private ChoiceBox<String> cb_semester;
    @FXML
    private TextField tf_semesterYear;

    @Autowired
    public CreateCourseController(SimpleCourseService courseService, WindowController windowController) {
        this.courseService = courseService;
        this.windowController = windowController;
    }

    @FXML
    private void initialize() {
        cb_semester.getItems().add("W");
        cb_semester.getItems().add("S");

        cb_semester.getSelectionModel().selectFirst();
    }

    void showCreateCourseWindow() {

        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/createCourse.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowController.openNewWindow("LVA erstellen", fxmlLoader);
    }

    public void createCourse(ActionEvent actionEvent) {

        try {

            String mark = tf_courseMark.getText().trim();
            String name = tf_courseName.getText().trim();
            String semester = cb_semester.getSelectionModel().getSelectedItem();
            String semesterYear = tf_semesterYear.getText().trim();

            Course course = new Course(mark, semesterYear + semester, name, false);

            courseService.validate(course);
            courseService.create(course);

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (ServiceException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("[Lerntia] LVA erstellen fehlgeschlagen");
            alert.setHeaderText("Fehler");
            alert.setContentText(e.getMessage());
            alert.setResizable(true);
            alert.showAndWait();

        }
    }
}
