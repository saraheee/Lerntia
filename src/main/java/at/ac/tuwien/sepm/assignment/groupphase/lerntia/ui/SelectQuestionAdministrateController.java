package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleLearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionnaireQuestionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.List;


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

    //For the Searching Operation
    @FXML public TextField tf_searchQuestion;
    @FXML public TextField tf_searchAnswer1;
    @FXML public TextField tf_searchAnswer2;
    @FXML public TextField tf_searchAnswer3;
    @FXML public TextField tf_searchAnswer4;
    @FXML public TextField tf_searchAnswer5;

    private final LerntiaMainController lerntiaMainController;
    private final IMainLerntiaService lerntiaService;
    private final WindowController windowController;
    private final IQuestionService questionDAO;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final IQuestionnaireQuestionService questionnaireQuestionService;


    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Stage stage;
    private LearningQuestionnaire administrateMode;

    public SelectQuestionAdministrateController(IMainLerntiaService lerntiaService,
                                                LerntiaMainController lerntiaMainController,
                                                WindowController windowController,
                                                IQuestionService questionDAO,
                                                SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
                                                IQuestionnaireQuestionService questionnaireQuestionService)
    {
        this.lerntiaService = lerntiaService;
        this.lerntiaMainController = lerntiaMainController;
        this.windowController = windowController;
        this.questionDAO = questionDAO;
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.questionnaireQuestionService = questionnaireQuestionService;
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
        //tc_id.setCellValueFactory(new PropertyValueFactory<Question, Long>("id"));
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
        //Clear the Table and Load the new Data
        tv_questionTable.getItems().clear();
        tv_questionTable.getItems().addAll(getContent());
        try {
            lerntiaMainController.getAndShowTheFirstQuestion();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
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
    public void showSelectQuestionAdministrateWindow(LearningQuestionnaire administrateMode){
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionAdministrate.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Fragebogen verwalten", fxmlLoader);
        this.administrateMode = administrateMode;
    }

    @FXML
    public void editQuestion(ActionEvent actionEvent) {
        //TODO Editing Questions.
    }

    @FXML
    public void deleteQuestions(ActionEvent actionEvent) {
        LOG.info("Delete Button Clicked");
        ObservableList<Question> selectedItems = tv_questionTable.getSelectionModel().getSelectedItems();
        if(selectedItems.size() == 0){
            //Nothing is selected -> Show a warning window
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Fragen löschen");
            warningAlert.setHeaderText("Min 1.Frage auswählen");
            warningAlert.setContentText(null);
            warningAlert.show();
            return;
        }

        //If min. 1 question is selected.
        Alert infoAlert = new Alert(Alert.AlertType.CONFIRMATION);
        infoAlert.setTitle("Fragen löschen");
        infoAlert.setHeaderText("Sollen die ausgewählte/n Frage/n gelöscht werden?");
        String allQuestions = "";
        for(int i = 0;i<selectedItems.size();i++){
            Question q = selectedItems.get(i);
            allQuestions+= "Es wurden: "+selectedItems.size()+" ausgewählt";
        }
        infoAlert.setContentText(allQuestions);
        Optional<ButtonType> result = infoAlert.showAndWait();
        if(result.get() == ButtonType.OK){
            LOG.info("LookHere: Es wird gelöscht");
            for(int i = 0;i<selectedItems.size();i++){
                try {
                    questionDAO.delete(selectedItems.get(i));
                    QuestionnaireQuestion qq = new QuestionnaireQuestion();
                    qq.setQid(administrateMode.getId());
                    qq.setQuestionid(selectedItems.get(i).getId());
                    questionnaireQuestionService.delete(qq);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }

            LOG.info("Delete Complete - Start Refreshing");
            //Close Window and Open informationen Window
            stage.close();
            Alert deleteInfo = new Alert(Alert.AlertType.INFORMATION);
            deleteInfo.setTitle(null);
            deleteInfo.setHeaderText("Fragen wurden gelöscht");
            deleteInfo.setContentText(null);
            deleteInfo.show();
            //call the First Question -> Is important for the Issue: What if the user deletes the Current or first Question

            try {
                lerntiaMainController.getAndShowTheFirstQuestion();
            } catch (ControllerException e) {
                e.printStackTrace();
            }
        }else{
            LOG.info("Cancled Delete Operation");
        }
    }

    @FXML
    public void markForExam(ActionEvent actionEvent) {
        //Todo Mark for Exam
    }

    @FXML
    public void searchQuestion(ActionEvent actionEvent) {
        this.stage.close();
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/searchQuestions.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Frage suchen", fxmlLoader);
        //Todo - Searching for Questions/Answers
    }

    /**
     * Is a Helping Function used for the Search operation
     * @param actionEvent
     */
    @FXML
    public void onSearchButtonClicked(ActionEvent actionEvent) {
        Question questionInput = new Question();
        questionInput.setQuestionText(tf_searchQuestion.getText());
        questionInput.setAnswer1(tf_searchAnswer1.getText());
        questionInput.setAnswer2(tf_searchAnswer2.getText());
        questionInput.setAnswer3(tf_searchAnswer3.getText());
        questionInput.setAnswer4(tf_searchAnswer4.getText());
        questionInput.setAnswer5(tf_searchAnswer5.getText());
        ObservableList<Question> newContent = FXCollections.observableArrayList();

        try {
            //Get the List and Load it into the TableView
            List<Question> searchedQuestions = questionDAO.searchForQuestions(questionInput);
            newContent.addAll(searchedQuestions);
            for(int i =0;i<tv_questionTable.getItems().size();i++){
                tv_questionTable.getItems().clear();
            }

            LOG.info("HABIBI: "+getContent().size()+" HBUBUB: "+newContent.size());
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        //Open the prev. Window and close the Current one
        this.stage.close();
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionAdministrate.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Fragebogen verwalten", fxmlLoader);
        tv_questionTable.getItems().clear();
        tv_questionTable.getItems().addAll(newContent);
    }
}
