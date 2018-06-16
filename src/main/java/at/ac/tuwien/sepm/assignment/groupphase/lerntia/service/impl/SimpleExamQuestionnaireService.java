package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamQuestionnaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleExamQuestionnaireService implements IExamQuestionnaireService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IExamQuestionnaireDAO examQuestionnaireDAO;

    public SimpleExamQuestionnaireService(IExamQuestionnaireDAO examQuestionnaireDAO){
        this.examQuestionnaireDAO = examQuestionnaireDAO;
    }

    @Override
    public void create(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.create(examQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustommessage());
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

    @Override
    public void select(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.select(examQuestionnaire);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustommessage());
        }
    }

    @Override
    public void deselect(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.deselect(examQuestionnaire);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getCustommessage());
        }
    }

    @Override
    public ExamQuestionnaire getSelected() throws ServiceException {

        ExamQuestionnaire examQuestionnaire;

        try {
            examQuestionnaire = examQuestionnaireDAO.getSelected();
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to get the selected questionnaire!");
        }

        return examQuestionnaire;
    }
}
