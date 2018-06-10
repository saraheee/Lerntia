package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserQuestionnaire;

public interface IUserQuestionnaireService {

    /**
     * Create a new User and save it to the Database
     *
     * @param userQuestionnaire the user-questionnaire connection that needs to be saved to the Database
     * @throws ServiceException if the method can't save the UserQuestionnaire to the Database
     */
    void create(UserQuestionnaire userQuestionnaire) throws ServiceException;
}
