package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ILearnAlgorithmDAO {

    /**
     * Create new DB question input for the learn algorithm
     *
     * @param questionLearnAlgorithm success and failure value of a specific question to be saved
     * @throws PersistenceException if the DAO can't save those values to the DB
     */
    void create(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException;

    /**
     * Update existing algorithm values of an question.
     *
     * @param questionLearnAlgorithm the question with new saved values
     * @throws PersistenceException if the DAO can't update the Algorithm values of the Question
     */
    void update(List<QuestionLearnAlgorithm> questionLearnAlgorithm) throws PersistenceException;

    /**
     * Delete an entry from the Database linked to a given question
     *
     * @param questionLearnAlgorithm values from a question that need to be deleted
     * @throws PersistenceException if the method can't delete the Algorithm values of the question.
     */
    void delete(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException;


    /**
     * Read all values from all existing questions from the Database
     *
     * @return List of all values from all questions from the Database
     * @throws PersistenceException if the method can't retrieve the List
     */
    List<QuestionLearnAlgorithm> readAll() throws PersistenceException;

    /**
     * Helper method that puts the result from ResultSet into the Results list.
     *
     * @param rsReadAll   ResultSet from the read all or search methods of the LearnAlgorithmDAO
     * @param readResults the List of results which the values of the result set need to be inserted
     * @throws SQLException if the method can't transfer the values from the result set to the list
     */
    void getResults(ResultSet rsReadAll, List<QuestionLearnAlgorithm> readResults) throws SQLException;

    /**
     * Search for all specific algorithm values of specified questions.
     *
     * @param questionAlgorithmList search parameters on which values of what questions to extract
     * @return List of all question algorithm values needed.
     * @throws PersistenceException if the method can't find all values specified by the search parameters.
     */
    List<QuestionLearnAlgorithm> search(List<QuestionLearnAlgorithm> questionAlgorithmList) throws PersistenceException;

    /**
     * Reset the values of a specific questions (in case a question changes or was updated)
     *
     * @param questionLearnAlgorithm the question which values need to be reset.
     * @throws PersistenceException if the method can't reset the values of the given question.
     */
    void reset(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException;
}


