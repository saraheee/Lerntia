package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;


import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.LearnAlgorithmController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MainLerntiaService implements IMainLerntiaService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Map<Long,Question> questionMap;
    private boolean learnAlgorithm;
    private boolean learningMode = true;
    private List<LearningQuestionnaire> allLQs;
    private LearningQuestionnaire currentLQ;
    private List<ExamQuestionnaire> allEQs;
    private ExamQuestionnaire currentEQ;
    private List<QuestionnaireQuestion> questionnaireQuestionsList;
    private List<Question> questionList;
    private List<Long> algorithmlist;
    private int algorithmlistcounter;
    private int currentAlgorithmQuestionIndex;
    private Question currentQuestion;
    private int listCounter;
    private int currentQuestionIndex;
    private List<QuestionLearnAlgorithm> questionLearnAlgorithmList;


    private ICourseService courseService;
    private IUserService userService;
    private IQuestionnaireService questionnaireService;
    private IExamQuestionnaireService examQuestionnaireService;
    private ILearningQuestionnaireService learningQuestionnaireService;
    private IQuestionService questionService;
    private IQuestionnaireQuestionService questionnaireQuestionService;
    private IUserCourseService userCourseService;
    private IUserQuestionnaireService userQuestionnaireService;
    private ILearnAlgorithmService learnAlgorithmService;
    private LearnAlgorithmController learnAlgorithmController;


    @Autowired
    public MainLerntiaService(ICourseService courseService, IUserService userService, IQuestionnaireService questionnaireService,
                              IExamQuestionnaireService examQuestionnaireService, ILearningQuestionnaireService learningQuestionnaireService,
                              IQuestionService questionService, IQuestionnaireQuestionService questionnaireQuestionService,
                              IUserCourseService userCourseService, IUserQuestionnaireService userQuestionnaireService, ILearnAlgorithmService learnAlgorithmService,LearnAlgorithmController learnAlgorithmController) {
        this.courseService = courseService;
        this.userService = userService;
        this.questionnaireService = questionnaireService;
        this.examQuestionnaireService = examQuestionnaireService;
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.questionService = questionService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.userCourseService = userCourseService;
        this.userQuestionnaireService = userQuestionnaireService;
        this.learnAlgorithmService = learnAlgorithmService;
        this.learnAlgorithmController = learnAlgorithmController;
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
        getExamQuestionFromList(searchparameters);
        LOG.info("All Exam Questionnaire Questions info found.");
        LOG.info("Send required question search parameters to retrieve");
        questionList = questionService.search(searchparameters);
        for (Question q : questionList) {
            listCounter++;
        }
        LOG.info("All Exam Questions set.");
    }

    private void getExamQuestionFromList(List<Question> searchParameters) {
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

    private void getLearningQuestionFromList(List<Question> searchparameters, List<QuestionLearnAlgorithm> questionLearnAlgorithmList) {
        Question question;
        QuestionLearnAlgorithm questionLearnAlgorithm;
        QuestionnaireQuestion questionnaireQuestion;
        while (!questionnaireQuestionsList.isEmpty()) {
            question = new Question();
            questionLearnAlgorithm = new QuestionLearnAlgorithm();
            questionnaireQuestion = questionnaireQuestionsList.get(0);
            question.setId(questionnaireQuestion.getQuestionid());
            searchparameters.add(question);

            questionLearnAlgorithm.setID(questionnaireQuestion.getQuestionid());
            questionLearnAlgorithmList.add(questionLearnAlgorithm);
            questionnaireQuestionsList.remove(0);
        }
    }

    private void getQuestionsFromLearningQuestionnaire(LearningQuestionnaire lQ) throws ServiceException {
        learnAlgorithm = learnAlgorithmController.isSelected();
        questionMap = new HashMap<>();

        if (algorithmlist!=null){}
        LOG.info("Get all questions from a Learning Questionnaire.");
        listCounter = 0;
        algorithmlistcounter = 0;
        questionnaireQuestionsList = new ArrayList<>();
        questionLearnAlgorithmList = new ArrayList<>();
        questionList = new ArrayList<>();
        List<Question> searchparameters = new ArrayList<>();
        Question question;
        QuestionLearnAlgorithm questionLearnAlgorithm;
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(lQ.getId());
        questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
        LOG.info("All info regarding Questions found.");
        getLearningQuestionFromList(searchparameters,questionLearnAlgorithmList);
        LOG.info("All questions from the selected LearningQuestionnaire found.");
        LOG.info("Search for questions in the Database.");
        questionList = questionService.search(searchparameters);
        algorithmlist = learnAlgorithmService.prepareQuestionvalues(questionLearnAlgorithmList);
        for (Question q : questionList) {
            listCounter++;
            algorithmlistcounter++;
            questionMap.put(q.getId(),q);
        }
        LOG.info("All Questions from the LearningQuestionnaire are set.");
    }


    @Override
    public Question getNextQuestionFromList() throws ServiceException {
        try {
            learnAlgorithm =learnAlgorithmController.isSelected();
            if (learnAlgorithm) {
                if (!(currentAlgorithmQuestionIndex+1>algorithmlistcounter)){
                            currentQuestion = new Question();
                            currentQuestion = questionMap.get(algorithmlist.get(++currentAlgorithmQuestionIndex));
                            LOG.info("Found next question determined by Learn Algorithm.");
                            return currentQuestion;
                        }
            }else {
                LOG.info("Get next Question from Questionnaire.");
                if (!(currentQuestionIndex + 1 > listCounter)) {
                    currentQuestion = new Question();
                    currentQuestion = questionList.get(++currentQuestionIndex);
                    return currentQuestion;
                }
                LOG.info("Next question.");
            }
            return currentQuestion;
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Question getPreviousQuestionFromList() throws ServiceException {
        try {
            learnAlgorithm =learnAlgorithmController.isSelected();
            if (learnAlgorithm){
                LOG.info("Get previous question determined by the Learn Algorithm");
                if (!(currentAlgorithmQuestionIndex-1<0)){
                    currentQuestion = new Question();
                    currentQuestion = questionMap.get(algorithmlist.get(--currentAlgorithmQuestionIndex));
                    LOG.info("Found previous question determinded by the Learn Algorithm.");
                    return currentQuestion;
                }
            }else {
                LOG.info("Get previous Question from Questionnaire");
                if (!(currentQuestionIndex - 1 < 0)) {
                    currentQuestion = new Question();
                    currentQuestion = questionList.get(--currentQuestionIndex);
                    return currentQuestion;
                }
                LOG.info("Previous Question.");
            }
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
            currentAlgorithmQuestionIndex = 0;
            if (learnAlgorithm){
                algorithmlist = learnAlgorithmService.prepareQuestionvalues(questionLearnAlgorithmList);
                LOG.info("Revert to first question in the Algorithm List.");
                currentQuestion = questionMap.get(algorithmlist.get(currentAlgorithmQuestionIndex));
                return currentQuestion;
            }else {
                LOG.info("Get first Question of the Question List.");
                currentQuestion = questionList.get(0);
                currentQuestionIndex = 0;
                return currentQuestion;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void recordCheckedAnswers(Question question,boolean answersCorrect) throws ServiceException {
        learnAlgorithm =learnAlgorithmController.isSelected();
        if (learnAlgorithm){
            if (answersCorrect){
                LOG.info("Send to update Map");
                learnAlgorithmService.updateSuccessValue(question);
            }else if (!answersCorrect){
                LOG.info("Send to failure Map");
                learnAlgorithmService.updateFailureValue(question);
            }
        }
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

    @Override
    public void stopAlgorithm() throws ServiceException {
        learnAlgorithmService.changeAlgorithmValues();
    }

}