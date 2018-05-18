package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamQuestionnaireService;
import javafx.collections.ObservableList;
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
            System.out.println("======================== 1");

            examQuestionnaireDAO.create(examQuestionnaire);

            System.out.println("======================== 10");
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.update(examQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void search(ExamQuestionnaire searchparameters) throws ServiceException {
        try {
            examQuestionnaireDAO.search(searchparameters);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.delete(examQuestionnaire);
        } catch (PersistenceException e) {
            LOG.warn("Persistance exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<ExamQuestionnaire> readAll() throws ServiceException {
        try {
            return examQuestionnaireDAO.readAll();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void select(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.select(examQuestionnaire);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deselect(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.deselect(examQuestionnaire);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ExamQuestionnaire getSelected() throws ServiceException {

        ExamQuestionnaire examQuestionnaire = null;

        try {
            examQuestionnaire = examQuestionnaireDAO.getSelected();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return examQuestionnaire;
    }
}
