package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;

import java.util.List;

public interface ILearningQuestionnaireService {
    /**
     * Create a new LearningQuestionnaire and save it to the Database
     *
     * @param learningQuestionnaire in question that needs to be saved to the Database
     * @throws ServiceException if the method can't save the LearningQuestionnaire to the Database
     * */
    void create(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Update an existing LearningQuestionnaire with new parameters to the Database
     *
     * @param learningQuestionnaire LearningQuestionnaire with updated values
     * @throws ServiceException if the LearningQuestionnaire can't be updated
     * */
    void update(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Search for specific LearningQuestionnaire with specific parameters in the Database
     *
     * @param learningQuestionnaire LearningQuestionnaire with searchparameters
     * @throws ServiceException if the method can't render or there is an error while searching
     * */
    void search(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Delete LearningQuestionnaire from the Database
     *
     * @param learningQuestionnaire LearningQuestionnaire that needs to be deleted
     * @throws ServiceException if the method can't delete the LearningQuestionnaire
     * */
    void delete(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Reads all LearningQuestionnaire from the Database
     *
     * @return List with all LearningQuestionnaire
     * @throws ServiceException  if it's not possible to get the List
     * */
    List readAll() throws ServiceException;
}
