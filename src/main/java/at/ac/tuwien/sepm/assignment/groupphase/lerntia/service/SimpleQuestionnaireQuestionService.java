package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionnaireQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class SimpleQuestionnaireQuestionService implements IQuestionnaireQuestionService {

    private final IQuestionnaireQuestionDAO iQuestionnaireQuestionDAO;

    public SimpleQuestionnaireQuestionService(IQuestionnaireQuestionDAO iQuestionnaireQuestionDAO){
        this.iQuestionnaireQuestionDAO = iQuestionnaireQuestionDAO;
    }

    @Override
    public void create(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.create(questionnaireQuestion);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.delete(questionnaireQuestion);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid, long newQuestionid) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.update(questionnaireQuestion, newQid, newQuestionid);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List readAll() throws ServiceException {
        return null;
    }
}
