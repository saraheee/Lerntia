package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireExportService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.AlertController;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class SimpleQuestionnaireExportService implements IQuestionnaireExportService {

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
    public void exportSelectedQuestionnaire(String fileName) {
        List<Question> allQuestions = null;
        try {
            allQuestions = getAllData(simpleLearningQuestionnaireService.getSelected());
        } catch (ServiceException e) {
            e.printStackTrace();
        }


        //Creating the Export file.
        String csvFile = null;
        FileWriter writer = null;
        try {
            csvFile = new File(".").getCanonicalPath();
            LOG.info("csvFile: "+csvFile);
            writer = new FileWriter(csvFile+"/"+fileName+".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Getting all the Questions
        String csvOutput = "";
        for(Question q: allQuestions){
            csvOutput += q.getQuestionText()+";"+q.getAnswer1()+";"+q.getAnswer2()+";"+q.getAnswer3()+";"+q.getAnswer4()+";"+q.getAnswer5()+";"+q.getCorrectAnswers()+";"+
                q.getPicture()+";\n";
        }
        try {
            writer.append(csvOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Flush & Close
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
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
        } catch (ServiceException e) {
            LOG.error("Can't select Questionnaire");
        }

        try {
            lerntiaService.loadQuestionnaireAndGetFirstQuestion();
            lerntiaService.getFirstQuestion();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return lerntiaService.getQuestionList();
    }
}
