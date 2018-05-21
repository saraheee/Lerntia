package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserQuestionnaire;


public interface IUserQuestionaireDAO {

    /**
     * Create a new User and save it to the Database
     *
     * @param userQuestionnaire the user-questionnaire connection that needs to be saved to the Database
     * @throws PersistenceException if the method can't save the UserQuestionnaire to the Database
     * */
    void create(UserQuestionnaire userQuestionnaire) throws PersistenceException;

    // from stories unclear what methods will be needed for this DAO - todo discuss at the next team meeting
}
