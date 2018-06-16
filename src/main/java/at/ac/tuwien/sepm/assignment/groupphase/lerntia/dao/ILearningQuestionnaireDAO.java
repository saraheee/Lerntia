package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;


import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;

import java.util.List;

public interface ILearningQuestionnaireDAO {

    /**
     * Create a new LearningQuestionnaire and save it to the Database
     *
     * @param learningQuestionnaire in question that needs to be saved to the Database
     * @throws PersistenceException if the method can't save the LearningQuestionnaire to the Database
     */
    void create(LearningQuestionnaire learningQuestionnaire) throws PersistenceException;

    /**
     * Delete LearningQuestionnaire from the Database
     *
     * @param learningQuestionnaire LearningQuestionnaire that needs to be deleted
     * @throws PersistenceException if the method can't delete the LearningQuestionnaire
     */
    void delete(LearningQuestionnaire learningQuestionnaire) throws PersistenceException;

    /**
     * Reads all LearningQuestionnaire from the Database
     *
     * @return List with all LearningQuestionnaire
     * @throws PersistenceException if it's not possible to get the List
     */
    List<LearningQuestionnaire> readAll() throws PersistenceException;

    /**
     * Select an LearningQuestionnaire to be used
     *
     * @param learningQuestionnaire the LearningQuestionnaire to be selected
     * @throws PersistenceException if the LearningQuestionnaire cannot be selected in the Database
     */
    void select(LearningQuestionnaire learningQuestionnaire) throws PersistenceException;

    /**
     * Deselect an LearningQuestionnaire that was previously used
     *
     * @param learningQuestionnaire the LearningQuestionnaire to be deselected
     * @throws PersistenceException if the LearningQuestionnaire cannot be deselected in the Database
     */
    void deselect(LearningQuestionnaire learningQuestionnaire) throws PersistenceException;

    /**
     * Get the selected LearningQuestionnaire from the Database
     *
     * @return the selected LearningQuestionnaire
     * @throws PersistenceException if the selected LearningQuestionnaire cannot be retrieved from the Database
     */
    LearningQuestionnaire getSelected() throws PersistenceException;
}
