package at.ac.tuwien.lerntia.lerntia.service.impl;


import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.exception.ServiceValidationException;
import at.ac.tuwien.lerntia.lerntia.dto.*;
import at.ac.tuwien.lerntia.lerntia.service.*;
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
    private final ILearningQuestionnaireService learningQuestionnaireService;
    private final IQuestionService questionService;
    private final IQuestionnaireQuestionService questionnaireQuestionService;
    private final ILearnAlgorithmService learnAlgorithmService;
    private final ICourseService courseService;
    private int correctAnswered;
    private int wrongAnswered;
    private Map<Long, Question> questionMap;
    private boolean learnAlgorithm;
    private boolean showOnlyWrongQuestions = false;
    private ExamQuestionnaire currentEQ;
    private List<QuestionnaireQuestion> questionnaireQuestionsList;
    private List<Question> questionList;
    private List<Question> wrongQuestions;
    private List<Long> algorithmList;
    private int algorithmListCounter;
    private int currentAlgorithmQuestionIndex;
    private int currentWrongQuestionIndex;
    private Question currentQuestion;
    private int listCounter;
    private int currentQuestionIndex;
    private boolean examMode;

    @Autowired
    public MainLerntiaService(ILearningQuestionnaireService learningQuestionnaireService, IQuestionService questionService,
                              IQuestionnaireQuestionService questionnaireQuestionService, ILearnAlgorithmService learnAlgorithmService,
                              ICourseService courseService
    ) {
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.questionService = questionService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.learnAlgorithmService = learnAlgorithmService;
        this.courseService = courseService;
        this.correctAnswered = 0;
        this.wrongAnswered = 0;
    }

    @Override
    public void setCustomExamQuestions(ArrayList<Question> customList) throws ServiceException, ServiceValidationException {
        if (customList == null || customList.size() <= 0) {
            throw new ServiceValidationException("Keine Prüfungsfragen in der Liste vorhanden!");
        }
        LOG.info("Set custom exam questionnaire");
        stopAlgorithm();
        resetWrongQuestionList();
        questionList = customList;
        listCounter = questionList.size();
        currentQuestionIndex = -1;
        LOG.debug("First custom exam question found.");
    }

    @Override
    public void getQuestionsFromExamQuestionnaire(ExamQuestionnaire eQ) throws ServiceException {
        try {
            //In case that the program switched from learning to exam questionnaire while learn algorithm is still open
            //or if the option was selected that only wrong question are to be shown
            stopAlgorithm();
            resetWrongQuestionList();
            LOG.info("Get questions from an exam questionnaire.");
            listCounter = 0;
            questionnaireQuestionsList = new ArrayList<>();
            questionList = new ArrayList<>();
            List<Question> searchParameters = new ArrayList<>();
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQid(eQ.getId());
            questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
            getExamQuestionFromList(searchParameters);
            LOG.debug("All exam questionnaire questions info found.");
            LOG.debug("Send required question search parameters to retrieve");
            questionList = questionService.search(searchParameters);
            for (Question ignored : questionList) {
                listCounter++;
            }
            currentQuestionIndex = -1;

            LOG.info("All exam questions set.");
        } catch (Exception e) {
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
        questionMap = new HashMap<>();
        if (algorithmList != null) {
            algorithmList = null;
            algorithmList = new ArrayList<>();
        }
        LOG.debug("Get all questions from a learning questionnaire.");
        listCounter = 0;
        algorithmListCounter = 0;
        questionnaireQuestionsList = new ArrayList<>();
        List<QuestionLearnAlgorithm> questionLearnAlgorithmList = new ArrayList<>();
        questionList = new ArrayList<>();
        List<Question> searchParameters = new ArrayList<>();
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(lQ.getId());
        questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
        LOG.debug("All info regarding questions found.");
        getLearningQuestionFromList(searchParameters, questionLearnAlgorithmList);
        LOG.debug("All questions from the selected learning questionnaire found.");
        LOG.debug("Search for questions in the Database.");
        questionList = questionService.search(searchParameters);
        algorithmList = learnAlgorithmService.prepareQuestionValues(questionLearnAlgorithmList);
        for (Question q : questionList) {
            listCounter++;
            algorithmListCounter++;
            questionMap.put(q.getId(), q);
        }
        LOG.info("All questions from the learning questionnaire are set.");
    }


    @Override
    public Question getNextQuestionFromList() throws ServiceException {
        if (examMode) {
            try {
                LOG.info("Get next exam question from exam questionnaire.");
                if (!(currentQuestionIndex + 1 > listCounter)) {
                    currentQuestion = new Question();
                    currentQuestion = questionList.get(++currentQuestionIndex);
                    LOG.info("Next exam question.");
                    return currentQuestion;
                } else {
                    currentQuestion = questionList.get(currentQuestionIndex);
                }
            } catch (IndexOutOfBoundsException e) {
                throw new ServiceException("Reached end of exam questionnaire");
            }
        } else {
            try {
                if (learnAlgorithm && showOnlyWrongQuestions) {
                    LOG.info("Get next algorithmic question from the 'wrong question list'");
                    if (wrongQuestions.size() != 0) {
                        if (getNextQuestionFromWrongQuestionList()) {
                            return currentQuestion;
                        }
                    } else {
                        stopAlgorithm();
                        resetWrongQuestionList();
                        getFirstQuestion();
                        throw new ServiceException("List of wrong questions is empty");
                    }
                } else if (learnAlgorithm) {
                    if (!(currentAlgorithmQuestionIndex + 1 > algorithmListCounter)) {
                        currentQuestion = new Question();
                        currentQuestion = questionMap.get(algorithmList.get(++currentAlgorithmQuestionIndex));
                        LOG.debug("Found next question determined by learn algorithm.");
                        return currentQuestion;
                    }
                } else if (showOnlyWrongQuestions) {
                    if (!(wrongQuestions.size() == 0)) {
                        if (getNextQuestionFromWrongQuestionList()) {
                            return currentQuestion;
                        }
                    } else {
                        resetWrongQuestionList();
                        getFirstQuestion();
                        throw new ServiceException("List of wrong questions is empty");
                    }
                } else {
                    LOG.info("Get next question from questionnaire.");
                    if (!(currentQuestionIndex + 1 > listCounter)) {
                        currentQuestion = new Question();
                        currentQuestion = questionList.get(++currentQuestionIndex);
                        return currentQuestion;
                    }
                    LOG.debug("Next question.");
                }
                return currentQuestion;
            } catch (IndexOutOfBoundsException e) {
                throw new ServiceException("Reached end of question list");
            }
        }
        return currentQuestion;
    }

    private boolean getNextQuestionFromWrongQuestionList() {
        if (!(currentWrongQuestionIndex + 1 > wrongQuestions.size())) {
            currentQuestion = new Question();
            currentQuestion = wrongQuestions.get(++currentWrongQuestionIndex);
            LOG.debug("Found next question that has been answered wrong previously.");
            return true;
        }
        return false;
    }

    @Override
    public Question getPreviousQuestionFromList() throws ServiceException {
        try {
            if (learnAlgorithm && showOnlyWrongQuestions) {
                if (!(currentWrongQuestionIndex - 1 > 0)) {
                    currentQuestion = new Question();
                    currentQuestion = wrongQuestions.get(--currentWrongQuestionIndex);
                    LOG.debug("Found previous question that has been answered wrong previously.");
                    return currentQuestion;
                }
            } else if (learnAlgorithm) {
                LOG.debug("Get previous question determined by the Learn Algorithm");
                if (!(currentAlgorithmQuestionIndex - 1 < 0)) {
                    currentQuestion = new Question();
                    currentQuestion = questionMap.get(algorithmList.get(--currentAlgorithmQuestionIndex));
                    LOG.debug("Found previous question determined by the learn algorithm.");
                    return currentQuestion;
                }
            } else if (showOnlyWrongQuestions) {
                if (!(currentWrongQuestionIndex - 1 < 0)) {
                    currentQuestion = new Question();
                    currentQuestion = wrongQuestions.get(--currentWrongQuestionIndex);
                    LOG.debug("Found previous question that has been answered wrong previously.");
                    return currentQuestion;
                }
            } else {
                LOG.debug("Get previous question from questionnaire");
                if (!(currentQuestionIndex - 1 < 0)) {
                    currentQuestion = new Question();
                    currentQuestion = questionList.get(--currentQuestionIndex);
                    return currentQuestion;
                }
                LOG.debug("Previous Question.");
            }
            return currentQuestion;
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException("Index out of bounds");
        }
    }

    @Override
    public Question loadQuestionnaireAndGetFirstQuestion() throws ServiceException {
        LOG.debug("Reset question list and prepare first question in the list.");
        if (wrongQuestions == null) {
            wrongQuestions = new ArrayList<>();
            currentWrongQuestionIndex = 0;
        } else {
            resetWrongQuestionList();
        }
        LearningQuestionnaire currentLQ = learningQuestionnaireService.getSelected();
        if (currentEQ != null) {
            currentEQ = null;
        }
        if (currentLQ == null) {
            throw new ServiceException("No questionnaire has been selected yet.");
        }
        getQuestionsFromLearningQuestionnaire(currentLQ);
        if (!learnAlgorithm) {
            currentQuestionIndex = -1;
            currentAlgorithmQuestionIndex = 0;
        } else {
            currentQuestionIndex = 0;
            currentAlgorithmQuestionIndex = -1;
        }
        LOG.debug("First question found.");
        return getNextQuestionFromList();
    }

    @Override
    public Question getFirstQuestion() throws ServiceException {
        try {
            LOG.debug("Get first question");
            currentAlgorithmQuestionIndex = 0;
            currentWrongQuestionIndex = 0;
            if (learnAlgorithm && showOnlyWrongQuestions && wrongQuestions.size() > 0) {
                currentQuestion = wrongQuestions.get(0);
                currentWrongQuestionIndex = 0;
                LOG.debug("First question found");
                return currentQuestion;
            } else if (learnAlgorithm && showOnlyWrongQuestions) { // wrongQuestions.size() is zero
                currentAlgorithmQuestionIndex = 0;
                throw new ServiceException("No wrong questions available");
            } else if (learnAlgorithm) {
                currentAlgorithmQuestionIndex = 0;
                LOG.debug("Revert to first question in the algorithm list.");
                currentQuestion = questionMap.get(algorithmList.get(currentAlgorithmQuestionIndex));
                wrongQuestions = new ArrayList<>();
                currentWrongQuestionIndex = 0;
                return currentQuestion;
            } else if (showOnlyWrongQuestions && wrongQuestions.size() > 0) {
                currentQuestion = wrongQuestions.get(0);
                currentWrongQuestionIndex = 0;
                return currentQuestion;
            } else if (showOnlyWrongQuestions) { // wrongQuestions.size() is zero
                throw new ServiceException("No wrong questions available");
            } else {
                LOG.debug("Get first question of the question list.");
                currentQuestion = questionList.get(0);
                currentQuestionIndex = 0;
                wrongQuestions = new ArrayList<>();
                currentWrongQuestionIndex = 0;
                return currentQuestion;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException("Index out of bounds");
        }
    }

    @Override
    public void recordCheckedAnswers(Question question, boolean answersCorrect) throws ServiceException {
        LOG.info("Record question answering values.");
        if (learnAlgorithm) {
            if (answersCorrect) {
                correctAnswered++;
                LOG.info("Send to update map");
                learnAlgorithmService.updateSuccessValue(question);
                if (wrongQuestions.contains(question)) {
                    wrongQuestions.remove(question);
                    currentWrongQuestionIndex--;
                }
            } else { // answers not correct
                LOG.info("Send to failure map");
                wrongAnswered++;
                learnAlgorithmService.updateFailureValue(question);
                if (!wrongQuestions.contains(question)) {
                    wrongQuestions.add(question);
                }
            }
        } else {
            if (!answersCorrect) {
                wrongAnswered++;
                if (!wrongQuestions.contains(question)) {
                    wrongQuestions.add(question);
                }
            } else { // answers correct
                correctAnswered++;
                if (wrongQuestions.contains(question)) {
                    wrongQuestions.remove(question);
                    currentWrongQuestionIndex--;
                }
            }
        }
    }

    @Override
    public List<Question> getQuestions() {
        LOG.debug("Get all questions.");
        return questionList;
    }

    @Override
    public List<Question> getQuestionList() {
        return this.questionList;
    }

    @Override
    public void stopAlgorithm() throws ServiceException {
        LOG.debug("Turn off algorithm.");
        currentAlgorithmQuestionIndex = 0;
        showOnlyWrongQuestions = false;
        learnAlgorithm = false;
        learnAlgorithmService.shutdown();

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
    public Question getFirstExamQuestion() throws ServiceException {
        return getNextQuestionFromList();
    }


    @Override
    public Question restoreQuestionsAndGetFirst() {
        if (learnAlgorithm && showOnlyWrongQuestions) {
            LOG.debug("Get first question of the question list.");
            LOG.debug("Revert to first question in the algorithm list.");
            currentQuestion = questionMap.get(algorithmList.get(currentAlgorithmQuestionIndex));
            wrongQuestions = new ArrayList<>();
            currentWrongQuestionIndex = 0;
            showOnlyWrongQuestions = false;
            return currentQuestion;
        } else {
            LOG.debug("Get first question of the question list.");
            currentQuestion = questionList.get(0);
            currentQuestionIndex = 0;
            wrongQuestions = new ArrayList<>();
            currentWrongQuestionIndex = 0;
            showOnlyWrongQuestions = false;
            return currentQuestion;
        }
    }

    private void resetWrongQuestionList() {
        LOG.info("Reset wrong question list");
        showOnlyWrongQuestions = false;
        if (wrongQuestions != null) {
            wrongQuestions.clear();
        }
        currentWrongQuestionIndex = 0;
    }


    @Override
    public int getCorrectAnswers() {
        return correctAnswered;
    }

    @Override
    public int getWrongAnswers() {
        return wrongAnswered;
    }

    @Override
    public int getIgnoredAnswers() {
        return questionList.size() - correctAnswered - wrongAnswered > 0 ? questionList.size() - correctAnswered - wrongAnswered : 0;
    }

    public int getWrongIgnoredAnswers() {
        return wrongQuestions.size() - correctAnswered - wrongAnswered > 0 ? wrongQuestions.size() - correctAnswered - wrongAnswered : 0;
    }

    @Override
    public int getIgnoredExamAnswers() {
        int count = 0;
        for (Question aQuestionList : questionList) {
            if (aQuestionList.getCheckedAnswers().equals("")) {
                count++;
            }
        }
        return count;
    }

    @Override
    public double getPercent() {
        double share = (double) getCorrectAnswers();
        double base = (double) getCorrectAnswers() + getWrongAnswers();
        double percent = base <= 0 ? 0 : (share / base) * 100.00;
        int temp = (int) (percent * Math.pow(10, 2));
        double result = ((double) temp) / Math.pow(10, 2);
        result = result > 100 ? 100 : result < 0 ? 0 : result;
        LOG.info("Percentage of correctly answered questions: " + result);
        LOG.info("Correct: " + getCorrectAnswers() + ", Wrong: " + getWrongAnswers() + ", Skipped: " + getIgnoredAnswers());
        return result;
    }

    @Override
    public List<Question> getWrongQuestionList() {
        return wrongQuestions;
    }

    @Override
    public void resetCounter() {
        wrongAnswered = 0;
        correctAnswered = 0;
    }

    @Override
    public void setLearnAlgorithmStatus(boolean b) {
        LOG.info("Set learn algorithm status to: " + b);
        this.learnAlgorithm = b;
    }
}