package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import javafx.collections.ObservableList;

public interface IExamQuestionaireDAO {


    void create(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    void update(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    void search(ExamQuestionnaire searchparameters) throws PersistenceException;

    void delete(ExamQuestionnaire examQuestionnaire) throws PersistenceException;

    ObservableList readAll() throws PersistenceException;
}
