package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Controller
public class SelectQuestionAdministrateController implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LerntiaMainController lerntiaMainController;
    private final IMainLerntiaService lerntiaService;
    private final WindowController windowController;
    private final IQuestionService questionService;
    private final IQuestionnaireQuestionService questionnaireQuestionService;
    private final EditQuestionsController editQuestionsController;
    private final AlertController alertController;
    private final ILearningQuestionnaireService iLearningQuestionnaireService;
    private final IQuestionnaireQuestionService iQuestionnaireQuestionService;
    @FXML
    public TableView<Question> tv_questionTable;
    @FXML
    public TableColumn<Question, CheckBox> tc_picture;
    @FXML
    public TableColumn<Question, String> tc_question;
    @FXML
    public TableColumn<Question, String> tc_answer1;
    @FXML
    public TableColumn<Question, String> tc_answer2;
    @FXML
    public TableColumn<Question, String> tc_answer3;
    @FXML
    public TableColumn<Question, String> tc_answer4;
    @FXML
    public TableColumn<Question, String> tc_answer5;
    //For the Searching Operation
    @FXML
    public TextField tf_searchQuestion;
    @FXML
    public TextField tf_searchAnswer1;
    @FXML
    public TextField tf_searchAnswer2;
    @FXML
    public TextField tf_searchAnswer3;
    @FXML
    public TextField tf_searchAnswer4;
    @FXML
    public TextField tf_searchAnswer5;
    private Stage stage;
    private LearningQuestionnaire administrateMode;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;

    private boolean deleteButtonClicked = false;
    private boolean editButtonClicked = false;
    private boolean searchButtonClicked = false;
    private boolean closedWindow = false;
    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    public SelectQuestionAdministrateController(
        IMainLerntiaService lerntiaService,
        LerntiaMainController lerntiaMainController,
        WindowController windowController,
        IQuestionService questionService,
        EditQuestionsController editQuestionsController,
        IQuestionnaireQuestionService questionnaireQuestionService,
        AlertController alertController,
        ILearningQuestionnaireService iLearningQuestionnaireService,
        IQuestionnaireQuestionService iQuestionnaireQuestionService) {
        this.lerntiaService = lerntiaService;
        this.lerntiaMainController = lerntiaMainController;
        this.windowController = windowController;
        this.questionService = questionService;
        this.editQuestionsController = editQuestionsController;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.alertController = alertController;
        this.iLearningQuestionnaireService = iLearningQuestionnaireService;
        this.iQuestionnaireQuestionService = iQuestionnaireQuestionService;
    }

    public void initialize() {
        /*
         * The following line must stay there. It Refreshs the LerntiaService.
         */
        try {
            lerntiaService.loadQuestionnaireAndGetFirstQuestion();
        } catch (ServiceException e) {
            // TODO - show alert or throw new exception
        }

        //Fill the First Table.
        tc_question.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        tc_answer1.setCellValueFactory(new PropertyValueFactory<>("answer1"));
        tc_answer2.setCellValueFactory(new PropertyValueFactory<>("answer2"));
        tc_answer3.setCellValueFactory(new PropertyValueFactory<>("answer3"));
        tc_answer4.setCellValueFactory(new PropertyValueFactory<>("answer4"));
        tc_answer5.setCellValueFactory(new PropertyValueFactory<>("answer5"));
        tc_picture.setCellValueFactory(new PropertyValueFactory<>("containPicture"));
        var content = this.getContent();

        tv_questionTable.getItems().addAll(content);
        tv_questionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        var buttonThread = new Thread(this);
        buttonThread.start();
        tv_questionTable.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();
            showHoverText(row);
            return row;
        });
    }

    private void showHoverText(TableRow<Question> row) {
        row.hoverProperty().addListener(event -> {
            if (!row.isEmpty()) {
                ToolTipManager.sharedInstance().setInitialDelay(10);
                var delay = Integer.MAX_VALUE;
                ToolTipManager.sharedInstance().setDismissDelay(delay);
                final var t = new Tooltip();
                var q = row.getItem();
                t.setText(q.toStringGUI());
                row.setTooltip(t);
            }
        });
    }

    /**
     * Refreshs the Data and the lertiaMainController
     */
    public void refresh() {
        LearningQuestionnaire studyMode;
        try {
            studyMode = iLearningQuestionnaireService.getSelected();
            iLearningQuestionnaireService.deselect(studyMode);
            iLearningQuestionnaireService.select(administrateMode);
            this.stage.close();
            var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionAdministrate.fxml"));
            fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
            this.stage = windowController.openNewWindow("Fragebogen verwalten", fxmlLoader);
            stage.setOnCloseRequest(event -> closedWindow = true);
            tv_questionTable.getItems().clear();
            tv_questionTable.getItems().addAll(getContent());
            iLearningQuestionnaireService.deselect(administrateMode);
            iLearningQuestionnaireService.select(studyMode);
            lerntiaMainController.getAndShowTheFirstQuestion();

            var buttonThread = new Thread(this);
            buttonThread.start();

        } catch (ServiceException e) {
            LOG.debug("Failed to refresh the table!");
        }
    }

    /**
     * @return the Content for the Table in form of a ObservableList<Question>
     */
    private ObservableList<Question> getContent() {
        ObservableList<Question> content = FXCollections.observableArrayList();
        content.addAll(lerntiaService.getQuestionList());
        return content;
    }

    /**
     * Opens the first Window in the SelectQuestionAdministrate operation.
     * Opens a window in which the user can See all the Questions .
     */
    public void showSelectQuestionAdministrateWindow(LearningQuestionnaire administrateMode) {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionAdministrate.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Fragebogen verwalten", fxmlLoader);
        this.administrateMode = administrateMode;
        stage.setOnCloseRequest(event -> closedWindow = true);
    }

    @FXML
    public void editQuestion() {
        editButtonClicked = true;
        //Check if there is at Least and not more than one element is Selected
        ObservableList<Question> selectedItems = tv_questionTable.getSelectionModel().getSelectedItems();
        if (selectedItems.size() < 1) {
            alertController.showStandardAlert(Alert.AlertType.WARNING, "Bearbeitungsfehler",
                "Bitte mindestens eine Frage zum Bearbeiten auswählen!", null);
            return;
        }

        if (selectedItems.size() > 1) {
            alertController.showStandardAlert(Alert.AlertType.WARNING, "Bearbeitungsfehler",
                "Das Bearbeiten von mehr als einer Frage glechzeitig ist nicht möglich.",
                "Bitte genau eine Frage auswählen!");
            return;
        }
        Question selectedQuestion = selectedItems.get(0);
        LOG.info(selectedQuestion.toString());

        //Open the New Question.
        stage.close();
        editQuestionsController.showEditQuestionsControllerWindow(selectedItems.get(0), this);
    }

    @FXML
    public void deleteQuestions() {
        deleteButtonClicked = true;
        LOG.info("Delete Button Clicked");
        ObservableList<Question> selectedItems = tv_questionTable.getSelectionModel().getSelectedItems();
        if (selectedItems.size() == 0) {
            //Nothing is selected -> Show a warning window
            alertController.showStandardAlert(Alert.AlertType.WARNING, "Fragen löschen",
                "Bitte mindestens eine Frage zum Löschen auswählen!", null);
            return;
        }

        boolean press;
        //One question was selected
        if (selectedItems.size() == 1) {
            press = alertController.showStandardConfirmationAlert("Frage löschen",
                "Soll die ausgewählte Frage wirklich gelöscht werden?", "Diese Änderung kann nicht rückgängig gemacht werden!");
        } else {
            press = alertController.showStandardConfirmationAlert("Fragen löschen",
                "Sollen die ausgewählten Fragen wirklich gelöscht werden?", "Es wurden " + selectedItems.size() + " Fragen ausgewählt.\n" +
                    "Diese Änderung kann nicht rückgängig gemacht werden!");
        }
        if (press) {
            for (Question selectedItem : selectedItems) {
                try {
                    questionService.delete(selectedItem);
                    QuestionnaireQuestion qq = new QuestionnaireQuestion();
                    qq.setQid(administrateMode.getId());
                    qq.setQuestionid(selectedItem.getId());
                    questionnaireQuestionService.delete(qq);
                } catch (ServiceException e) {
                    // TODO - show alert or throw new exception
                }
            }

            LOG.info("Delete Complete - Start Refreshing");
            //Close Window and Open information Window
            alertController.showStandardAlert(Alert.AlertType.INFORMATION, "Löschvorgang abgeschlossen",
                "Erfolgreich gelöscht!", "Die ausgewählen Fragen wurden erfolgreich gelöscht!");

            //call the First Question -> Is important for the Issue: What if the user deletes the Current or first Question
            lerntiaMainController.getAndShowTheFirstQuestion();
            this.refresh();
        }
    }

    @FXML
    public void searchQuestion() {
        searchButtonClicked = true;
        this.stage.close();
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/searchQuestions.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Frage suchen", fxmlLoader);
        stage.setOnCloseRequest(event -> closedWindow = true);

        var buttonThread = new Thread(this);
        buttonThread.start();
    }

    /**
     * Is a Helping Function used for the Search operation
     */
    @FXML
    public void onSearchButtonClicked() {

        LOG.info("Hier: "+administrateMode.getId());
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(administrateMode.getId());
        List<QuestionnaireQuestion> tableQuestions = null;
        List<Long> allIDs = new ArrayList<>();
        try {
            tableQuestions = iQuestionnaireQuestionService.search(questionnaireQuestion);
            for(int i = 0;i<tableQuestions.size();i++){
                allIDs.add(tableQuestions.get(i).getQuestionid());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        searchButtonClicked = true;
        Question questionInput = new Question();
        questionInput.setQuestionText(tf_searchQuestion.getText().trim());
        questionInput.setAnswer1(tf_searchAnswer1.getText().trim());
        questionInput.setAnswer2(tf_searchAnswer2.getText().trim());
        questionInput.setAnswer3(tf_searchAnswer3.getText().trim());
        questionInput.setAnswer4(tf_searchAnswer4.getText().trim());
        questionInput.setAnswer5(tf_searchAnswer5.getText().trim());
        ObservableList<Question> newContent = FXCollections.observableArrayList();

        try {
            //Get the List and Load it into the TableView
            List<Question> searchedQuestions = questionService.searchForQuestions(questionInput);
            newContent.addAll(searchedQuestions);
            for (int i = 0; i < tv_questionTable.getItems().size(); i++) {
                tv_questionTable.getItems().clear();
            }

            //Delete the Questions that are not in the selected Questionnaire.
            for(int i = 0;i<searchedQuestions.size();i++){
                if(!(allIDs.contains(searchedQuestions.get(i).getId()))){
                    searchedQuestions.remove(searchedQuestions.get(i));
                }
            }

            LOG.trace("Content size: " + getContent().size() + ", new content size: " + newContent.size());
        } catch (ServiceException e) {
            // TODO - show alert or throw new exception
            AlertController alertController = new AlertController();
            alertController.showStandardAlert(Alert.AlertType.INFORMATION,"Warnung","Such option fehlgeschlagen",null);
        }

        //Open the prev. Window and close the Current one
        this.stage.close();
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/selectQuestionAdministrate.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        this.stage = windowController.openNewWindow("Fragebogen verwalten", fxmlLoader);
        stage.setOnCloseRequest(event -> closedWindow = true);
        tv_questionTable.getItems().clear();
        tv_questionTable.getItems().addAll(newContent);
    }

    public LearningQuestionnaire getAdministrateMode() {
        return this.administrateMode;
    }

    @Override
    public void run() {
        resetValues();
        while (!closedWindow && !deleteButtonClicked && !editButtonClicked && !searchButtonClicked) {
            lock.lock();
            try {
                var selectedItems = tv_questionTable.getSelectionModel().getSelectedItems();
                if (selectedItems.size() < 1) {
                    editButton.setDisable(true);
                    deleteButton.setDisable(true);
                }
                if (selectedItems.size() == 1) {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                }
                if (selectedItems.size() > 1) {
                    editButton.setDisable(true);
                    deleteButton.setDisable(false);
                }
            } finally {
                lock.unlock();
            }

        }
    }

    private void resetValues() {
        closedWindow = false;
        editButtonClicked = false;
        deleteButtonClicked = false;
        searchButtonClicked = false;
    }

}
