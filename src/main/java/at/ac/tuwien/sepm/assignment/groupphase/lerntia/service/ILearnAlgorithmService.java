package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;

import java.util.List;
import java.util.Map;

/**
 * The LearnAlgorithmService Class will handle the majority of calculation and value-management of the learn algorithm.
 * It will handle the successValue , failureValue and pointValue update. It will also sort all questions there are with the questions
 * with the lowest going up.
 * */
public interface ILearnAlgorithmService {

    /**
     * Update the Success quota (between 0 and 10) of an existing Question. After an Question has been correctly answered the quota of it
     * appearing again will be diminished in order to have other, less known Questions shown to the User. If the success rate is higher it will slowly
     * minimize the current failureValue of the question so the value gets a higher point. At the end of the update it will calculate a new value
     * (subtract failureValue from successValue or only 1 point if the failureValue is higher than the successValue)
     *
     * @param question The question which success quota has to be updated.
     * @throws ServiceException if the method can't update the success value
     * */
    void updateSuccessValue(Question question) throws ServiceException;

    /**
     * Update the Failure quota (between 0 and 10) of an existing Question. After a Question has been falsely answered the quota of it
     * appearing again will be raised in order help the User learn this Question better. If the failure rate is higher than the success rate
     * it will slowly minimize the successValue of the question so the value gets more negative points. At the end of the update it will calculate a new value
     * (subtract successValue from failureValue or only 1 negative point if the successValue is higher than the successValue.)
     *
     * @param question The question which failure quota has to be updated.
     * @throws ServiceException if the method can't update the failure value
     * */
    void updateFailureValue(Question question) throws ServiceException;

    /**
     * Prepare the Values of a Questionnaire for the Algorithm and sort the questions so that the questions with the lowest values start first
     *
     * @param questionAlgorithmList list of questions
     * @return List with the sorted IDs of questions
     * @throws ServiceException if the method can't sort or retrieve the List.
     *
     * */
    List<Long> prepareQuestionValues(List<QuestionLearnAlgorithm> questionAlgorithmList) throws ServiceException;

    List<Long> sortValueMap(Map<Long, Double> valueMap);

    /**
     * Change the Values of Questions in the Database when another questionnaire has been selected or the program shuts down.
     *
     * @throws ServiceException if the method can't update those values and cant send them to the DAO layer.
     * */
    void changeAlgorithmValues()throws ServiceException;

    /**
     * Updates the values of the current questionnaire so those values do not get lost
     *
     * @throws ServiceException if the method can't update those values and cant send them to the DAO layer.
     * */
    void shutdown() throws ServiceException;
}