package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;

import java.util.List;

public interface IQuestionnaireQuestionDAO {

    /**
     * Create a new QuestionnaireQuestion entry for the Middle-Table in the Database
     *
     * @param questionnaireQuestion the QuestionnaireQuestion that needs to be saved
     * @throws PersistenceException if the method can't save the entry
     */
    void create(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException;

    /**
     * Searches for specific QuestionnaireQuestions with given parameters
     *
     * @param searchParameters for the QuestionnaireQuestions we want to find
     * @return list of all QuestionnaireQuestion matching the searchParameters
     * @throws PersistenceException if the list cannot be retrieved
     */
    List<QuestionnaireQuestion> search(QuestionnaireQuestion searchParameters) throws PersistenceException;

    /**
     * Delete a specific QuestionnaireQuestion entry in the Database
     *
     * @param questionnaireQuestion the QuestionnaireQuestion that needs to be deleted from the Database
     * @throws PersistenceException if the method can't delete the QuestionnaireQuestion
     */
    void delete(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException;

    /**
     * Update an existing QuestionnaireQuestion in the Database with new values
     *
     * @param questionnaireQuestion QuestionnaireQuestion that needs to be updated
     * @param newQid                new ID from the Questionnaire
     * @param newQuestionId         new Question ID
     * @throws PersistenceException if the method can't update the QuestionnaireQuestion
     */
    void update(QuestionnaireQuestion questionnaireQuestion, long newQid, long newQuestionId) throws PersistenceException;

    /**
     * Read All QuestionnaireQuestions from the Database
     *
     * @return List of all QuestionnaireQuestions
     * @throws PersistenceException if the method can't read all elements from the Database
     */
    List<QuestionnaireQuestion> readAll() throws PersistenceException;

}
