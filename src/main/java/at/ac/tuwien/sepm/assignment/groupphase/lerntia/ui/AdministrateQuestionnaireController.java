package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleLearningQuestionnaireService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Controller
public class AdministrateQuestionnaireController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final SelectQuestionAdministrateController selectQuestionAdministrateController;
    private final IMainLerntiaService lerntiaService;
    private List<LearningQuestionnaire> learningQuestionnaires;

    public AdministrateQuestionnaireController(
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
        SelectQuestionAdministrateController selectQuestionAdministrateController,
        IMainLerntiaService lerntiaService) {
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.selectQuestionAdministrateController = selectQuestionAdministrateController;
        this.lerntiaService = lerntiaService;
    }

    @FXML
    public void initialize() {
        try {
            this.learningQuestionnaires = simpleLearningQuestionnaireService.readAll();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < learningQuestionnaires.size(); i++) {
            cb_questionnaire.getItems().add(learningQuestionnaires.get(i).getName());
        }
    }


    @FXML
    public ChoiceBox cb_questionnaire;

    @FXML
    public void selectQuestionnaire(ActionEvent actionEvent) {
        //Get the Selected Item.
        LearningQuestionnaire selectedLearningQuestionnaire = learningQuestionnaires.get(cb_questionnaire.getSelectionModel().getSelectedIndex());
        LearningQuestionnaire studyMode = null;
        try {
            studyMode = simpleLearningQuestionnaireService.getSelected();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        //Unselect all the Other Questionnaire
        for (int i = 0; i < learningQuestionnaires.size(); i++) {
            try {
                simpleLearningQuestionnaireService.deselect(learningQuestionnaires.get(i));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

        //Select the Questionnaire
        try {
            simpleLearningQuestionnaireService.select(selectedLearningQuestionnaire);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //Opens the New Window which contains a TableView and all Questions.
        selectQuestionAdministrateController.showSelectQuestionAdministrateWindow();
        try {
            simpleLearningQuestionnaireService.deselect(selectedLearningQuestionnaire);
            simpleLearningQuestionnaireService.select(studyMode);
            lerntiaService.getFirstQuestion();
            //Delete this Line if not Needed
            LOG.info("Study: "+studyMode.getName()+" Selected: "+selectedLearningQuestionnaire.getName());

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //Todo Close the Current window
    }



    /**
     * Opens the first Window in the AdministrateQuestionnare operation.
     * Opens a window in which the user is allowed to Chooces a Questionnare.
     * The Code is taken from SelectQuestionnaireController
     */
    public void showAdministrateQuestionnaireWindow(){
        var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/views/administrateQuestionnaire.fxml"));
        var stage = new Stage();

        try {
            fxmlLoader.setControllerFactory(param -> param.isInstance(this) ? this : null);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("[Lerntia] Fragebogen verwalten");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            LOG.debug("Successfully opened a window for administrating a questionnaire.");
        } catch (IOException e) {
            LOG.error("Failed to open a window for administrating a questionnaire. " + e.getMessage());
        }
    }

}
