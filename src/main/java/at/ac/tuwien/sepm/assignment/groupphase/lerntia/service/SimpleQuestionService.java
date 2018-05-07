package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import org.springframework.stereotype.Service;

import javax.sound.midi.SysexMessage;

@Service
public class SimpleQuestionService implements IQuestionService {

    private final QuestionDAO questionDAO;

    public SimpleQuestionService(QuestionDAO questionDAO){
        this.questionDAO = questionDAO;
    }

    @Override
    public long create(Question question) throws ServiceException {
        long id = 0;

        try {
            id = questionDAO.create(question);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public void update(Question question) throws ServiceException {
        try {
            questionDAO.update(question);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void search(Question question) throws ServiceException {
        try {
            questionDAO.search(question);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Question question) throws ServiceException {
        try {
            questionDAO.delete(question);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Question get(long id) throws ServiceException {

        Question question = null;

        try {
            question = questionDAO.get(id);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return question;
    }
}
