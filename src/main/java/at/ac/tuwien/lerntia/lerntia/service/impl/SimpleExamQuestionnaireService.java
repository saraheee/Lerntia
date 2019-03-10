package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.service.IExamQuestionnaireService;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleExamQuestionnaireService implements IExamQuestionnaireService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IExamQuestionnaireDAO examQuestionnaireDAO;

    @Autowired
    public SimpleExamQuestionnaireService(IExamQuestionnaireDAO examQuestionnaireDAO) {
        this.examQuestionnaireDAO = examQuestionnaireDAO;
    }

    @Override
    public void create(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.create(examQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public List<ExamQuestionnaire> readAll() throws ServiceException {
        try {
            return examQuestionnaireDAO.readAll();
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to read all questionnaires!");
        }
    }


}
