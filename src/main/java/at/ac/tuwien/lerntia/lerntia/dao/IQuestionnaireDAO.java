package at.ac.tuwien.lerntia.lerntia.dao;

import at.ac.tuwien.lerntia.lerntia.dto.Questionnaire;
import at.ac.tuwien.lerntia.exception.PersistenceException;


public interface IQuestionnaireDAO {

    /**
     * Create a new Questionnaire and save it to the Database
     *
     * @param questionnaire Questionnaire that needs to be saved to the Database
     * @throws PersistenceException if the method can't save the Questionnaire to the Database
     */
    void create(Questionnaire questionnaire) throws PersistenceException;

    /**
     * Select a Questionnaire from the Database using an id
     *
     * @param id the id for which a Questionnaire should be retrieved from the Database
     * @throws PersistenceException if the Questionnaire cannot be retrieved from the Database
     */
    String getQuestionnaireName(Long id) throws PersistenceException;

    /**
     * Select an Questionnaire to be used
     *
     * @param questionnaire the Questionnaire to be selected
     * @throws PersistenceException if the Questionnaire cannot be selected in the Database
     */
    void select(Questionnaire questionnaire) throws PersistenceException;

    /**
     * Deselect an Questionnaire that was previously used
     *
     * @param questionnaire the Questionnaire to be deselected
     * @throws PersistenceException if the Questionnaire cannot be deselected in the Database
     */
    void deselect(Questionnaire questionnaire) throws PersistenceException;

    /**
     * Get the selected Questionnaire from the Database
     *
     * @return the selected Questionnaire
     * @throws PersistenceException if the selected Questionnaire cannot be retrieved from the Database
     */
    Questionnaire getSelected() throws PersistenceException;
}
