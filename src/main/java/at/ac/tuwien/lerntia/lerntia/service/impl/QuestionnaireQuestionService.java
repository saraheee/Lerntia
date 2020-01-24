package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionnaireQuestionService;
import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireQuestionDAO;
import at.ac.tuwien.lerntia.lerntia.dto.QuestionnaireQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class QuestionnaireQuestionService implements IQuestionnaireQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IQuestionnaireQuestionDAO iQuestionnaireQuestionDAO;

    @Autowired
    public QuestionnaireQuestionService(IQuestionnaireQuestionDAO iQuestionnaireQuestionDAO) {
        this.iQuestionnaireQuestionDAO = iQuestionnaireQuestionDAO;
    }

    @Override
    public void create(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.create(questionnaireQuestion);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.delete(questionnaireQuestion);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid, long newQuestionId) throws ServiceException {
        try {
            iQuestionnaireQuestionDAO.update(questionnaireQuestion, newQid, newQuestionId);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public List readAll() throws ServiceException {
        try {
            return iQuestionnaireQuestionDAO.readAll();
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public List<QuestionnaireQuestion> search(QuestionnaireQuestion searchParameters) throws ServiceException {
        try {
            return iQuestionnaireQuestionDAO.search(searchParameters);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }
}
