package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;

import java.awt.*;

public interface IQuestionnaireQuestionDAO {

    void create(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException;
    void delete(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException;
    void update(QuestionnaireQuestion questionnaireQuestion, long newQid,long newQuestionid) throws PersistenceException;
    List readAll(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException;

}
