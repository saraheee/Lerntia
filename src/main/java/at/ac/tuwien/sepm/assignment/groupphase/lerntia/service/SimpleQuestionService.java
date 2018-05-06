package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import org.springframework.stereotype.Service;

@Service
public class SimpleQuestionService implements IQuestionService {

    @Override
    public long create(Question question) throws ServiceException {
        return 0;
    }

    @Override
    public void update(Question question) throws ServiceException {

    }

    @Override
    public void search(Question question) throws ServiceException {

    }

    @Override
    public void delete(Question question) throws ServiceException {

    }

    @Override
    public Question get(long id) throws ServiceException {
        return null;
    }
}
