package at.ac.tuwien.lerntia.lerntia.service;

import at.ac.tuwien.lerntia.exception.ServiceException;

public interface IQuestionnaireService {

    /**
     * Deselect every Questionnaire
     *
     * @throws ServiceException if the Questionnaires cannot be deselected
     */
    void deselectAllQuestionnaires() throws ServiceException;
}
