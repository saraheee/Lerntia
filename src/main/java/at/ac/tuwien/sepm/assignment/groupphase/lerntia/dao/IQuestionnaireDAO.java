package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;

import java.util.List;

public interface IQuestionnaireDAO {

    /**
     * Create a new Questionnaire and save it to the Database
     *
     * @param questionnaire Questionnaire that needs to be saved to the Database
     * @throws PersistenceException if the method can't save the Questionnaire to the Database
     * */
    void create(Questionnaire questionnaire) throws PersistenceException;

    /**
     * Update an existing Questionnaire in the Database
     *
     * @param questionnaire the questionnaire in question that needs to be updated in the Database
     * @throws PersistenceException if the method can't update the Questionnaire
     * */
    void update(Questionnaire questionnaire) throws PersistenceException;

    String getQuestionnaireName(Long id) throws PersistenceException;

    void select(Questionnaire questionnaire) throws PersistenceException;

    void deselect(Questionnaire questionnaire) throws PersistenceException;

    Questionnaire getSelected() throws PersistenceException;
}
