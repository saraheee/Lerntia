package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class MainLerntiaService implements IMainLerntiaService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int SLEEP_SECONDS = 2;

    private boolean learningMode = true;
    private List<LearningQuestionnaire> allLQs;
    private LearningQuestionnaire currentLQ;
    private List<ExamQuestionnaire> allEQs;
    private ExamQuestionnaire currentEQ;
    private List<QuestionnaireQuestion> questionnaireQuestionsList;
    private List<Question> questionList;
    private Question currentQuestion;
    private int listcounter;
    private int currentQuestionIndex;

    private ICourseService courseService;
    private IUserService userService;
    private IQuestionnaireService questionnaireService;
    private IExamQuestionnaireService examQuestionaireService;
    private ILearningQuestionnaireService learningQuestionnaireService;
    private IQuestionService questionService;
    private IQuestionnaireQuestionService questionnaireQuestionService;
    private IUserCourseService userCourseService;
    private IUserQuestionnaireService userQuestionaireService;


    @Autowired
    public MainLerntiaService(ICourseService courseService, IUserService userService, IQuestionnaireService questionnaireService, IExamQuestionnaireService examQuestionaireService, ILearningQuestionnaireService learningQuestionnaireService, IQuestionService questionService, IQuestionnaireQuestionService questionnaireQuestionService, IUserCourseService userCourseService, IUserQuestionnaireService userQuestionaireService) {
        this.courseService = courseService;
        this.userService = userService;
        this.questionnaireService = questionnaireService;
        this.examQuestionaireService = examQuestionaireService;
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.questionService = questionService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.userCourseService = userCourseService;
        this.userQuestionaireService = userQuestionaireService;
    }

    /*
    private ICourseDAO courseDAO;
    private IUserDAO userDAO;
    private IQuestionnaireDAO questionnaireDAO;
    private IExamQuestionnaireDAO examQuestionaireDAO;
    private ILearningQuestionnaireDAO learningQuestionnaireDAO;
    private IQuestionDAO questionDAO;
    private IQuestionnaireQuestionDAO questionnaireQuestionDAO;
    private IUserCourseDAO userCourseDAO;
    private IUserQuestionaireDAO userQuestionaireDAO;


    @Autowired
    public MainLerntiaService(CourseDAO courseDAO, UserDAO userDAO, QuestionnaireDAO questionnaireDAO, ExamQuestionaireDAO examQuestionaireDAO, LearningQuestionnaireDAO learningQuestionnaireDAO, QuestionDAO questionDAO, QuestionnaireQuestionDAO questionnaireQuestionDAO, UserCourseDAO userCourseDAO, UserQuestionaireDAO userQuestionaireDAO){
        this.courseDAO = courseDAO;
        this.userDAO = userDAO;
        this.questionnaireDAO = questionnaireDAO;
        this.examQuestionaireDAO = examQuestionaireDAO;
        this.learningQuestionnaireDAO =learningQuestionnaireDAO;
        this.questionDAO = questionDAO;
        this.questionnaireQuestionDAO = questionnaireQuestionDAO;
        this.userCourseDAO = userCourseDAO;
        this.userQuestionaireDAO = userQuestionaireDAO;
    }
    */

    private void getQuestionsFromExamQuestionnaire(ExamQuestionnaire eQ) throws ServiceException{
        listcounter = 0;
        questionnaireQuestionsList = new ArrayList<>();
        questionList = new ArrayList<>();
        List<Question> searchparameters = new ArrayList<>();
        Question question;
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(eQ.getId());
        questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
        //TODO send updated DATE to ExamQuestionnaireDAO
        while (!questionnaireQuestionsList.isEmpty()){
            question = new Question();
            questionnaireQuestion = questionnaireQuestionsList.get(0);
            question.setId(questionnaireQuestion.getQuestionid());
            searchparameters.add(question);
            questionnaireQuestionsList.remove(0);
            LOG.info("ExamQuestionnaire Question found.");
        }
        questionList = questionService.search(searchparameters);
        for (Question q:questionList) {
            listcounter++;
        }
    }

    private void getQuestionsFromLearningQuestionnaire(LearningQuestionnaire lQ) throws ServiceException{
        listcounter = 0;
        questionnaireQuestionsList = new ArrayList<>();
        questionList = new ArrayList<>();
        List<Question> searchparameters = new ArrayList<>();
        Question question;
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(lQ.getId());
        questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
        while (!questionnaireQuestionsList.isEmpty()){
            question = new Question();
            questionnaireQuestion = questionnaireQuestionsList.get(0);
            question.setId(questionnaireQuestion.getQuestionid());
            searchparameters.add(question);
            questionnaireQuestionsList.remove(0);
            LOG.info("LearningQuestionnaire Question found.");
        }
        questionList = questionService.search(searchparameters);
        for (Question q:questionList) {
            listcounter++;
        }
    }


    @Override
    public Question getNextQuestionFromList() throws ServiceException{
        try {
            if (!(currentQuestionIndex + 1 > listcounter)) {
                currentQuestion = new Question();
                currentQuestion = questionList.get(++currentQuestionIndex);
            }
            return currentQuestion;
        }catch (IndexOutOfBoundsException e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Question getPreviousQuestionFromList()throws ServiceException {
        try {
            if (!(currentQuestionIndex - 1 < 0)) {
                currentQuestion = new Question();
                currentQuestion = questionList.get(--currentQuestionIndex);
            }
            return currentQuestion;
        }catch (IndexOutOfBoundsException e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Question getFirstQuestion() throws ServiceException {
        LearningQuestionnaire currentLQ = null;
        currentLQ = learningQuestionnaireService.getSelected();

        if (currentLQ == null){
            throw new ServiceException("Es wurde noch kein Fragebogen ausgewählt");
        }

        getQuestionsFromLearningQuestionnaire(currentLQ);
        currentQuestionIndex = -1;
        return getNextQuestionFromList();
    }

    @Override
    public void recordCheckedAnswers(Question mockQuestion) throws ServiceException {
        // todo implement this when implementing learning algorithm
    }
}