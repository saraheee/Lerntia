package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class SimpleQuestionnaireQuestionService implements IQuestionnaireQuestionService {
    @Override
    public void create(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {

    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws ServiceException {

    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid, long newQuestionid) throws ServiceException {

    }

    @Override
    public List readAll() throws ServiceException {
        return null;
    }
}
