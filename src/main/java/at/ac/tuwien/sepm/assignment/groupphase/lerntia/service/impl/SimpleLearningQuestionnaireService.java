package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearningQuestionnaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleLearningQuestionnaireService implements ILearningQuestionnaireService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ILearningQuestionnaireDAO iLearningQuestionnaireDAO;

    @Autowired
    public SimpleLearningQuestionnaireService(ILearningQuestionnaireDAO iLearningQuestionnaireDAO) {
        this.iLearningQuestionnaireDAO = iLearningQuestionnaireDAO;
    }

    @Override
    public void create(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.create(learningQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException("Persistence exception caught");
        }
    }

    @Override
    public void delete(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.delete(learningQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public List<LearningQuestionnaire> readAll() throws ServiceException {
        try {
            return iLearningQuestionnaireDAO.readAll();
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public void select(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.select(learningQuestionnaire);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public void deselect(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.deselect(learningQuestionnaire);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public LearningQuestionnaire getSelected() throws ServiceException {

        LearningQuestionnaire selectedLearningQuestionnaire;

        try {
            selectedLearningQuestionnaire = iLearningQuestionnaireDAO.getSelected();
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to get the selected questionnaire!");
        }

        return selectedLearningQuestionnaire;
    }
}
