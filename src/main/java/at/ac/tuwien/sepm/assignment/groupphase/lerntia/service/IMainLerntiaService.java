package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main service of the application. It does not directly access DAOs but rather other services.
 * It is the general home for business logic.
 */
public interface IMainLerntiaService {

    /**
     * Loads selected questionnaire and gets first question of that questionnaire
     *
     * @return first question of the newly selected Questionnaire
     * @throws ServiceException if the method can't get the questionnaire or select the first question
     */
    Question loadQuestionnaireAndGetFirstQuestion() throws ServiceException;

    /**
     * Get first question of the selected questionnaire on startup or when the questionnaire has been passed through
     *
     * @return first Question of the questionnaire
     * @throws ServiceException if the method can't select or get the first question
     */
    Question getFirstQuestion() throws ServiceException;

    /**
     * Sets the custom chosen questions from the Edit Exam Window in the questions List
     *
     * @param customList custom list of questions that are set for the exam.
     * @throws ServiceException if the method can't obtain the questions or/and can't set them into the question list.
     */
    void setCustomExamQuestions(ArrayList<Question> customList) throws ServiceException;

    /**
     * Standard exam-questionnaire question set method. Takes the questions linked with a specific exam-questionnaire
     * and sets those questions into the program
     *
     * @param eQ exam-questionnaire in question.
     * @throws ServiceException if the method can't obtain the questions or set them into the Question List
     */
    void getQuestionsFromExamQuestionnaire(ExamQuestionnaire eQ) throws ServiceException;

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
     * Returns the list of current questions
     *
     * @return List of the currently used questions.
     */
    List<Question> getQuestions();

    /**
     * Returns the QuestionList
     *
     * @return list of questions.
     */
    List<Question> getQuestionList();

    /**
     * Stops the Learning algorithm and sends the updated values to the DB before closing.
     *
     * @throws ServiceException if the learn algorithm shutdown encounters an error during shutdown or during the sending of the value package to the database
     */
    void stopAlgorithm() throws ServiceException;

    /**
     * Returns the number of correct answers of one questionnaire
     *
     * @return the number of correct answers of one questionnaire
     */
    int getCorrectAnswers();

    /**
     * Returns the number of wrong answers of one questionnaire
     *
     * @return number of wrong answers of one questionnaire
     */
    int getWrongAnswers();

    /**
     * Returns the number of questions of one questionnaire, which were not answered
     *
     * @return the number of questions of one questionnaire, which were not answered.
     */
    int getIgnoredAnswers();

    /**
     * Returns the percentage of correct answers of one questionnaire
     */
    double getPercent();

    /**
     * Set if the program has to rerun the questionnaire with only the wrongly answered questions or not.
     *
     * @param onlyWrongQuestions boolean flag which determines if it needs only wrong questions or all in general.
     */
    void setOnlyWrongQuestions(Boolean onlyWrongQuestions);

    /**
     * Restores all questions and returns the first question of the list. Used mostly after using the "wrong question list" option
     * and deciding to revert to all the questions from a questionnaire.
     *
     * @return first question of the restored list.
     */
    Question restoreQuestionsAndGetFirst();

    /**
     * Set the boolean value for the service layer that the exam mode is currently active or not.
     *
     * @param examMode boolean value which indicates if the program is currently in the Exam mode or not.
     */
    void setExamMode(boolean examMode);

    /**
     * Returns the first Question of an Exam Questionnaire
     *
     * @return first exam question
     * @throws ServiceException if the method can't obtain the first Exam Question.
     */
    Question getFirstExamQuestion() throws ServiceException;

    /**
     * Get the list of wrongly answered Questions
     *
     * @return List of all wrongly answered Questions.
     */
    List<Question> getWrongQuestionList();

    /**
     * Resets the statistic counters
     */
    void resetCounter();
}
