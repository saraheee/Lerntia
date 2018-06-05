package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;


import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.AlertController;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.LearnAlgorithmController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.rowset.serial.SerialException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MainLerntiaService implements IMainLerntiaService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Map<Long, Question> questionMap;
    private boolean learnAlgorithm;
    private boolean learningMode = true;
    private boolean showOnlyWrongQuestions = false;
    private List<LearningQuestionnaire> allLQs;
    private LearningQuestionnaire currentLQ;
    private List<ExamQuestionnaire> allEQs;
    private List<QuestionLearnAlgorithm> checker;
    private ExamQuestionnaire currentEQ;
    private List<QuestionnaireQuestion> questionnaireQuestionsList;
    private List<Question> questionList;
    private List<Question> wrongQuestions;
    private List<Long> algorithmList;
    private int algorithmListCounter;
    private int currentAlgorithmQuestionIndex;
    private int currentWrongQuestionIndex;
    private int wrongQuestionListCounter;
    private Question currentQuestion;
    private int listCounter;
    private int currentQuestionIndex;
    private List<QuestionLearnAlgorithm> questionLearnAlgorithmList;
    private boolean examMode;


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
    private AlertController alertController;


    @Autowired
    public MainLerntiaService(ICourseService courseService, IUserService userService, IQuestionnaireService questionnaireService,
                              IExamQuestionnaireService examQuestionnaireService, ILearningQuestionnaireService learningQuestionnaireService,
                              IQuestionService questionService, IQuestionnaireQuestionService questionnaireQuestionService,
                              IUserCourseService userCourseService, IUserQuestionnaireService userQuestionnaireService,
                              ILearnAlgorithmService learnAlgorithmService, LearnAlgorithmController learnAlgorithmController, AlertController alertController) {
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
        this.alertController = alertController;
    }


    @Override
    public void setCustomExamQuestions(ArrayList customList) throws ServiceException {
        stopAlgorithm();
        resetWrongQuestionList();
        questionList = customList;
        listCounter = questionList.size();
        currentQuestionIndex = -1;
        LOG.info("First question found.");
    }
    @Override
    public void getQuestionsFromExamQuestionnaire(ExamQuestionnaire eQ) throws ServiceException {
        try {
            //In case that the Programm switched from learning to examquestionnaire while learnalgorithm is still open or if the option was selected that only wrong question are to be shown
            stopAlgorithm();
            resetWrongQuestionList();

            LOG.info("Get questions from an Exam Questionnaire.");
            listCounter = 0;
            questionnaireQuestionsList = new ArrayList<>();
            questionList = new ArrayList<>();
            List<Question> searchParameters = new ArrayList<>();
            Question question;
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQid(eQ.getId());
            questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
            //TODO send updated DATE to ExamQuestionnaireDAO
            getExamQuestionFromList(searchParameters);
            LOG.info("All Exam Questionnaire Questions info found.");
            LOG.info("Send required question search parameters to retrieve");
            questionList = questionService.search(searchParameters);
            for (Question q : questionList) {
                listCounter++;
            }
            LOG.info("All Exam Questions set.");
        }catch (Exception e){
            throw new ServiceException("Can't retrieve exam questionnaire.");
        }
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

    private void getLearningQuestionFromList(List<Question> searchParameters, List<QuestionLearnAlgorithm> questionLearnAlgorithmList) {
        Question question;
        QuestionLearnAlgorithm questionLearnAlgorithm;
        QuestionnaireQuestion questionnaireQuestion;

        while (!questionnaireQuestionsList.isEmpty()) {
            List<QuestionLearnAlgorithm> helper = questionLearnAlgorithmList;
            question = new Question();
            questionLearnAlgorithm = new QuestionLearnAlgorithm();
            questionnaireQuestion = questionnaireQuestionsList.get(0);
            question.setId(questionnaireQuestion.getQuestionid());
            searchParameters.add(question);

            questionLearnAlgorithm.setID(questionnaireQuestion.getQuestionid());
            questionLearnAlgorithmList.add(questionLearnAlgorithm);
            questionnaireQuestionsList.remove(0);
        }
    }

    private void getQuestionsFromLearningQuestionnaire(LearningQuestionnaire lQ) throws ServiceException {
        learnAlgorithm = learnAlgorithmController.isSelected();
        questionMap = new HashMap<>();
        if (algorithmList != null) {
            algorithmList = null;
            algorithmList = new ArrayList<>();
        }
        LOG.info("Get all questions from a Learning Questionnaire.");
        listCounter = 0;
        algorithmListCounter = 0;
        questionnaireQuestionsList = new ArrayList<>();
        questionLearnAlgorithmList = new ArrayList<>();
        questionList = new ArrayList<>();
        List<Question> searchParameters = new ArrayList<>();
        Question question;
        QuestionLearnAlgorithm questionLearnAlgorithm;
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(lQ.getId());
        questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
        LOG.info("All info regarding Questions found.");
        getLearningQuestionFromList(searchParameters, questionLearnAlgorithmList);
        LOG.info("All questions from the selected LearningQuestionnaire found.");
        LOG.info("Search for questions in the Database.");
        questionList = questionService.search(searchParameters);
        checker = questionLearnAlgorithmList;
        algorithmList = learnAlgorithmService.prepareQuestionValues(questionLearnAlgorithmList);
        for (Question q : questionList) {
            listCounter++;
            algorithmListCounter++;
            questionMap.put(q.getId(), q);
        }
        LOG.info("All Questions from the LearningQuestionnaire are set.");
    }


    @Override
    public Question getNextQuestionFromList() throws ServiceException {
        if (examMode) {
            try {
                LOG.info("Get next Exam Question from Exam Questionnaire.");
                if (!(currentQuestionIndex + 1 > listCounter)) {
                    currentQuestion = new Question();
                    currentQuestion = questionList.get(++currentQuestionIndex);
                    LOG.info("Next question.");
                    return currentQuestion;
                }else {
                    currentQuestion = questionList.get(currentQuestionIndex);
                }
            }catch (IndexOutOfBoundsException e){
                throw new ServiceException("Reached end of Exam Questionnaire");
            }
        } else {
                try {
                learnAlgorithm = learnAlgorithmController.isSelected();
                if (learnAlgorithm && showOnlyWrongQuestions) {
                    if (wrongQuestions.size() != 0) {
                        if (!(currentWrongQuestionIndex + 1 > wrongQuestions.size())) {
                            currentQuestion = new Question();
                            currentQuestion = wrongQuestions.get(++currentWrongQuestionIndex);
                            LOG.info("Found next question that has been answered wrong previously and is part of the.");
                            return currentQuestion;
                        }
                    } else {
                        stopAlgorithm();
                        resetWrongQuestionList();
                        alertController.setOnlyWrongQuestions(false);
                        getFirstQuestion();
                        throw new ServiceException("List of wrong questions is Empty");
                    }
                } else if (learnAlgorithm) {
                    if (!(currentAlgorithmQuestionIndex + 1 > algorithmListCounter)) {
                        currentQuestion = new Question();
                        currentQuestion = questionMap.get(algorithmList.get(++currentAlgorithmQuestionIndex));
                        LOG.info("Found next question determined by Learn Algorithm.");
                        return currentQuestion;
                    }
                } else if (showOnlyWrongQuestions) {
                    if (!(wrongQuestions.size() == 0)) {
                        if (!(currentWrongQuestionIndex + 1 > wrongQuestions.size())) {
                            currentQuestion = new Question();
                            currentQuestion = wrongQuestions.get(++currentWrongQuestionIndex);
                            LOG.info("Found next question that has been answered wrong previously.");
                            return currentQuestion;
                        }
                    } else {
                        resetWrongQuestionList();
                        alertController.setOnlyWrongQuestions(false);
                        getFirstQuestion();
                        throw new ServiceException("List of wrong questions is Empty");
                    }
                } else {
                    LOG.info("Get next Question from Questionnaire.");
                    if (!(currentQuestionIndex + 1 > listCounter)) {
                        currentQuestion = new Question();
                        currentQuestion = questionList.get(++currentQuestionIndex);
                        return currentQuestion;
                    }
                    LOG.info("Next question.");
                }
                return currentQuestion;
            } catch(IndexOutOfBoundsException e){
                throw new ServiceException("Reached end of Question List");
            }
        }
        return currentQuestion;
    }

    @Override
    public Question getPreviousQuestionFromList() throws ServiceException {
        try {
            learnAlgorithm = learnAlgorithmController.isSelected();
            if (learnAlgorithm && showOnlyWrongQuestions) {
                if (!(currentWrongQuestionIndex - 1 > 0)) {
                    currentQuestion = new Question();
                    currentQuestion = wrongQuestions.get(--currentWrongQuestionIndex);
                    LOG.info("Found previous question that has been answered wrong previously and is part of the.");
                    return currentQuestion;
                }
            } else if (learnAlgorithm) {
                LOG.info("Get previous question determined by the Learn Algorithm");
                if (!(currentAlgorithmQuestionIndex - 1 < 0)) {
                    currentQuestion = new Question();
                    currentQuestion = questionMap.get(algorithmList.get(--currentAlgorithmQuestionIndex));
                    LOG.info("Found previous question determined by the Learn Algorithm.");
                    return currentQuestion;
                }
            } else if (showOnlyWrongQuestions) {
                if (!(currentWrongQuestionIndex - 1 < 0)) {
                    currentQuestion = new Question();
                    currentQuestion = wrongQuestions.get(--currentWrongQuestionIndex);
                    LOG.info("Found previous question that has been answered wrong previously.");
                    return currentQuestion;
                }
            } else {
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
        if (wrongQuestions == null){
            wrongQuestions = new ArrayList<>();
            currentWrongQuestionIndex = 0;
            wrongQuestionListCounter = 0;
        } else {
            resetWrongQuestionList();
        }

            currentLQ = learningQuestionnaireService.getSelected();
            currentEQ = null;
            getQuestionsFromLearningQuestionnaire(currentLQ);
            if (currentLQ == null) {
                throw new ServiceException("No Questionnaire has been selected yet.");
            }

        if (!learnAlgorithm) {
            currentQuestionIndex = -1;
            currentAlgorithmQuestionIndex = 0;
        } else {
            currentQuestionIndex = 0;
            currentAlgorithmQuestionIndex = -1;

        }
        LOG.info("First question found.");
        return getNextQuestionFromList();

    }

    @Override
    public Question getFirstQuestion() throws ServiceException {
        try {
            currentAlgorithmQuestionIndex = 0;
            currentWrongQuestionIndex = 0;
            if (learnAlgorithm && showOnlyWrongQuestions && wrongQuestions.size() > 0) {
                currentQuestion = wrongQuestions.get(0);
                currentWrongQuestionIndex = 0;
                return currentQuestion;
            } else if (learnAlgorithm && showOnlyWrongQuestions && wrongQuestions.size() == 0) {
                currentAlgorithmQuestionIndex = 0;
                throw new ServiceException("No wrong Questions available");
            } else if (learnAlgorithm) {
                currentAlgorithmQuestionIndex = 0;
                LOG.info("Revert to first question in the Algorithm List.");
                currentQuestion = questionMap.get(algorithmList.get(currentAlgorithmQuestionIndex));
                wrongQuestions = new ArrayList<>();
                currentWrongQuestionIndex = 0;
                wrongQuestionListCounter = 0;
                return currentQuestion;
            } else if (showOnlyWrongQuestions && wrongQuestions.size() > 0) {
                currentQuestion = wrongQuestions.get(0);
                currentWrongQuestionIndex = 0;
                return currentQuestion;
            } else if (showOnlyWrongQuestions && wrongQuestions.size() == 0) {
                throw new ServiceException("No wrong Questions available");
            } else {
                LOG.info("Get first Question of the Question List.");
                currentQuestion = questionList.get(0);
                currentQuestionIndex = 0;
                wrongQuestions = new ArrayList<>();
                currentWrongQuestionIndex = 0;
                wrongQuestionListCounter = 0;
                return currentQuestion;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void recordCheckedAnswers(Question question, boolean answersCorrect) throws ServiceException {
        learnAlgorithm = learnAlgorithmController.isSelected();
        if (learnAlgorithm) {
            if (answersCorrect) {
                LOG.info("Send to update Map");
                learnAlgorithmService.updateSuccessValue(question);
                if (wrongQuestions.contains(question)) {
                    wrongQuestions.remove(question);
                    currentWrongQuestionIndex--;
                }
            } else if (!answersCorrect) {
                LOG.info("Send to failure Map");
                learnAlgorithmService.updateFailureValue(question);
                if (!wrongQuestions.contains(question)) {
                    wrongQuestions.add(question);

                }
            }
        } else {
            if (!answersCorrect) {
                if (!wrongQuestions.contains(question)) {
                    wrongQuestions.add(question);
                }
            } else {
                if (wrongQuestions.contains(question)) {
                    wrongQuestions.remove(question);
                    currentWrongQuestionIndex--;
                }
            }
        }
    }

    @Override
    public List<Question> getQuestions() {
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
        LOG.info("Turn off Algorithm while its on Exam Mode.");
        currentAlgorithmQuestionIndex = 0;
        showOnlyWrongQuestions = false;
        learnAlgorithm = false;
        learnAlgorithmService.shutdown();
        learnAlgorithmController.reset();
    }

    @Override
    public void setOnlyWrongQuestions(Boolean onlyWrongQuestions) {
        this.showOnlyWrongQuestions = onlyWrongQuestions;
    }

    @Override
    public void setExamMode(boolean examMode) {
        this.examMode = examMode;

    }

    @Override
    public void setExamQuestionnaire(ExamQuestionnaire selectedQuestionnaire) {
        this.currentEQ = selectedQuestionnaire;
    }

    @Override
    public Question getFirstExamQuestion() throws ServiceException {
        return getNextQuestionFromList();
    }


    @Override
    public Question restoreQuestionsAndGetFirst() {
        if (learnAlgorithm&&showOnlyWrongQuestions) {
            LOG.info("Get first Question of the Question List.");
            LOG.info("Revert to first question in the Algorithm List.");
            currentQuestion = questionMap.get(algorithmList.get(currentAlgorithmQuestionIndex));
            wrongQuestions = new ArrayList<>();
            currentWrongQuestionIndex = 0;
            wrongQuestionListCounter = 0;
            showOnlyWrongQuestions = false;
            alertController.setOnlyWrongQuestions(false);
            return currentQuestion;
        }else {
            LOG.info("Get first Question of the Question List. ZEZ");
            currentQuestion = questionList.get(0);
            currentQuestionIndex = 0;
            wrongQuestions = new ArrayList<>();
            currentWrongQuestionIndex = 0;
            wrongQuestionListCounter = 0;
            showOnlyWrongQuestions = false;
            alertController.setOnlyWrongQuestions(false);
            return currentQuestion;
        }
    }

    private void resetWrongQuestionList() {
        alertController.setOnlyWrongQuestions(false);
        showOnlyWrongQuestions = false;

        wrongQuestions.clear();
        currentWrongQuestionIndex = 0;
        wrongQuestionListCounter = 0;
    }


    @Override
    public int getCorrectAnswers() {
        int count = 0;
        for (int i = 0; i < questionList.size(); i++) {
            String givenAnswers = questionList.get(i).getCheckedAnswers();
            String correctAnswers = questionList.get(i).getCorrectAnswers();
            if (givenAnswers.equals(correctAnswers)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getWrongAnswers() {
        int count = 0;
        for (int i = 0; i < questionList.size(); i++) {
            String givenAnswers = questionList.get(i).getCheckedAnswers();
            String correctAnswers = questionList.get(i).getCorrectAnswers();
            if (!givenAnswers.equals(correctAnswers)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public double getPercent() {
        double share = (double) getCorrectAnswers();
        double base = (double) questionList.size();
        double percent = (share / base) * 100.00;
        return percent;
    }

}