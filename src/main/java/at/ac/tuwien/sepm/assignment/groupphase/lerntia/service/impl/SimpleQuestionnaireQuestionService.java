package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

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
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.delete(questionnaireQuestion);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid, long newQuestionId) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.update(questionnaireQuestion, newQid, newQuestionId);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List readAll() throws ServiceException {
        try {
            return iQuestionnaireQuestionDAO.readAll();
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<QuestionnaireQuestion> search(QuestionnaireQuestion searchParameters) throws ServiceException {
        try {
            return iQuestionnaireQuestionDAO.search(searchParameters);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
