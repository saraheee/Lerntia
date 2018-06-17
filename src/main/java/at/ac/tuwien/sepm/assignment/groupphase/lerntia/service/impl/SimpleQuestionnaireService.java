package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleQuestionnaireService implements IQuestionnaireService {

    private final ILearningQuestionnaireService iLearningQuestionnaire;

    @Autowired
    public SimpleQuestionnaireService(ILearningQuestionnaireService iLearningQuestionnaire) {
        this.iLearningQuestionnaire = iLearningQuestionnaire;
    }

    @Override
    public void deselectAllQuestionnaires() throws ServiceException {

        List<LearningQuestionnaire> learningQuestionnaireList = iLearningQuestionnaire.readAll();

        for (LearningQuestionnaire aLearningQuestionnaireList : learningQuestionnaireList) {
            iLearningQuestionnaire.deselect(aLearningQuestionnaireList);
        }

    }
}
