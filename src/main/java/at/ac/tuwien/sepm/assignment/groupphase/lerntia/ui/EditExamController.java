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
import org.springframework.stereotype.Controller;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class EditExamController {

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

    private ObservableList<Question> examQuestionList = FXCollections.observableArrayList();

    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableColumn<Question,String> questionColumn;
    @FXML
    private TableColumn<Integer,Integer> columnList;
    @FXML
    private Button tableViewButton;
    @FXML
    private Button randomButton;
    @FXML
    private Button removeButton;
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
        setQuestionTable();
        removeButton.setDisable(true);
        resetButton.setDisable(true);
        questionTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedQuestions = questionTable.getSelectionModel().getSelectedItems();
                if (selectedQuestions.size()!=0 || selectedQuestions != null){
                    removeButton.setDisable(false);
                }else {
                    removeButton.setDisable(true);
                }

            }
        });

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                questionTable.getSelectionModel().clearSelection();
                removeButton.setDisable(true);
            }
        });
        questionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        questionColumn.setSortable(false);
        questionTable.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();

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
                    Question draggedQuestion = questionTable.getItems().remove(draggedIndex);

                    int dropIndex ;

                    if (row.isEmpty()) {
                        dropIndex = questionTable.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    questionTable.getItems().add(dropIndex, draggedQuestion);

                    event.setDropCompleted(true);
                    questionTable.getSelectionModel().clearSelection();
                    questionTable.getSelectionModel().select(dropIndex);
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
            if (questionTable.getItems().size()==0){
                throw new ControllerException("Keine Fragen vorhanden");
            }
            ArrayList questionList = new ArrayList();
            for (int i=0;i<questionTable.getItems().size();i++) {
                Question tableRow = questionTable.getItems().get(i);
                questionList.add(tableRow);
            }

            lerntiaMainController.setExamMode(true);
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
            if (questionTable.getItems().size()==0){
                throw new ControllerException("Keine Fragen vorhanden");
            }
        ArrayList questionList = new ArrayList();
        for (int i=0;i<questionTable.getItems().size();i++) {
            Question tableRow = questionTable.getItems().get(i);
            questionList.add(tableRow);
        }
        Collections.shuffle(questionList);
        lerntiaMainController.setExamMode(true);
        lerntiaMainController.setExamMode(true);
        lerntiaMainController.switchToExamMode();
        mainLerntiaService.setCustomExamQuestions(questionList);
        lerntiaMainController.getAndShowTheFirstExamQuestion();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfungsmodus anzeigen fehlgeschlagen.",
                "Fehler","Es ist nicht möglich in den Prüfungsmodus zu wechseln!.");
        } catch (ControllerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR,"Lehre Prüfungsfragenbogen.","Keine Fragen verfügbar","Nicht möglich in den Prüfungsmodus zu wechseln.\n Revetieren Sie die gelöschten Fragen und versuchen Sie erneut.");
        }


    }

    public void onRemoveButtonClicked(ActionEvent actionEvent) {
        for (Question q: selectedQuestions){
            System.out.println("STAAAAA"+selectedQuestions.size());
            if (currentQuestionList.contains(q)){
                currentQuestionList.remove(q);
            }
        }
        ObservableList<Question> newList = FXCollections.observableArrayList(currentQuestionList);
        resetButton.setDisable(false);
        questionTable.setItems(newList);
        selectedQuestions.clear();
    }

    public void onResetButtonClicked(ActionEvent actionEvent) {
        questionTable.setItems(FXCollections.observableArrayList(entirequestionList));
        resetButton.setDisable(true);
        selectedQuestions.clear();
        currentQuestionList.clear();
        entirequestionList.forEach(currentQuestionList::add);
    }
}
