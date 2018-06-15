package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;

import java.util.List;

public interface IExamQuestionnaireDAO {

    /**
     * Create a new ExamQuestionnaire and save it in the Database
     *
     * @param examQuestionnaire ExamQuestionnaire that needs to be saved to the Database
     * @throws PersistenceException if the ExamQuestionnaire can't be created
     */
    void create(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    /**
     * Update an existing ExamQuestionnaire with new Parameters and save those to the Database
     *
     * @param examQuestionnaire the ExamQuestionnaire in question with the new or same parameters.
     * @throws PersistenceException if the method can't update the ExamQuestionnaire
     */
    void update(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    /**
     * Search for ExamQuestionnaire with given searchParameters in the Database
     *
     * @param searchParameters all or some searchParameters of the ExamQuestionnaire
     * @throws PersistenceException if the method can't render
     */
    void search(ExamQuestionnaire searchParameters) throws PersistenceException;

    /**
     * Delete ExamQuestionnaire from the Database
     *
     * @param examQuestionnaire ExamQuestionnaire in question that has to be deleted
     * @throws PersistenceException if the method can't delete the ExamQuestionnaire from the Database
     */
    void delete(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    /**
     * Read all ExamQuestionnaires from the Database
     *
     * @return list of all ExamQuestionnaires
     * @throws PersistenceException if the list cannot be retrieved from the Database
     */
    List<ExamQuestionnaire> readAll() throws PersistenceException;

    /**
     * Select an ExamQuestionnaire to be used for an exam
     *
     * @param examQuestionnaire the ExamQuestionnaire to be selected
     * @throws PersistenceException if the ExamQuestionnaire cannot be selected in the Database
     */
    void select(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    /**
     * Deselect an ExamQuestionnaire that was previously used
     *
     * @param examQuestionnaire the ExamQuestionnaire to be deselected
     * @throws PersistenceException if the ExamQuestionnaire cannot be deselected in the Database
     */
    void deselect(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    /**
     * Get the selected ExamQuestionnaire from the Database
     *
     * @return the selected ExamQuestionnaire
     * @throws PersistenceException if the selected ExamQuestionnaire cannot be retrieved from the Database
     */
    ExamQuestionnaire getSelected() throws PersistenceException;

}
