package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import javafx.collections.ObservableList;

import java.util.List;

public interface IExamQuestionnaireDAO {

    /**
     * Create a new ExamQuestionnaire and save it in the Database
     *
     * @param examQuestionnaire ExamQuestionnaire that needs to be saved to the Database
     * @throws PersistenceException if the ExamQuestionnaire can't be created
     * */
    void create(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    /**
     * Update an existing ExamQuestionnaire with new Parameters and save those to the Database
     *
     * @param examQuestionnaire the ExamQuestionnaire in question with the new or same parameters.
     * @throws PersistenceException if the method can't update the ExamQuestionnaire
     * */
    void update(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    /**
     * Search for ExamQuestionnaire with given searchparameters in the Database
     *
     * @param searchparameters all or some searchparameters of the ExamQuestionnaire
     * @throws PersistenceException if the method can't render
     * */
    void search(ExamQuestionnaire searchparameters) throws PersistenceException;

    /**
     * Delete ExamQuestionnaire from the Database
     *
     * @param examQuestionnaire ExamQuestionnaire in question that has to be deleted
     * @throws PersistenceException if the method can't delete the ExamQuestionnaire from the Database
     * */
    void delete(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    List<ExamQuestionnaire> readAll() throws PersistenceException;
}
