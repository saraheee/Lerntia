package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;

public interface IQuestionnaireService {

    /**
     * Create a new Questionnaire and save it to the Database
     *
     * @param questionnaire Questionnaire that needs to be saved to the Database
     * @throws ServiceException if the method can't save the Questionnaire to the Database
     * */
    void create(Questionnaire questionnaire) throws ServiceException;

    /**
     * Update an existing Questionnaire in the Database
     *
     * @param questionnaire the questionnaire in question that needs to be updated in the Database
     * @throws ServiceException if the method can't update the Questionnaire
     * */
    void update(Questionnaire questionnaire) throws ServiceException;

    /**
     * Deselect every Questionnaire
     *
     * @throws ServiceException if the Questionnaires cannot be deselected
     * */
    void deselectAllQuestionnaires() throws ServiceException;
}
