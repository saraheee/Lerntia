package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;

import java.util.List;

public class SimpleLearningQuestionnaireService implements ILearningQuestionnaireService {
    @Override
    public long create(LearningQuestionnaire learningQuestionnaire) throws ServiceException {
        return 0;
    }

    @Override
    public void update(LearningQuestionnaire learningQuestionnaire) throws ServiceException {

    }

    @Override
    public void search(LearningQuestionnaire learningQuestionnaire) throws ServiceException {

    }

    @Override
    public void delete(LearningQuestionnaire learningQuestionnaire) throws ServiceException {

    }

    @Override
    public List readAll() throws ServiceException {
        return null;
    }
}
