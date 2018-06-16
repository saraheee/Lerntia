package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;

public interface IQuestionnaireService {

    /**
     * Deselect every Questionnaire
     *
     * @throws ServiceException if the Questionnaires cannot be deselected
     */
    void deselectAllQuestionnaires() throws ServiceException;
}
