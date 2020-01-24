package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.lerntia.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionnaireService implements IQuestionnaireService {

    private final ILearningQuestionnaireService iLearningQuestionnaire;

    @Autowired
    public QuestionnaireService(ILearningQuestionnaireService iLearningQuestionnaire) {
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
