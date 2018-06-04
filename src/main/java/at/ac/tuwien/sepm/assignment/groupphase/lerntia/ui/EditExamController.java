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
import org.codehaus.groovy.runtime.dgmimpl.arrays.LongArrayGetAtMetaMethod;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EditExamController {

    private final LerntiaMainController lerntiaMainController;
    private final WindowController windowController;
    private final AlertController alertController;
    private final IExamQuestionnaireService examQuestionnaireService;
    private final IQuestionnaireQuestionService questionnaireQuestionService;
    private final IQuestionService questionService;
    private ExamQuestionnaire selected;

    private ObservableList<Question> examQuestionList = FXCollections.observableArrayList();

    @FXML
    private ListView<Question> listView = new ListView<>();
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
            listView.getItems().setAll(examQuestionList);

        listView.setCellFactory(param -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getQuestionText() == null) {
                    setText(null);
                } else {
                    setText(item.getQuestionText());
                }
            }
        });
    }



    public void showSelectExamWindow(ExamQuestionnaire selectedQuestionnaire){
        selected = selectedQuestionnaire;
        System.out.println("STA OCES");
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/editExam.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowController.openNewWindow("Fragebogen editieren", fxmlLoader);

    }

    private void setQuestionTable() {
        try {
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            System.out.println("sta je problem?");
            List<Question> searchparameters = new ArrayList<>();
            List<Question> questionList = new ArrayList<>();

        System.out.println("sigurno");
        System.out.println(selected.getId());


        questionnaireQuestion.setQid(selected.getId());
        System.out.println(questionnaireQuestion.getQid());
        System.out.println("A sad?");
        List<QuestionnaireQuestion> helper = questionnaireQuestionService.search(questionnaireQuestion);

        Question searchparameter;
        for (QuestionnaireQuestion q:helper){
            searchparameter = new Question();
            searchparameter.setId(q.getQuestionid());
        }
        System.out.println(helper.size()+"helper");




         questionList = questionService.search(searchparameters);
        System.out.println("hahaha"+questionList.size());
        examQuestionList = FXCollections.observableArrayList(questionList);
        questionTable.setItems(FXCollections.observableArrayList(examQuestionList));
        questionTable.getSelectionModel().selectFirst();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
