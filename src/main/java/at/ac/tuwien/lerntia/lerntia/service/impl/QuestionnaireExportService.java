package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.exception.ServiceValidationException;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireExportDAO;
import at.ac.tuwien.lerntia.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionnaireExportService;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.dto.Question;
import at.ac.tuwien.lerntia.lerntia.service.ILearningQuestionnaireService;
import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class QuestionnaireExportService implements IQuestionnaireExportService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IQuestionnaireService iQuestionnaireService;
    private final ILearningQuestionnaireService iLearningQuestionnaireService;
    private final IMainLerntiaService lerntiaService;
    private final IQuestionnaireExportDAO iQuestionnaireExportDAO;

    @Autowired
    public QuestionnaireExportService(
        IMainLerntiaService lerntiaService,
        IQuestionnaireService iQuestionnaireService,
        ILearningQuestionnaireService simpleLearningQuestionnaireService,
        IQuestionnaireExportDAO iQuestionnaireExportDAO) {
        this.iQuestionnaireService = iQuestionnaireService;
        this.iLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.lerntiaService = lerntiaService;
        this.iQuestionnaireExportDAO = iQuestionnaireExportDAO;
    }

    @Override
    public void exportSelectedQuestionnaire(LearningQuestionnaire questionnaire) throws ServiceException, ServiceValidationException {
        try {
            iQuestionnaireExportDAO.exportQuestionnaire(questionnaire, getAllData(iLearningQuestionnaireService.getSelected()));
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to export file.");

        } catch (FileExistsException e) {
            throw new ServiceValidationException("Validation failed. File already exists.");
        }
    }

    @Override
    public List<Question> getAllData(LearningQuestionnaire selectedLearningQuestionnaire) throws ServiceException {
        LOG.info("Deselect all the other questionnaires");
        iQuestionnaireService.deselectAllQuestionnaires();

        //Select the Questionnaire which is Selected from the User
        LOG.info("Select the questionnaire");
        iLearningQuestionnaireService.select(selectedLearningQuestionnaire);
        lerntiaService.loadQuestionnaireAndGetFirstQuestion();
        lerntiaService.getFirstQuestion();
        return lerntiaService.getQuestionList();
    }

    @Override
    public void overwriteFile(LearningQuestionnaire questionnaire) throws ServiceException, ServiceValidationException {
        try {
            iQuestionnaireExportDAO.overwriteFile(questionnaire, getAllData(iLearningQuestionnaireService.getSelected()));
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to export file.");

        } catch (FileExistsException e) {
            throw new ServiceValidationException("Validation failed. File already exists.");
        }
    }

}
