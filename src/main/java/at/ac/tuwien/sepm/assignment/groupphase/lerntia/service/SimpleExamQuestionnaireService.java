package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public class SimpleExamQuestionnaireService implements IExamQuestionnaireService{
    @Override
    public void create(ExamQuestionnaire examQuestionnaire) throws ServiceException {

    }

    @Override
    public void update(ExamQuestionnaire examQuestionnaire) throws ServiceException {

    }

    @Override
    public void search(ExamQuestionnaire searchparameters) throws ServiceException {

    }

    @Override
    public void delete(ExamQuestionnaire examQuestionnaire) throws ServiceException {

    }

    @Override
    public ObservableList readAll() throws ServiceException {
        return null;
    }
}
