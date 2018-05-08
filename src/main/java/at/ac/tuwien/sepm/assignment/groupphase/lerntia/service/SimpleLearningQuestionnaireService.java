package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.LearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleLearningQuestionnaireService implements ILearningQuestionnaireService {

    private final ILearningQuestionnaireDAO iLearningQuestionnaireDAO;

    public SimpleLearningQuestionnaireService(ILearningQuestionnaireDAO iLearningQuestionnaireDAO){
        this.iLearningQuestionnaireDAO = iLearningQuestionnaireDAO;
    }

    @Override
    public void create(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.create(learningQuestionnaire);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.update(learningQuestionnaire);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void search(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.search(learningQuestionnaire);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        try {
            iLearningQuestionnaireDAO.delete(learningQuestionnaire);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List readAll() throws ServiceException {
        return null;
    }
}
