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
