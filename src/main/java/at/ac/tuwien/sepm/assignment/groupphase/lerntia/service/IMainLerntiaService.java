package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

import java.util.List;

/**
 * This is the main service of the application. It does not directly access DAOs but rather other services.
 * It is the general home for business logic.
 */
public interface IMainLerntiaService {

    /**
     * Loads selected Questionnaire and gets first question of that questionnaire
     *
     * @return first question of the newly selected Questionnaire
     * @throws ServiceException if the method can't get the questionnaire or select the first question
     */
    Question loadQuestionnaireAndGetFirstQuestion() throws ServiceException;

    /**
     * Get first question of the selected Questionnaire on startup or when the questionnaire has been passed through
     *
     * @return first Question of the questionnaire
     * @throws ServiceException if the method can't select or get the first question
     * */
    Question getFirstQuestion() throws ServiceException;

    /**
     * Return the next question (in order) form the selected questionnaire
     *
     * @return the next question
     * @throws ServiceException if the method can't find the next Question. Possible reasons:
     *                          - the end of the question list was reached
     *                          - other implementation-specific reasons
     */
    Question getNextQuestionFromList() throws ServiceException;

    /**
     * Return the previous question (in order) form the selected questionnaire
     *
     * @return the next question
     * @throws ServiceException if the method can't find the previous Question. Possible reasons:
     *                          - the beginning of the question list was reached
     *                          - other implementation-specific reasons
     */
    Question getPreviousQuestionFromList() throws ServiceException;

    /**
     * Saves the selected answers for the purposes of statistics and learning algorithm
     *
     * @param question a DTO containing only the question id and the list of selected answers in the field correctAnswers
     * @throws ServiceException if the method can't find the next Question. Possible reasons:
     *                          - the selected answers are null
     *                          - the selected answers have an unexpected format
     *                          - other implementation-specific reasons
     */
    void recordCheckedAnswers(Question question, boolean answersCorrect) throws ServiceException;

    /**
     *
     * Returns the list of current questions
     *
     * @throws ServiceException if the method can't get the current list of questions
     * @return List of the currently used questions.
     */
    List<Question> getQuestions() throws ServiceException;

    /**
     * Returns the Size of the ListCounter
     */
    int getListCounter();

    /**
     * Returns the QuestionList
     */
    List<Question> getQuestionList();


    /**
     * Stops the Learning algorithm and sends the updated values to the DB before closing.
     *
     * @throws ServiceException if the Learnalgorithm shutdown encounters an error during shutdown or during the sending of the value package to the Database
     * */
    void stopAlgorithm() throws ServiceException;

    /**
     * Returns the number of correct answers of one questionnaire
     */
    int getCorrectAnswers();

    /**
     * Returns the number of wrong answers of one questionnaire
     */
    int getWrongAnswers();

    /**
     * Returns the percentage of correct answers of one questionnaire
     */
    double getPercent();

    /**
     * Set if the programm has to rerun the questionnaire with only the wrongly answered questions or not.
     *
     * @param onlyWrongQuestions boolean flag which determines if it needs only wrong questions or all in general.
     */
    void setOnlyWrongQuestions(Boolean onlyWrongQuestions);
}
