package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;

import java.awt.*;

public interface IQuestionnaireQuestionService {

    /**
     * Create a new QuestionnaireQuestion entry for the Middle-Table in the Database
     *
     * @param questionnaireQuestion the QuestionnaireQuestion that needs to be saved
     * @throws ServiceException if the method can't save the entry
     * */
    void create(QuestionnaireQuestion questionnaireQuestion) throws ServiceException;

    /**
     * Delete a specific QuestionnaireQuestion entry in the Database
     *
     * @param questionnaireQuestion the QuestionnaireQuestion that needs to be deleted from the Database
     * @throws ServiceException if the method can't delete the QuestionnaireQuestion
     * */
    void delete(QuestionnaireQuestion questionnaireQuestion) throws ServiceException;

    /**
     * Update an existing QuestionnaireQuestion in the Database with new values
     *
     * @param questionnaireQuestion QuestionnaireQuestion that needs to be updated
     * @param newQid new ID from the Questionnaire
     * @param newQuestionid new Question ID
     * @throws ServiceException if the method can't update the QuestionnaireQuestion
     * */
    void update(QuestionnaireQuestion questionnaireQuestion, long newQid,long newQuestionid) throws ServiceException;

    /**
     * Read All QuesstionnaireQuestions from the Database
     *
     * @return List of all QuestionnaireQuestions
     * @throws ServiceException if the method can't read all elements from the Database
     * */
    List readAll() throws ServiceException;
}
