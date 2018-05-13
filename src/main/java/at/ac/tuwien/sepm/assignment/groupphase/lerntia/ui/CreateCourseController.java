package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ICourseService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleCourseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Controller
public class CreateCourseController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SimpleCourseService courseService;

    @FXML
    private TextField tf_courseName;
    @FXML
    private ChoiceBox<String> cb_semester;
    @FXML
    private TextField tf_semesterYear;

    public CreateCourseController(SimpleCourseService courseService){
        this.courseService = courseService;
    }

    @FXML
    private void initialize() {
        cb_semester.getItems().add("ws");
        cb_semester.getItems().add("ss");

        cb_semester.getSelectionModel().selectFirst();
    }

    void showCreateCourseWindow() {

        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/createCourse.fxml"));
        var stage = new Stage();

        try {
            fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("[Lerntia] LVA erstellen");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            LOG.debug("Successfully opened a window for creating a course.");
        } catch (IOException e) {
            LOG.error("Failed to open a window for creating a course. " + e.getMessage());
        }
    }

    public void createCourse(ActionEvent actionEvent) {

        try {

            String name = tf_courseName.getText().trim();
            String semester = cb_semester.getSelectionModel().getSelectedItem();
            String semesterYear = tf_semesterYear.getText().trim();

            Course course = new Course(name, semester + semesterYear, name, false);

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
