package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.AlertController;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class SimpleQuestionnaireExportService implements IQuestionnaireExportService{

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IQuestionnaireService iQuestionnaireService;
    private final AlertController alertController;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final IMainLerntiaService lerntiaService;

    @Autowired
    public SimpleQuestionnaireExportService(
        IMainLerntiaService lerntiaService,
        IQuestionnaireService iQuestionnaireService,
        AlertController alertController,
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService
    )
    {
        this.iQuestionnaireService = iQuestionnaireService;
        this.alertController = alertController;
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.lerntiaService = lerntiaService;
    }
    @Override
    public void exportSelectedQuestionnaire() {
        try {
            List<Question> allQuestions = getAllData(simpleLearningQuestionnaireService.getSelected());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> getAllData(LearningQuestionnaire selectedLearningQuestionnaire) {
        LOG.info("Unselect all the Other Questionnaire");
        try {
            iQuestionnaireService.deselectAllQuestionnaires();
        } catch (ServiceException e) {
            alertController.showStandardAlert(Alert.AlertType.ERROR, "Fragebogen vergessen fehlgeschlagen",
                "Fehler!", "Der zuvor ausgewählte Fragebogen konnte nicht vergessen werden.");
        }

        //Select the Database (Select the Questionnaire which is Selected from the User)
        LOG.info("Select the Questionnaire");
        try {
            simpleLearningQuestionnaireService.select(selectedLearningQuestionnaire);
            //lerntiaMainController.getAndShowTheFirstQuestion();
        } catch (ServiceException e) {
            LOG.error("Can't select Questionnaire");
        }// catch (ControllerException e) {
        // e.printStackTrace();
        //}

        try {
            lerntiaService.loadQuestionnaireAndGetFirstQuestion();
            lerntiaService.getFirstQuestion();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return lerntiaService.getQuestionList();
    }
}
