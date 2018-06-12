package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceValidationException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireExportDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireExportService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class SimpleQuestionnaireExportService implements IQuestionnaireExportService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IQuestionnaireService iQuestionnaireService;
    private final SimpleLearningQuestionnaireService simpleLearningQuestionnaireService;
    private final IMainLerntiaService lerntiaService;
    private final IQuestionnaireExportDAO iQuestionnaireExportDAO;

    @Autowired
    public SimpleQuestionnaireExportService(
        IMainLerntiaService lerntiaService,
        IQuestionnaireService iQuestionnaireService,
        SimpleLearningQuestionnaireService simpleLearningQuestionnaireService,
        IQuestionnaireExportDAO iQuestionnaireExportDAO) {
        this.iQuestionnaireService = iQuestionnaireService;
        this.simpleLearningQuestionnaireService = simpleLearningQuestionnaireService;
        this.lerntiaService = lerntiaService;
        this.iQuestionnaireExportDAO = iQuestionnaireExportDAO;
    }

    @Override
    public void exportSelectedQuestionnaire(LearningQuestionnaire questionnaire) throws ServiceException, ServiceValidationException {
        try {
            iQuestionnaireExportDAO.exportQuestionnaire(questionnaire, getAllData(simpleLearningQuestionnaireService.getSelected()));
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to export file.");

        } catch (FileExistsException e) {
            throw new ServiceValidationException("Validation failed. File already exists.");
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

    @Override
    public void overwriteFile(LearningQuestionnaire questionnaire) throws ServiceException, ServiceValidationException {
        try {
            iQuestionnaireExportDAO.overwriteFile(questionnaire, getAllData(simpleLearningQuestionnaireService.getSelected()));
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to export file.");

        } catch (FileExistsException e) {
            throw new ServiceValidationException("Validation failed. File already exists.");
        }
    }

}
