package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleQuestionnaireService implements IQuestionnaireService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IQuestionnaireDAO iQuestionnaireDAO;

    private final ILearningQuestionnaireService iLearningQuestionnaire;
    private final IExamQuestionnaireService iExamQuestionnaireService;

    public SimpleQuestionnaireService(
        IQuestionnaireDAO iQuestionnaireDAO,
        ILearningQuestionnaireService iLearningQuestionnaire,
        IExamQuestionnaireService iExamQuestionnaireService
    )
    {
        this.iQuestionnaireDAO = iQuestionnaireDAO;
        this.iLearningQuestionnaire = iLearningQuestionnaire;
        this.iExamQuestionnaireService = iExamQuestionnaireService;
    }

    @Override
    public void create(Questionnaire questionnaire) throws ServiceException {

    }

    @Override
    public void update(Questionnaire questionnaire) throws ServiceException {

    }

    @Override
    public void deselectAllQuestionnaires() throws ServiceException {

        List<LearningQuestionnaire> learningQuestionnaireList = iLearningQuestionnaire.readAll();

        for (int i = 0; i < learningQuestionnaireList.size(); i++){
            iLearningQuestionnaire.deselect(learningQuestionnaireList.get(i));
        }

        List<ExamQuestionnaire> examQuestionnaireList = iExamQuestionnaireService.readAll();

        for (int i = 0; i < examQuestionnaireList.size(); i++){
            iExamQuestionnaireService.deselect(examQuestionnaireList.get(i));
        }
    }
}
