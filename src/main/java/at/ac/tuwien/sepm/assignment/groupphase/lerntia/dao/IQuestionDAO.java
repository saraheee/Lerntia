package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

import java.util.List;

public interface IQuestionDAO {

    /**
     * Create new Question and save it to the Database
     *
     * @param question that is going to be sent to the Database
     * @throws PersistenceException if the Question can't be saved to the Database
     */
    void create(Question question) throws PersistenceException;

    /**
     * Update an existing Question in the Database with new Values
     *
     * @param question the Question with the updated values
     * @throws PersistenceException if the method can't update the Question
     */
    void update(Question question) throws PersistenceException;

    /**
     * Search for Question with specific parameters
     *
     * @param questionList Question with search parameters
     * @return List of found questions
     * @throws PersistenceException if the method can't search for the question
     */

    List<Question> search(List<Question> questionList) throws PersistenceException;

    /**
     * Delete Question from the Database
     *
     * @param question Question that needs to be deleted from the Database
     * @throws PersistenceException if the method can't delete the Question
     */
    void delete(Question question) throws PersistenceException;

    /**
     * Get Question with given question ID
     *
     * @param id of the question
     * @return returns Question which matches with the ID
     * @throws PersistenceException if the method can't get the Question
     */
    Question get(long id) throws PersistenceException;

    /**
     * @param questionInput contains a part of a Question that is used to find the Questions
     * @return list which contains all the Question that contain a part of the questionInput
     */
    List<Question> searchForQuestions(Question questionInput) throws PersistenceException;
}
