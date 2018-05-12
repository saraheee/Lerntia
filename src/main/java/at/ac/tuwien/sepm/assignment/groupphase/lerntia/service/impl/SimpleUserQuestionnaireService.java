package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserQuestionaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserQuestionnaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SimpleUserQuestionnaireService implements IUserQuestionnaireService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IUserQuestionaireDAO iUserQuestionaireDAO;

    public SimpleUserQuestionnaireService(IUserQuestionaireDAO iUserQuestionaireDAO) {
        this.iUserQuestionaireDAO = iUserQuestionaireDAO;
    }

    @Override
    public void create(UserQuestionnaire userQuestionnaire) throws ServiceException {

    }
}
