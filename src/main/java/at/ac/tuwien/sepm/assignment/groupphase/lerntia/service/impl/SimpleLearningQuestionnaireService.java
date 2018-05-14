package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearningQuestionnaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleLearningQuestionnaireService implements ILearningQuestionnaireService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ILearningQuestionnaireDAO iLearningQuestionnaireDAO;

    public SimpleLearningQuestionnaireService(ILearningQuestionnaireDAO iLearningQuestionnaireDAO){
        this.iLearningQuestionnaireDAO = iLearningQuestionnaireDAO;
    }

    @Override
    public void create(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.create(learningQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.update(learningQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void search(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.search(learningQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.delete(learningQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List readAll() throws ServiceException {
        try {
            return iLearningQuestionnaireDAO.readAll();
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void select(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.select(learningQuestionnaire);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deselect(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.deselect(learningQuestionnaire);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public LearningQuestionnaire getSelected() throws ServiceException {

        LearningQuestionnaire selectedLearningQuestionnaire = null;

        try {
            selectedLearningQuestionnaire = iLearningQuestionnaireDAO.getSelected();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return selectedLearningQuestionnaire;
    }


}
