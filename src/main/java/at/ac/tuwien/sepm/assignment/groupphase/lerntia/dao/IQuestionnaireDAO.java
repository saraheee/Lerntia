package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;

public interface IQuestionnaireDAO {
    void create(Questionnaire questionnaire) throws PersistenceException;

    void update(Questionnaire questionnaire) throws PersistenceException;
}
