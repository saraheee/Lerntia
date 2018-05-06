package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

public interface IQuestionService {

    long create(Question question) throws PersistenceException;

    /**
     * Update an existing Question in the Database with new Values
     *
     * @param question the Question with the updated values
     * @throws PersistenceException if the method can't update the Question
     * */
    void update(Question question) throws PersistenceException;

    /**
     * Search for Question with specific parameters
     *
     * @param question Question with searchparameters
     * @throws PersistenceException if the method can't search for the question
     * */
    void search(Question question) throws PersistenceException;

    /**
     * Delete Question from the Database
     *
     * @param question Question that needs to be deleted from the Database
     * @throws PersistenceException if the method can't delete the Question
     * */
    void delete(Question question) throws PersistenceException;

    /**
     * Get Question with given question ID
     *
     * @param id of the question
     * @return returns Question which matches with the ID
     * @throws PersistenceException if the method can't get the Question
     * */
    Question get(long id) throws PersistenceException;

}
