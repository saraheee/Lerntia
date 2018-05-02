package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;

public interface ILearningQuestionnaireDAO {
    void create(LearningQuestionnaire learningQuestionnaire) throws PersistenceException;

    void update(LearningQuestionnaire learningQuestionnaire) throws PersistenceException;
}
