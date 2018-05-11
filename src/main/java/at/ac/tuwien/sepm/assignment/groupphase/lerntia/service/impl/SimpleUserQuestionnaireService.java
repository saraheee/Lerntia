package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserQuestionaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserQuestionnaireService;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserQuestionnaireService implements IUserQuestionnaireService {

    private final IUserQuestionaireDAO iUserQuestionaireDAO;

    public SimpleUserQuestionnaireService(IUserQuestionaireDAO iUserQuestionaireDAO){
        this.iUserQuestionaireDAO = iUserQuestionaireDAO;
    }

    @Override
    public void create(UserQuestionnaire userQuestionnaire) throws ServiceException {

    }
}
