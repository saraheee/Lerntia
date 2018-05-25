package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;

import java.util.List;

public interface ILearnAlgorithmDAO {

    /**
     * Create new DB question input for the Learnalgorithm
     *
     * @param questionLearnAlgorithm success and failure value of a specific question to be saved
     * @throws PersistenceException if the DAO can't save those values to the DB
     * */
    void create(QuestionLearnAlgorithm questionLearnAlgorithm)throws PersistenceException;

    /**
     * Update existing algorithm values of an question.
     *
     * @param questionLearnAlgorithm the question with new saved values
     * @throws PersistenceException if the DAO can't update the Algorithm values of the Question
     * */
    void update(List<QuestionLearnAlgorithm> questionLearnAlgorithm) throws PersistenceException;

    /**
     * Delete an entry from the Database linked to a given question
     *
     * @param questionLearnAlgorithm values from a question that need to be deleted
     * @throws PersistenceException if the method can't delete the Algorithm values of the question.
     * */
    void delete(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException;


    /**
     * Read all values from all existing questions from the Database
     *
     * @return List of all values from all questions from the Database
     * @throws PersistenceException if the method can't retireve the List
     * */
    List<QuestionLearnAlgorithm> readAll() throws PersistenceException;

    List<QuestionLearnAlgorithm> search(List<QuestionLearnAlgorithm> questionAlgorithmList) throws PersistenceException;

    void reset(QuestionLearnAlgorithm questionLearnAlgorithm)throws PersistenceException;
}


