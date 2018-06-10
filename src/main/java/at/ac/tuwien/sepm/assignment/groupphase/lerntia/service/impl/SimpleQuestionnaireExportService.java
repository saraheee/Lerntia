package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireExportService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class SimpleQuestionnaireExportService implements IQuestionnaireExportService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IQuestionnaireService iQuestionnaireService;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final IMainLerntiaService lerntiaService;

    @Autowired
    public SimpleQuestionnaireExportService(
        IMainLerntiaService lerntiaService,
        IQuestionnaireService iQuestionnaireService,
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService
    ) {
        this.iQuestionnaireService = iQuestionnaireService;
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.lerntiaService = lerntiaService;
    }

    @Override
    public void exportSelectedQuestionnaire(String fileName) throws ServiceException {
        List<Question> allQuestions;
        allQuestions = getAllData(simpleLearningQuestionnaireService.getSelected());

        //Creating the Export file.
        String csvFile;
        FileWriter writer;
        try {
            csvFile = new File(".").getCanonicalPath();
            LOG.info("csvFile: " + csvFile);
            writer = new FileWriter(csvFile + "/" + fileName + ".csv");
        } catch (IOException e) {
            LOG.error("Failed to create the export file.");
            throw new ServiceException("Failed to create the CSV export file.");
        }
        //Getting all the Questions
        var csvOutput = new StringBuilder();
        if (allQuestions != null) {
            for (var q : allQuestions) {
                csvOutput.append(q.getQuestionText()).append(";")
                    .append(q.getAnswer1()).append(";")
                    .append(q.getAnswer2()).append(";")
                    .append(q.getAnswer3()).append(";")
                    .append(q.getAnswer4()).append(";")
                    .append(q.getAnswer5()).append(";")
                    .append(q.getCorrectAnswers()).append(";")
                    .append(q.getPicture()).append(";")
                    .append(q.getOptionalFeedback()).append(";\n");
            }
        }
        try {
            writer.append(csvOutput.toString());
        } catch (IOException e) {
            LOG.error("Failed to write data to the export file.");
            throw new ServiceException("Failed to write all questions for the CSV export.");
        }
        //Flush & Close
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOG.error("Failed to flush and close the writer for the export file.");
            throw new ServiceException("Failed to close the writer for the CSV export.");
        }
    }

    @Override
    public List<Question> getAllData(LearningQuestionnaire selectedLearningQuestionnaire) throws ServiceException {
        LOG.info("Unselect all the Other Questionnaire");
        iQuestionnaireService.deselectAllQuestionnaires();

        //Select the Questionnaire which is Selected from the User
        LOG.info("Select the Questionnaire");
        simpleLearningQuestionnaireService.select(selectedLearningQuestionnaire);
        lerntiaService.loadQuestionnaireAndGetFirstQuestion();
        lerntiaService.getFirstQuestion();
        return lerntiaService.getQuestionList();
    }
}
