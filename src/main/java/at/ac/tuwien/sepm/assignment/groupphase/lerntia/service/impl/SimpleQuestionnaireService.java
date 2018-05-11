package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import org.springframework.stereotype.Service;

@Service
public class SimpleQuestionnaireService implements IQuestionnaireService {

    private final IQuestionnaireDAO iQuestionnaireDAO;

    public SimpleQuestionnaireService(IQuestionnaireDAO iQuestionnaireDAO){
        this.iQuestionnaireDAO = iQuestionnaireDAO;
    }

    @Override
    public void create(Questionnaire questionnaire) throws ServiceException {

    }

    @Override
    public void update(Questionnaire questionnaire) throws ServiceException {

    }
}
