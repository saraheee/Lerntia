package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleQuestionnaireService implements IQuestionnaireService {

    private final ILearningQuestionnaireService iLearningQuestionnaire;
    private final IExamQuestionnaireService iExamQuestionnaireService;

    public SimpleQuestionnaireService(
        ILearningQuestionnaireService iLearningQuestionnaire,
        IExamQuestionnaireService iExamQuestionnaireService
    ) {
        this.iLearningQuestionnaire = iLearningQuestionnaire;
        this.iExamQuestionnaireService = iExamQuestionnaireService;
    }

    @Override
    public void deselectAllQuestionnaires() throws ServiceException {

        List<LearningQuestionnaire> learningQuestionnaireList = iLearningQuestionnaire.readAll();

        for (LearningQuestionnaire aLearningQuestionnaireList : learningQuestionnaireList) {
            iLearningQuestionnaire.deselect(aLearningQuestionnaireList);
        }

        List<ExamQuestionnaire> examQuestionnaireList = iExamQuestionnaireService.readAll();

        for (ExamQuestionnaire anExamQuestionnaireList : examQuestionnaireList) {
            iExamQuestionnaireService.deselect(anExamQuestionnaireList);
        }
    }
}
