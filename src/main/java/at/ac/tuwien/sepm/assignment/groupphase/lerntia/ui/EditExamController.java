package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ControllerException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class EditExamController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private final LerntiaMainController lerntiaMainController;
    private final WindowController windowController;
    private final AlertController alertController;
    private final IExamQuestionnaireService examQuestionnaireService;
    private final IQuestionnaireQuestionService questionnaireQuestionService;
    private final IQuestionService questionService;
    private final IMainLerntiaService mainLerntiaService;
    private ExamQuestionnaire selected;
    private ObservableList<Question> selectedQuestions;
    private List<Question> entirequestionList;
    private List<Question> currentQuestionList;
    private List<Question> acceptedQuestionList = new ArrayList<>();

    private ObservableList<Question> examQuestionList = FXCollections.observableArrayList();

    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableView<Question> acceptedTable;
    @FXML
    private TableColumn<Question,String> columnAccepted;
    @FXML
    private TableColumn<Question,String> firstAnswerColumn;
    @FXML
    private TableColumn<Question,String> secondAnswerColumn;
    @FXML
    private TableColumn<Question,String> thirdAnswerColumn;
    @FXML
    private TableColumn<Question,String> fourthAnswerColumn;
    @FXML
    private TableColumn<Question,String> fifthAnswerColumn;
    @FXML
    private TableColumn<Question,String> xfirstAnswerColumn;
    @FXML
    private TableColumn<Question,String> xsecondAnswerColumn;
    @FXML
    private TableColumn<Question,String> xthirdAnswerColumn;
    @FXML
    private TableColumn<Question,String> xfourthAnswerColumn;
    @FXML
    private TableColumn<Question,String> xfifthAnswerColumn;
    @FXML
    private TableColumn<Question,String> questionColumn;
    @FXML
    private TableColumn<Integer,Integer> columnList;
    @FXML
    private Button tableViewButton;
    @FXML
    private Button randomButton;
    @FXML
    private Button resetButton;
    @FXML
    private AnchorPane pane;

    public EditExamController(LerntiaMainController lerntiaMainController,
                              WindowController windowController,
                              AlertController alertController,
                              IExamQuestionnaireService examQuestionnaireService,
                              IQuestionnaireQuestionService questionnaireQuestionService,
                              IQuestionService questionService,
                              IMainLerntiaService mainLerntiaService) {
        this.lerntiaMainController = lerntiaMainController ;
        this.windowController = windowController;
        this.alertController = alertController;
        this.examQuestionnaireService = examQuestionnaireService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.questionService = questionService;
        this.mainLerntiaService = mainLerntiaService;
    }

    @FXML
    private void initialize(){
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        columnAccepted.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        firstAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer1"));
        xfirstAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer1"));
        secondAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer2"));
        xsecondAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer2"));
        thirdAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer3"));
        xthirdAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer3"));
        fourthAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer4"));
        xfourthAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer4"));
        fifthAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer5"));
        xfifthAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer5"));

        setQuestionTable();
        resetButton.setDisable(true);

        acceptedTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedQuestions = acceptedTable.getSelectionModel().getSelectedItems();
                questionTable.getSelectionModel().clearSelection();
            }
        });

        questionTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedQuestions = questionTable.getSelectionModel().getSelectedItems();
                acceptedTable.getSelectionModel().clearSelection();
                if (selectedQuestions.size()!=0 || selectedQuestions != null){
                }else {
                }

            }
        });

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                questionTable.getSelectionModel().clearSelection();
            }
        });
        questionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        acceptedTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        questionColumn.setSortable(false);
        questionTable.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();

            LOG.info("Row factory for Question Table");
            row.hoverProperty().addListener(event ->{
                if (!row.isEmpty()) {
                    LOG.info("Hovering over Question");
                    ToolTipManager.sharedInstance().setInitialDelay(40);
                    int delay = Integer.MAX_VALUE;
                    ToolTipManager.sharedInstance().setDismissDelay(delay);
                    final Tooltip t = new Tooltip();
                    Question q = row.getItem();
                    t.setText(q.toStringGUI());
                    row.setTooltip(t);


                }
            });


            row.setOnDragDetected(event -> {

                if (! row.isEmpty()) {
                    LOG.info("Drag detected");
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    LOG.info("");
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    LOG.info("Drag Dropped");
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Question draggedQuestion = questionTable.getItems().remove(draggedIndex);

                    int dropIndex ;

                    if (row.isEmpty()) {
                        dropIndex = questionTable.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }
                    questionTable.getItems().add(draggedQuestion);

                    event.setDropCompleted(true);
                    acceptedTable.getSelectionModel().clearSelection();
                    questionTable.getSelectionModel().clearSelection();
                    questionTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });
            return row ;
        });


        acceptedTable.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();


            row.hoverProperty().addListener(event ->{
                if (!row.isEmpty()) {
                    ToolTipManager.sharedInstance().setInitialDelay(40);
                    int delay = Integer.MAX_VALUE;
                    ToolTipManager.sharedInstance().setDismissDelay(delay);
                    final Tooltip t = new Tooltip();
                    Question q = row.getItem();
                    t.setText(q.toStringGUI());
                    row.setTooltip(t);
                }
            });

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Question draggedQuestion = acceptedTable.getItems().remove(draggedIndex);

                    int dropIndex ;

                    if (row.isEmpty()) {
                        dropIndex = acceptedTable.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }
                    acceptedTable.getItems().add(draggedQuestion);

                    event.setDropCompleted(true);
                    acceptedTable.getSelectionModel().clearSelection();
                    questionTable.getSelectionModel().clearSelection();
                    acceptedTable.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });
            return row ;
        });
    }




    public void showSelectExamWindow(ExamQuestionnaire selectedQuestionnaire){
        selected = selectedQuestionnaire;
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/editExam.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowController.openNewWindow("Fragebogen editieren", fxmlLoader);
    }

    private void setQuestionTable() {
        try {

        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        List<Question> searchparameters = new ArrayList<>();
        entirequestionList = new ArrayList<>();
        questionnaireQuestion.setQid(selected.getId());
        List<QuestionnaireQuestion> helper = questionnaireQuestionService.search(questionnaireQuestion);

        Question searchparameter;
        for (QuestionnaireQuestion q:helper){
            searchparameter = new Question();
            searchparameter.setId(q.getQuestionid());
            searchparameters.add(searchparameter);
        }

        entirequestionList = questionService.search(searchparameters);
        currentQuestionList = new ArrayList<>();
        entirequestionList.forEach(currentQuestionList::add);
        examQuestionList = FXCollections.observableArrayList(currentQuestionList);
        questionTable.setItems(FXCollections.observableArrayList(examQuestionList));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }



    public void onTableViewButtonClicked(ActionEvent actionEvent) {
        try {
            if (acceptedTable.getItems().size()==0){
                throw new ControllerException("Keine Fragen vorhanden");
            }
            ArrayList questionList = new ArrayList();
            for (int i=0;i<acceptedTable.getItems().size();i++) {
                Question tableRow = acceptedTable.getItems().get(i);
                questionList.add(tableRow);
            }

            lerntiaMainController.setExamMode(true);
            lerntiaMainController.switchToExamMode();
            mainLerntiaService.setCustomExamQuestions(questionList);
            lerntiaMainController.getAndShowTheFirstExamQuestion();
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfungsmodus anzeigen fehlgeschlagen.",
                "Fehler","Es ist nicht möglich in den Prüfungsmodus zu wechseln!");
        } catch (ControllerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR,"Lehre Prüfungsfragenbogen.","Keine Fragen verfügbar","Nicht möglich in den Prüfungsmodus zu wechseln.\n Revetieren Sie die gelöschten Fragen und versuchen Sie erneut.");
        }
    }

    public void onRandomButtonClicked(ActionEvent actionEvent) {
        try {
            if (acceptedTable.getItems().size() == 0) {
                throw new ControllerException("Keine Fragen vorhanden");
            }
            ArrayList questionList = new ArrayList();
            for (int i = 0; i < acceptedTable.getItems().size(); i++) {
                Question tableRow = acceptedTable.getItems().get(i);
                questionList.add(tableRow);
            }
            Collections.shuffle(questionList);
            acceptedTable.setItems(FXCollections.observableArrayList(questionList));


        }catch (ControllerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR,"Lehre Prüfungsfragenbogen.","Keine Fragen verfügbar","Nicht möglich in den Prüfungsmodus zu wechseln.\n Revetieren Sie die gelöschten Fragen und versuchen Sie erneut.");
        }


    }



    public void onResetButtonClicked(ActionEvent actionEvent) {
        questionTable.setItems(FXCollections.observableArrayList(entirequestionList));
        acceptedTable.setItems(FXCollections.observableArrayList());
        resetButton.setDisable(true);
        selectedQuestions.clear();
        currentQuestionList.clear();
        entirequestionList.forEach(currentQuestionList::add);
    }

    public void onAddQuestionsButtonClicked(ActionEvent actionEvent) {
        for (Question q: selectedQuestions){
            if (!acceptedQuestionList.contains(q)){
                acceptedQuestionList.add(q);
            }
        }
        for (Question q: selectedQuestions){
            if (currentQuestionList.contains(q)){
                currentQuestionList.remove(q);
            }
        }

        ObservableList<Question> newList = FXCollections.observableArrayList(currentQuestionList);
        resetButton.setDisable(false);
        acceptedTable.setItems(FXCollections.observableArrayList(acceptedQuestionList));
        questionTable.setItems(FXCollections.observableArrayList(currentQuestionList));
        selectedQuestions.clear();

    }

    public void onRemoveQuestionsButtonClicked(ActionEvent actionEvent) {
        for (Question q: selectedQuestions){
            if (acceptedQuestionList.contains(q)){
                acceptedQuestionList.remove(q);
            }
        }
        for (Question q: selectedQuestions){
            if (!currentQuestionList.contains(q)){
                currentQuestionList.add(q);
            }
        }

        ObservableList<Question> newList = FXCollections.observableArrayList(currentQuestionList);
        resetButton.setDisable(false);
        acceptedTable.setItems(FXCollections.observableArrayList(acceptedQuestionList));
        questionTable.setItems(FXCollections.observableArrayList(currentQuestionList));
        selectedQuestions.clear();
    }
}
