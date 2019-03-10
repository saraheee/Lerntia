package at.ac.tuwien.lerntia.lerntia.service;

import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.exception.ServiceException;

import java.util.List;

public interface ILearningQuestionnaireService {

    /**
     * Create a new LearningQuestionnaire and save it
     *
     * @param learningQuestionnaire in question that needs to be saved
     * @throws ServiceException if the method can't save the LearningQuestionnaire
     */
    void create(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Reads all LearningQuestionnaires
     *
     * @return List with all LearningQuestionnaire
     * @throws ServiceException if it's not possible to get the List
     */
    List<LearningQuestionnaire> readAll() throws ServiceException;

    /**
     * Select an LearningQuestionnaire to be used
     *
     * @param learningQuestionnaire the LearningQuestionnaire to be selected
     * @throws ServiceException if the LearningQuestionnaire cannot be selected
     */
    void select(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Deselect an LearningQuestionnaire that was previously used
     *
     * @param learningQuestionnaire the LearningQuestionnaire to be deselected
     * @throws ServiceException if the LearningQuestionnaire cannot be deselected
     */
    void deselect(LearningQuestionnaire learningQuestionnaire) throws ServiceException;

    /**
     * Get the selected LearningQuestionnaire
     *
     * @return the selected LearningQuestionnaire
     * @throws ServiceException if the selected LearningQuestionnaire cannot be retrieved
     */
    LearningQuestionnaire getSelected() throws ServiceException;
}
