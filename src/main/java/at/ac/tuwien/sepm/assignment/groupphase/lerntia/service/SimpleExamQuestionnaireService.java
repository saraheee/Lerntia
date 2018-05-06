package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ExamQuestionaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public class SimpleExamQuestionnaireService implements IExamQuestionnaireService{

    private final IExamQuestionnaireDAO examQuestionnaireDAO;

    public SimpleExamQuestionnaireService(IExamQuestionnaireDAO examQuestionnaireDAO){
        this.examQuestionnaireDAO = examQuestionnaireDAO;
    }

    @Override
    public void create(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.create(examQuestionnaire);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.update(examQuestionnaire);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void search(ExamQuestionnaire searchparameters) throws ServiceException {
        try {
            examQuestionnaireDAO.search(searchparameters);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ExamQuestionnaire examQuestionnaire) throws ServiceException {
        try {
            examQuestionnaireDAO.delete(examQuestionnaire);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList readAll() throws ServiceException {
        return null;
    }
}
