package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class MenuBarController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ImportFileController importFileController;
    private final CreateCourseController createCourseController;

    @Autowired
    MenuBarController(
        ImportFileController importFileController,
        CreateCourseController createCourseController
    ) {
        this.importFileController = importFileController;
        this.createCourseController = createCourseController;
    }

    @FXML
    private void importQuestions() {
        importFileController.showImportWindow();
    }

    public void createCourse(ActionEvent actionEvent) { createCourseController.showCreateCourseWindow(); }
}