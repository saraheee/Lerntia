package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

/**
 * This is the main service of the application. It does not directly access DAOs but rather other services.
 * It is the general home for business logic.
 */
public interface IMainLerntiaService {

    Question getFirstQuestion() throws ServiceException;


    /**
     * Return the next question (in order) form the selected questionnaire
     *
     * @return the next question
     * @throws ServiceException if the method can't find the next Question. Possible reasons:
     *    - the end of questions list was reached
     *    - other implementation-specific reasons
     * */
    Question getNextQuestionFromList() throws ServiceException;

    /**
     * Return the previous question (in order) form the selected questionnaire
     *
     * @return the next question
     * @throws ServiceException if the method can't find the previous Question. Possible reasons:
     *    - the beginning of questions list was reached
     *    - other implementation-specific reasons
     * */
    Question getPreviousQuestionFromList()throws ServiceException;

    /**
     * Saves the selected answers for the purposes of statistics and learning algorithm
     *
     * @param mockQuestion a DTO containing only the question id and the list of selected answers in the field correctAnswers
     * @throws ServiceException if the method can't find the next Question. Possible reasons:
     *    - the selected answers was a null
     *    - the selected answers has an unexpected format
     *    - other implementation-specific reasons
     * */
    void recordCheckedAnswers(Question mockQuestion) throws ServiceException;
}
