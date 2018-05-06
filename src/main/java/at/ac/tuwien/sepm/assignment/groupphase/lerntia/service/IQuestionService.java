package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

public interface IQuestionService {

    long create(Question question) throws ServiceException;

    /**
     * Update an existing Question in the Database with new Values
     *
     * @param question the Question with the updated values
     * @throws ServiceException if the method can't update the Question
     * */
    void update(Question question) throws ServiceException;

    /**
     * Search for Question with specific parameters
     *
     * @param question Question with searchparameters
     * @throws ServiceException if the method can't search for the question
     * */
    void search(Question question) throws ServiceException;

    /**
     * Delete Question from the Database
     *
     * @param question Question that needs to be deleted from the Database
     * @throws ServiceException if the method can't delete the Question
     * */
    void delete(Question question) throws ServiceException;

    /**
     * Get Question with given question ID
     *
     * @param id of the question
     * @return returns Question which matches with the ID
     * @throws ServiceException if the method can't get the Question
     * */
    Question get(long id) throws ServiceException;

}
