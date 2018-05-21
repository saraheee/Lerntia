package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;

import java.util.List;

public interface ILearningQuestionnaireService {

    /**
     * Create a new LearningQuestionnaire and save it
     *
     * @param learningQuestionnaire in question that needs to be saved
     * @throws ServiceException if the method can't save the LearningQuestionnaire
     * */
    void create(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Update an existing LearningQuestionnaire with new parameters
     *
     * @param learningQuestionnaire LearningQuestionnaire with updated values
     * @throws ServiceException if the LearningQuestionnaire can't be updated
     * */
    void update(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Search for specific LearningQuestionnaire with specific parameters
     *
     * @param learningQuestionnaire LearningQuestionnaire with searchparameters
     * @throws ServiceException if the method can't render or there is an error while searching
     * */
    void search(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Delete LearningQuestionnaires
     *
     * @param learningQuestionnaire LearningQuestionnaire that needs to be deleted
     * @throws ServiceException if the method can't delete the LearningQuestionnaire
     * */
    void delete(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Reads all LearningQuestionnaires
     *
     * @return List with all LearningQuestionnaire
     * @throws ServiceException  if it's not possible to get the List
     * */
    List readAll() throws ServiceException;

    /**
     * Select an LearningQuestionnaire to be used
     *
     * @param learningQuestionnaire the LearningQuestionnaire to be selected
     * @throws ServiceException if the LearningQuestionnaire cannot be selected
     * */
    void select(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Deselect an LearningQuestionnaire that was previously used
     *
     * @param learningQuestionnaire the LearningQuestionnaire to be deselected
     * @throws ServiceException if the LearningQuestionnaire cannot be deselected
     * */
    void deselect(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Get the selected LearningQuestionnaire
     *
     * @return the selected LearningQuestionnaire
     * @throws ServiceException if the selected LearningQuestionnaire cannot be retrieved
     * */
    LearningQuestionnaire getSelected() throws ServiceException;
}
