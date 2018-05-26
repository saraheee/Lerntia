package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;


import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;


@Component
public class MainLerntiaService implements IMainLerntiaService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private boolean learningMode = true;
    private List<LearningQuestionnaire> allLQs;
    private LearningQuestionnaire currentLQ;
    private List<ExamQuestionnaire> allEQs;
    private ExamQuestionnaire currentEQ;
    private List<QuestionnaireQuestion> questionnaireQuestionsList;
    private List<Question> questionList;
    private Question currentQuestion;
    private int listCounter;
    private int currentQuestionIndex;

    private ICourseService courseService;
    private IUserService userService;
    private IQuestionnaireService questionnaireService;
    private IExamQuestionnaireService examQuestionnaireService;
    private ILearningQuestionnaireService learningQuestionnaireService;
    private IQuestionService questionService;
    private IQuestionnaireQuestionService questionnaireQuestionService;
    private IUserCourseService userCourseService;
    private IUserQuestionnaireService userQuestionnaireService;


    @Autowired
    public MainLerntiaService(ICourseService courseService, IUserService userService, IQuestionnaireService questionnaireService,
                              IExamQuestionnaireService examQuestionnaireService, ILearningQuestionnaireService learningQuestionnaireService,
                              IQuestionService questionService, IQuestionnaireQuestionService questionnaireQuestionService,
                              IUserCourseService userCourseService, IUserQuestionnaireService userQuestionnaireService) {
        this.courseService = courseService;
        this.userService = userService;
        this.questionnaireService = questionnaireService;
        this.examQuestionnaireService = examQuestionnaireService;
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.questionService = questionService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.userCourseService = userCourseService;
        this.userQuestionnaireService = userQuestionnaireService;
    }


    private void getQuestionsFromExamQuestionnaire(ExamQuestionnaire eQ) throws ServiceException {
        LOG.info("Get questions from an Exam Questionnaire.");
        listCounter = 0;
        questionnaireQuestionsList = new ArrayList<>();
        questionList = new ArrayList<>();
        List<Question> searchparameters = new ArrayList<>();
        Question question;
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(eQ.getId());
        questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
        //TODO send updated DATE to ExamQuestionnaireDAO
        getQuestionFromList(searchparameters);
        LOG.info("All Exam Questionnaire Questions info found.");
        LOG.info("Send required question search parameters to retrieve");
        questionList = questionService.search(searchparameters);
        for (Question q : questionList) {
            listCounter++;
        }
        LOG.info("All Exam Questions set.");
    }

    private void getQuestionFromList(List<Question> searchParameters) {
        Question question;
        QuestionnaireQuestion questionnaireQuestion;
        while (!questionnaireQuestionsList.isEmpty()) {
            question = new Question();
            questionnaireQuestion = questionnaireQuestionsList.get(0);
            question.setId(questionnaireQuestion.getQuestionid());
            searchParameters.add(question);
            questionnaireQuestionsList.remove(0);
        }
    }

    private void getQuestionsFromLearningQuestionnaire(LearningQuestionnaire lQ) throws ServiceException {
        LOG.info("Get all questions from a Learning Questionnaire.");
        listCounter = 0;
        questionnaireQuestionsList = new ArrayList<>();
        questionList = new ArrayList<>();
        List<Question> searchparameters = new ArrayList<>();
        Question question;
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(lQ.getId());
        questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
        LOG.info("All info regarding Questions found.");
        getQuestionFromList(searchparameters);
        LOG.info("All questions from the selected LearningQuestionnaire found.");
        LOG.info("Search for questions in the Database.");
        questionList = questionService.search(searchparameters);
        for (Question q : questionList) {
            listCounter++;
        }
        LOG.info("All Questins from the LearningQuestionnaire set.");
    }


    @Override
    public Question getNextQuestionFromList() throws ServiceException {
        try {
            LOG.info("Get next Question from Questionnaire.");
            if (!(currentQuestionIndex + 1 > listCounter)) {
                currentQuestion = new Question();
                currentQuestion = questionList.get(++currentQuestionIndex);
            }
            LOG.info("Next question.");
            return currentQuestion;
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Question getPreviousQuestionFromList() throws ServiceException {
        try {
            LOG.info("Get previous Question from Questionnaire");
            if (!(currentQuestionIndex - 1 < 0)) {
                currentQuestion = new Question();
                currentQuestion = questionList.get(--currentQuestionIndex);
            }
            LOG.info("Previous Question.");
            return currentQuestion;
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Question loadQuestionnaireAndGetFirstQuestion() throws ServiceException {
        LearningQuestionnaire currentLQ;
        currentLQ = learningQuestionnaireService.getSelected();
        if (currentLQ == null) {
            throw new ServiceException("No Questionnaire has been selected yet.");
        }

        getQuestionsFromLearningQuestionnaire(currentLQ);
        currentQuestionIndex = -1;
        LOG.info("First question found.");
        return getNextQuestionFromList();
    }

    @Override
    public Question getFirstQuestion() throws ServiceException {
        try {
            currentQuestion = questionList.get(0);
            currentQuestionIndex = 0;
            return currentQuestion;
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void recordCheckedAnswers(Question mockQuestion) throws ServiceException {
        // todo implement this when implementing learning algorithm - learning algorithm in another branch currently
    }

    @Override
    public List<Question> getQuestions() throws ServiceException {
        LOG.info("Get all questions.");
        return questionList;
    }

    /**
     * Returns the Size of the ListCounter
     */
    @Override
    public int getListCounter() {
        return this.listCounter;
    }

    @Override
    public List<Question> getQuestionList() {
        return this.questionList;
    }

}