package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.codehaus.groovy.runtime.dgmimpl.arrays.LongArrayGetAtMetaMethod;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
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
    private ExamQuestionnaire selected;

    private ObservableList<Question> examQuestionList = FXCollections.observableArrayList();

    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableColumn<Question,String> questionColumn;
    @FXML
    private TableColumn<Integer,Integer> columnList;

    public EditExamController(LerntiaMainController lerntiaMainController,
                              WindowController windowController,
                              AlertController alertController,
                              IExamQuestionnaireService examQuestionnaireService,
                              IQuestionnaireQuestionService questionnaireQuestionService,
                              IQuestionService questionService) {
        this.lerntiaMainController = lerntiaMainController ;
        this.windowController = windowController;
        this.alertController = alertController;
        this.examQuestionnaireService = examQuestionnaireService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.questionService = questionService;
    }

    @FXML
    private void initialize(){
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        setQuestionTable();

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

                    //TODO IMPORTANT
                    Question tableRow = questionTable.getItems().get(0);

                    event.setDropCompleted(true);
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
        List<Question> questionList = new ArrayList<>();
        questionnaireQuestion.setQid(selected.getId());
        List<QuestionnaireQuestion> helper = questionnaireQuestionService.search(questionnaireQuestion);

        Question searchparameter;
        for (QuestionnaireQuestion q:helper){
            searchparameter = new Question();
            searchparameter.setId(q.getQuestionid());
        }

        questionList = questionService.search(searchparameters);
        examQuestionList = FXCollections.observableArrayList(questionList);
        questionTable.setItems(FXCollections.observableArrayList(examQuestionList));
        questionTable.getSelectionModel().selectFirst();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getTableViewValues(TableView tableView) {
        //TODO implement when implementing buttons
        ArrayList<String> values = new ArrayList<>();
        ObservableList<Question> columns = tableView.getColumns();
        return values;
    }
}
