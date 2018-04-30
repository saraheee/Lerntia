package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

public interface QuestionDAO {
    
    void create(Question question) throws PersistenceException;

    void update(Question question) throws PersistenceException;

    void search(Question question) throws PersistenceException;

    void delete(Question question) throws PersistenceException;
}
