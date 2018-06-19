package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

import java.util.ArrayList;
import java.util.List;

public interface IQuestionService {

    void create(Question question) throws ServiceException;

    /**
     * Update an existing Question in the Database with new Values
     *
     * @param question the Question with the updated values
     * @throws ServiceException if the method can't update the Question
     */
    void update(Question question) throws ServiceException;

    /**
     * Search for Question with specific parameters
     *
     * @param questionList Questions with search parameters
     * @return list of questions that were found
     * @throws ServiceException if the method can't search for the question
     */

    List<Question> search(List<Question> questionList) throws ServiceException;

    /**
     * Delete Question from the Database
     *
     * @param question Question that needs to be deleted from the Database
     * @throws ServiceException if the method can't delete the Question
     */
    void delete(Question question) throws ServiceException;

    /**
     * Get Question with given question ID
     *
     * @param id of the question
     * @return returns Question which matches with the ID
     * @throws ServiceException if the method can't get the Question
     */
    Question get(long id) throws ServiceException;

    /**
     * Get all answers of the corresponding Question
     *
     * @param question the Question for which the answers should be retrieved
     * @return a list of all the answers of the corresponding Question
     */
    ArrayList<String> getAllAnswers(Question question);

    /**
     * Validate a Question
     *
     * @param question the Question to be validated
     * @throws ServiceException if the Question failed the validation
     */
    void validate(Question question) throws ServiceException;

    /**
     * @param questionInput contains a part of a question that is used to find the questions
     * @return list which contains all the Question that contain a part of the questionInput
     * @throws ServiceException if the question failed the validation
     *                          or if the appropriate config file ('questions.properties') is not provided
     */
    List<Question> searchForQuestions(Question questionInput) throws ServiceException;

}
