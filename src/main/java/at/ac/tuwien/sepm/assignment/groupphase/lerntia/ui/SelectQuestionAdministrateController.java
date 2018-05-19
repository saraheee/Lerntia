package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Controller
public class SelectQuestionAdministrateController {

    @FXML public TableView<Question> tv_questionTable;
    @FXML public TableColumn<Question, Long> tc_id;
    @FXML public TableColumn<Question, String> tc_question;
    @FXML public TableColumn<Question, String> tc_answer1;
    @FXML public TableColumn<Question, String> tc_answer2;
    @FXML public TableColumn<Question, String> tc_answer3;
    @FXML public TableColumn<Question, String> tc_answer4;
    @FXML public TableColumn<Question, String> tc_answer5;

    private final LerntiaMainController lerntiaMainController;
    private final IMainLerntiaService lerntiaService;
    private final WindowController windowController;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Stage stage;


    public SelectQuestionAdministrateController(IMainLerntiaService lerntiaService, LerntiaMainController lerntiaMainController, WindowController windowController) {
        this.lerntiaService = lerntiaService;
        this.lerntiaMainController = lerntiaMainController;
        this.windowController = windowController;
    }

    public void initialize(){
        /**
         * The following line must stay there. It Refreshs the LerntiaService.
         */

        try {
            lerntiaService.getFirstQuestion();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //Fill the First Table.
        tc_id.setCellValueFactory(new PropertyValueFactory<Question, Long>("id"));
        tc_question.setCellValueFactory(new PropertyValueFactory<Question, String>("questionText"));
        tc_answer1.setCellValueFactory(new PropertyValueFactory<Question, String>("answer1"));
        tc_answer2.setCellValueFactory(new PropertyValueFactory<Question, String>("answer2"));
        tc_answer3.setCellValueFactory(new PropertyValueFactory<Question, String>("answer3"));
        tc_answer4.setCellValueFactory(new PropertyValueFactory<Question, String>("answer4"));
        tc_answer5.setCellValueFactory(new PropertyValueFactory<Question, String>("answer5"));
        ObservableList<Question> content = this.getContent();
        tv_questionTable.getItems().addAll(content);
        tv_questionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    /**
     * Loads the Data into the TableView
     */
    public void refresh(){
        tv_questionTable.getItems().addAll(getContent());
    }

    public ObservableList<Question> getContent(){
        ObservableList<Question> content = FXCollections.observableArrayList();
        for(int i = 0;i<lerntiaService.getQuestionList().size();i++){
            content.add(lerntiaService.getQuestionList().get(i));
        }
        return content;
    }

    /**
     * Opens the first Window in the SelectQuestionAdministrate operation.
     * Opens a window in which the user can See all the Questions .
     */
    public void showSelectQuestionAdministrateWindow(){
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionAdministrate.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Fragebogen verwalten", fxmlLoader);
    }

    @FXML
    public void editQuestion(ActionEvent actionEvent) {
        //TODO Editing Questions.
    }

    @FXML
    public void deleteQuestions(ActionEvent actionEvent) {
        //TODO Deleting Questions
    }

    @FXML
    public void markForExam(ActionEvent actionEvent) {
        //Todo Mark for Exam
    }
}
