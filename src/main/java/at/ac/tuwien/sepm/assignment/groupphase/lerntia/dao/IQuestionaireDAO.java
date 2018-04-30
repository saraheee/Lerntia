package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;


import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;
import javafx.collections.ObservableList;

public interface IQuestionaireDAO {
    void create(Questionnaire questionnaire) throws PersistenceException;

    void update(Questionnaire questionnaire) throws PersistenceException;

    void search(Questionnaire questionnaire) throws PersistenceException;

    void delete(Questionnaire questionnaire) throws PersistenceException;

    ObservableList readAll() throws PersistenceException;
}
