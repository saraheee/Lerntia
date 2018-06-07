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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
    private List<Question> entireQuestionList;
    private List<Question> currentQuestionList;
    private ArrayList<Question> questionList = new ArrayList<>();
    private List<Question> acceptedQuestionList = new ArrayList<>();
    private boolean editingCanceled = false;
    private ObservableList<Question> examQuestionList = FXCollections.observableArrayList();

    @FXML
    private TableView<Question> questionTable;
    @FXML
    private TableView<Question> acceptedTable;
    @FXML
    private TableColumn<Question, String> columnAccepted;
    @FXML
    private TableColumn<Question, String> firstAnswerColumn;
    @FXML
    private TableColumn<Question, String> secondAnswerColumn;
    @FXML
    private TableColumn<Question, String> thirdAnswerColumn;
    @FXML
    private TableColumn<Question, String> fourthAnswerColumn;
    @FXML
    private TableColumn<Question, String> fifthAnswerColumn;
    @FXML
    private TableColumn<Question, String> xfirstAnswerColumn;
    @FXML
    private TableColumn<Question, String> xsecondAnswerColumn;
    @FXML
    private TableColumn<Question, String> xthirdAnswerColumn;
    @FXML
    private TableColumn<Question, String> xfourthAnswerColumn;
    @FXML
    private TableColumn<Question, String> xfifthAnswerColumn;
    @FXML
    private TableColumn<Question, String> questionColumn;
    @FXML
    private TableColumn<Integer, Integer> columnList;
    @FXML
    private Button tableViewButton;
    @FXML
    private Button randomButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button removeQuestions;
    @FXML
    private Button addQuestions;
    @FXML
    private AnchorPane pane;
    private Stage windowStage;

    public EditExamController(LerntiaMainController lerntiaMainController,
                              WindowController windowController,
                              AlertController alertController,
                              IExamQuestionnaireService examQuestionnaireService,
                              IQuestionnaireQuestionService questionnaireQuestionService,
                              IQuestionService questionService,
                              IMainLerntiaService mainLerntiaService) {
        this.lerntiaMainController = lerntiaMainController;
        this.windowController = windowController;
        this.alertController = alertController;
        this.examQuestionnaireService = examQuestionnaireService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.questionService = questionService;
        this.mainLerntiaService = mainLerntiaService;
    }

    @FXML
    private void initialize() {
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
        removeQuestions.setDisable(true);
        addQuestions.setDisable(true);

        acceptedTable.setOnMousePressed(event -> {
            selectedQuestions = acceptedTable.getSelectionModel().getSelectedItems();
            questionTable.getSelectionModel().clearSelection();
            addQuestions.setDisable(true);
            removeQuestions.setDisable(false);
        });

        questionTable.setOnMousePressed(event -> {
            selectedQuestions = questionTable.getSelectionModel().getSelectedItems();
            acceptedTable.getSelectionModel().clearSelection();
            addQuestions.setDisable(false);
            removeQuestions.setDisable(true);
        });

        pane.setOnMouseClicked(event -> questionTable.getSelectionModel().clearSelection());
        questionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        acceptedTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        questionColumn.setSortable(false);
        questionTable.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();

            LOG.info("Row factory for Question Table");
            showHoverText(row);


            row.setOnDragDetected(event -> {

                if (!row.isEmpty()) {
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
                    if (row.getIndex() != (Integer) db.getContent(SERIALIZED_MIME_TYPE)) {
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

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = questionTable.getItems().size();
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
            return row;
        });


        acceptedTable.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();


            showHoverText(row);

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
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
                    if (row.getIndex() != (Integer) db.getContent(SERIALIZED_MIME_TYPE)) {
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

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = acceptedTable.getItems().size();
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
            return row;
        });
    }

    private void showHoverText(TableRow<Question> row) {
        row.hoverProperty().addListener(event -> {
            if (!row.isEmpty()) {
                ToolTipManager.sharedInstance().setInitialDelay(10);
                int delay = Integer.MAX_VALUE;
                ToolTipManager.sharedInstance().setDismissDelay(delay);
                final Tooltip t = new Tooltip();
                Question q = row.getItem();
                t.setText(q.toStringGUI());
                row.setTooltip(t);
            }
        });
    }


    public void showSelectExamWindow(ExamQuestionnaire selectedQuestionnaire) {
        selected = selectedQuestionnaire;
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/editExam.fxml"));
        fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
        windowStage = windowController.openNewWindow("Fragebogen editieren", fxmlLoader);

        windowStage.setOnCloseRequest(event -> {
            var alertController = new AlertController();
            if (alertController.showStandardConfirmationAlert("Prüfungseditierung abbrechen?",
                "Soll der Prüfungsvorgang wirklich beendet werden?",
                "Alle Änderungen gehen verloren und es erfolgt eine Weiterleitung in den Lernmodus!")) {
                LOG.debug("Canceled editing the exam!");
                editingCanceled = true;
                return;
            }
            event.consume();
        });
    }

    public boolean getEditingCanceled() {
        return editingCanceled;
    }

    public void setEditingCanceled(boolean editingCanceled) {
        this.editingCanceled = editingCanceled;
    }

    private void setQuestionTable() {
        try {
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            List<Question> searchparameters = new ArrayList<>();
            entireQuestionList = new ArrayList<>();
            questionnaireQuestion.setQid(selected.getId());
            List<QuestionnaireQuestion> helper = questionnaireQuestionService.search(questionnaireQuestion);

            Question searchParameter;
            for (QuestionnaireQuestion q : helper) {
                searchParameter = new Question();
                searchParameter.setId(q.getQuestionid());
                searchparameters.add(searchParameter);
            }

            entireQuestionList = questionService.search(searchparameters);
            currentQuestionList = new ArrayList<>();
            currentQuestionList.addAll(entireQuestionList);
            examQuestionList = FXCollections.observableArrayList(currentQuestionList);
            questionTable.setItems(FXCollections.observableArrayList(examQuestionList));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    public void onTableViewButtonClicked(ActionEvent actionEvent) {
        try {
            if (acceptedTable.getItems().size() == 0) {
                throw new ControllerException("Keine Fragen vorhanden");
            }
            if (questionList != null) {
                questionList.clear();
            }
            questionList.addAll(acceptedTable.getItems());

            lerntiaMainController.setExamMode(true);
            lerntiaMainController.switchToExamMode();
            mainLerntiaService.setCustomExamQuestions(questionList);
            lerntiaMainController.getAndShowTheFirstExamQuestion();
            entireQuestionList.clear();
            acceptedQuestionList.clear();
            examQuestionList.clear();
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Prüfungsmodus anzeigen fehlgeschlagen.",
                "Fehler", "Es ist nicht möglich in den Prüfungsmodus zu wechseln!");
        } catch (ControllerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Keine Fragen vorhanden.", "Keine Fragen verfügbar",
                "Die Prüfung kann nicht gestartet werden, wenn keine Fragen zur Prüfungstabelle hinzugefügt wurden.");
        }
    }

    public void onRandomButtonClicked() {
        try {
            if (acceptedTable.getItems().size() == 0) {
                throw new ControllerException("Keine Fragen vorhanden");
            }
            ArrayList<Question> questionList = new ArrayList<>(acceptedTable.getItems());
            Collections.shuffle(questionList);
            acceptedTable.setItems(FXCollections.observableArrayList(questionList));


        } catch (ControllerException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Keine Fragen vorhanden.", "Keine Fragen verfügbar",
                "Eine Zufallsreihenfolge kann nicht generiert werden, wenn keine Fragen zur Prüfungstabelle hinzugefügt wurden.");
        }


    }


    public void onResetButtonClicked() {
        questionTable.setItems(FXCollections.observableArrayList(entireQuestionList));
        acceptedTable.setItems(FXCollections.observableArrayList());
        resetButton.setDisable(true);
        selectedQuestions.clear();
        currentQuestionList.clear();
        acceptedQuestionList.clear();
        currentQuestionList.addAll(entireQuestionList);
    }

    public void onAddQuestionsButtonClicked() {
        if (selectedQuestions != null) {
            for (Question q : selectedQuestions) {
                if (!acceptedQuestionList.contains(q)) {
                    acceptedQuestionList.add(q);
                }
            }
            for (Question q : selectedQuestions) {
                if (currentQuestionList.contains(q)) {
                    currentQuestionList.remove(q);
                }
            }

            ObservableList<Question> newList = FXCollections.observableArrayList(currentQuestionList);
            resetButton.setDisable(false);
            addQuestions.setDisable(true);
            acceptedTable.setItems(FXCollections.observableArrayList(acceptedQuestionList));
            questionTable.setItems(FXCollections.observableArrayList(currentQuestionList));
            selectedQuestions.clear();
        }

    }

    public void onRemoveQuestionsButtonClicked() {
        if (selectedQuestions != null) {
            for (Question q : selectedQuestions) {
                if (acceptedQuestionList.contains(q)) {
                    acceptedQuestionList.remove(q);
                }
            }

            for (Question q : selectedQuestions) {
                if (!currentQuestionList.contains(q)) {
                    currentQuestionList.add(q);
                }
            }

            ObservableList<Question> newList = FXCollections.observableArrayList(currentQuestionList);
            resetButton.setDisable(false);
            removeQuestions.setDisable(true);
            acceptedTable.setItems(FXCollections.observableArrayList(acceptedQuestionList));
            questionTable.setItems(FXCollections.observableArrayList(currentQuestionList));
            selectedQuestions.clear();
        }
    }
}

