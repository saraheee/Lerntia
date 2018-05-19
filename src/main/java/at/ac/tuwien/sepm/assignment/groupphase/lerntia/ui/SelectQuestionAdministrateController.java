package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    @FXML public TableView<Question> tv_details;
    @FXML public TableColumn<Question, String> tc_answer1;
    @FXML public TableColumn<Question, String> tc_answer2;
    @FXML public TableColumn<Question, String> tc_answer3;
    @FXML public TableColumn<Question, String> tc_answer4;
    @FXML public TableColumn<Question, String> tc_answer5;
    @FXML public TableColumn<Question, Long> tc_detailsID;

    private final LerntiaMainController lerntiaMainController;
    private final IMainLerntiaService lerntiaService;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());



    public SelectQuestionAdministrateController(IMainLerntiaService lerntiaService, LerntiaMainController lerntiaMainController) {
        this.lerntiaService = lerntiaService;
        this.lerntiaMainController = lerntiaMainController;
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
        tc_detailsID.setCellValueFactory(new PropertyValueFactory<Question, Long>("id"));
        tc_question.setCellValueFactory(new PropertyValueFactory<Question, String>("questionText"));
        tc_answer1.setCellValueFactory(new PropertyValueFactory<Question, String>("answer1"));
        tc_answer2.setCellValueFactory(new PropertyValueFactory<Question, String>("answer2"));
        tc_answer3.setCellValueFactory(new PropertyValueFactory<Question, String>("answer3"));
        tc_answer4.setCellValueFactory(new PropertyValueFactory<Question, String>("answer4"));
        tc_answer5.setCellValueFactory(new PropertyValueFactory<Question, String>("answer5"));
        ObservableList<Question> content = this.getContent();
        tv_questionTable.getItems().addAll(content);

        //Allow the First table more the one Selection and close the Selection for the secound one.
        tv_questionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tv_details.getSelectionModel().setSelectionMode(null);
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

    @FXML
    public void selectQuestion(ActionEvent actionEvent) {
        ObservableList<Question> selectedItems = tv_questionTable.getSelectionModel().getSelectedItems();
        if(selectedItems.size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler!");
            alert.setHeaderText("Bitte min. 1 Frage auswählen.");
            alert.setContentText(null);
            alert.showAndWait();
        }

        //Delete the Current content of the Table
        for(int i = 0;i<tv_details.getItems().size();i++){
            tv_details.getItems().clear();
        }

        //Load the new content intu the Table
        tv_details.getItems().addAll(selectedItems);
    }

    /**
     * Opens the first Window in the SelectQuestionAdministrate operation.
     * Opens a window in which the user can See all the Questions .
     * The Code is taken from SelectQuestionnaireController
     */
    public void showSelectQuestionAdministrateWindow(){
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionAdministrate.fxml"));
        var stage = new Stage();

        try {
            fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("[Lerntia] Fragebogen verwalten");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            LOG.debug("Successfully opened a window for administrating the questions.");
        } catch (IOException e) {
            LOG.error("Failed to open a window for administrating a questionnaire. " + e.getMessage());
        }
    }

    @FXML
    public void backButton(ActionEvent actionEvent) {
        //Todo Close the Current window and Open the Last one.
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
