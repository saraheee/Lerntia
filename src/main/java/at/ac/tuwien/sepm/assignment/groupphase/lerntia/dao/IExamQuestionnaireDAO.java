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

}
