package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class SimpleQuestionnaireQuestionService implements IQuestionnaireQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IQuestionnaireQuestionDAO iQuestionnaireQuestionDAO;

    public SimpleQuestionnaireQuestionService(IQuestionnaireQuestionDAO iQuestionnaireQuestionDAO) {
        this.iQuestionnaireQuestionDAO = iQuestionnaireQuestionDAO;
    }

    @Override
    public void create(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.create(questionnaireQuestion);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.delete(questionnaireQuestion);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid, long newQuestionid) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.update(questionnaireQuestion, newQid, newQuestionid);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List readAll() throws ServiceException {
        return null;
    }

    @Override
    public List<QuestionnaireQuestion> search(QuestionnaireQuestion searchparameters) throws ServiceException {
        try {
            return iQuestionnaireQuestionDAO.search(searchparameters);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
