package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;

public class SimpleQuestionService implements IQuestionService {


    @Override
    public long create(Question question) throws PersistenceException {
        return 0;
    }

    @Override
    public void update(Question question) throws PersistenceException {

    }

    @Override
    public void search(Question question) throws PersistenceException {

    }

    @Override
    public void delete(Question question) throws PersistenceException {

    }

    @Override
    public Question get(long id) throws PersistenceException {
        return null;
    }
}
