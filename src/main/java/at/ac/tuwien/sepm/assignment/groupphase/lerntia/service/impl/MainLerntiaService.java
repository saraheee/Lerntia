package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;


import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.AlertController;
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

    private ILearningQuestionnaireService learningQuestionnaireService;
    private IQuestionService questionService;
    private IQuestionnaireQuestionService questionnaireQuestionService;
    private ILearnAlgorithmService learnAlgorithmService;
    private LearnAlgorithmController learnAlgorithmController;
    private AlertController alertController;

    @Autowired
    public MainLerntiaService(ILearningQuestionnaireService learningQuestionnaireService, IQuestionService questionService,
                              IQuestionnaireQuestionService questionnaireQuestionService, ILearnAlgorithmService learnAlgorithmService,
                              LearnAlgorithmController learnAlgorithmController, AlertController alertController) {
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.questionService = questionService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.learnAlgorithmService = learnAlgorithmService;
        this.learnAlgorithmController = learnAlgorithmController;
        this.alertController = alertController;
    }

    @Override
    public void setCustomExamQuestions(ArrayList<Question> customList) throws ServiceException {
        LOG.info("Set custom Exam Questionnaire");
        stopAlgorithm();
        resetWrongQuestionList();
        questionList = customList;
        listCounter = questionList.size();
        currentQuestionIndex = -1;
        LOG.info("First custom Exam question found.");
    }

    @Override
    public void getQuestionsFromExamQuestionnaire(ExamQuestionnaire eQ) throws ServiceException {
        try {
            //In case that the program switched from learning to exam questionnaire while learn algorithm is still open
            //or if the option was selected that only wrong question are to be shown
            stopAlgorithm();
            resetWrongQuestionList();
            LOG.info("Get questions from an Exam Questionnaire.");
            listCounter = 0;
            questionnaireQuestionsList = new ArrayList<>();
            questionList = new ArrayList<>();
            List<Question> searchParameters = new ArrayList<>();
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQid(eQ.getId());
            questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
            //TODO send updated DATE to ExamQuestionnaireDAO
            getExamQuestionFromList(searchParameters);
            LOG.info("All Exam Questionnaire Questions info found.");
            LOG.info("Send required question search parameters to retrieve");
            questionList = questionService.search(searchParameters);
            for (Question ignored : questionList) {
                listCounter++;
            }
            currentQuestionIndex = -1;
            LOG.info("All Exam Questions set.");
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
        List<QuestionLearnAlgorithm> questionLearnAlgorithmList = new ArrayList<>();
        questionList = new ArrayList<>();
        List<Question> searchParameters = new ArrayList<>();
        QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
        questionnaireQuestion.setQid(lQ.getId());
        questionnaireQuestionsList = questionnaireQuestionService.search(questionnaireQuestion);
        LOG.info("All info regarding Questions found.");
        getLearningQuestionFromList(searchParameters, questionLearnAlgorithmList);
        LOG.info("All questions from the selected LearningQuestionnaire found.");
        LOG.info("Search for questions in the Database.");
        questionList = questionService.search(searchParameters);
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
                    LOG.info("Next Exam question.");
                    return currentQuestion;
                } else {
                    currentQuestion = questionList.get(currentQuestionIndex);
                }
            } catch (IndexOutOfBoundsException e) {
                throw new ServiceException("Reached end of Exam Questionnaire");
            }
        } else {
            try {
                learnAlgorithm = learnAlgorithmController.isSelected();
                if (learnAlgorithm && showOnlyWrongQuestions) {
                    LOG.info("Get next algorithmic Question while the 'Wrong Question List'");
                    if (wrongQuestions.size() != 0) {
                        if (getNextQuestionFromWrongQuestionList()) {
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
                        if (getNextQuestionFromWrongQuestionList()) {
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
            } catch (IndexOutOfBoundsException e) {
                throw new ServiceException("Reached end of Question List");
            }
        }
        return currentQuestion;
    }

    private boolean getNextQuestionFromWrongQuestionList() {
        if (!(currentWrongQuestionIndex + 1 > wrongQuestions.size())) {
            currentQuestion = new Question();
            currentQuestion = wrongQuestions.get(++currentWrongQuestionIndex);
            LOG.info("Found next question that has been answered wrong previously.");
            return true;
        }
        return false;
    }

    @Override
    public Question getPreviousQuestionFromList() throws ServiceException {
        try {
            learnAlgorithm = learnAlgorithmController.isSelected();
            if (learnAlgorithm && showOnlyWrongQuestions) {
                if (!(currentWrongQuestionIndex - 1 > 0)) {
                    currentQuestion = new Question();
                    currentQuestion = wrongQuestions.get(--currentWrongQuestionIndex);
                    LOG.info("Found previous question that has been answered wrong previously.");
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
            throw new ServiceException("Index out of bounds");
        }
    }

    @Override
    public Question loadQuestionnaireAndGetFirstQuestion() throws ServiceException {
        LOG.info("Prepare/reset Question List and prepare first Question in the List.");
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
            throw new ServiceException("No Questionnaire has been selected yet.");
        }
        getQuestionsFromLearningQuestionnaire(currentLQ);
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
            LOG.info("Get first Question");
            currentAlgorithmQuestionIndex = 0;
            currentWrongQuestionIndex = 0;
            if (learnAlgorithm && showOnlyWrongQuestions && wrongQuestions.size() > 0) {
                currentQuestion = wrongQuestions.get(0);
                currentWrongQuestionIndex = 0;
                LOG.info("First Question Found");
                return currentQuestion;
            } else if (learnAlgorithm && showOnlyWrongQuestions) { // wrongQuestions.size() is zero
                currentAlgorithmQuestionIndex = 0;
                throw new ServiceException("No wrong Questions available");
            } else if (learnAlgorithm) {
                currentAlgorithmQuestionIndex = 0;
                LOG.info("Revert to first question in the Algorithm List.");
                currentQuestion = questionMap.get(algorithmList.get(currentAlgorithmQuestionIndex));
                wrongQuestions = new ArrayList<>();
                currentWrongQuestionIndex = 0;
                return currentQuestion;
            } else if (showOnlyWrongQuestions && wrongQuestions.size() > 0) {
                currentQuestion = wrongQuestions.get(0);
                currentWrongQuestionIndex = 0;
                return currentQuestion;
            } else if (showOnlyWrongQuestions) { // wrongQuestions.size() is zero
                throw new ServiceException("No wrong Questions available");
            } else {
                LOG.info("Get first Question of the Question List.");
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
        learnAlgorithm = learnAlgorithmController.isSelected();
        if (learnAlgorithm) {
            if (answersCorrect) {
                LOG.info("Send to update Map");
                learnAlgorithmService.updateSuccessValue(question);
                if (wrongQuestions.contains(question)) {
                    wrongQuestions.remove(question);
                    currentWrongQuestionIndex--;
                }
            } else { // answers not correct
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
    public Question getFirstExamQuestion() throws ServiceException {
        return getNextQuestionFromList();
    }


    @Override
    public Question restoreQuestionsAndGetFirst() {
        if (learnAlgorithm && showOnlyWrongQuestions) {
            LOG.info("Get first Question of the Question List.");
            LOG.info("Revert to first question in the Algorithm List.");
            currentQuestion = questionMap.get(algorithmList.get(currentAlgorithmQuestionIndex));
            wrongQuestions = new ArrayList<>();
            currentWrongQuestionIndex = 0;
            showOnlyWrongQuestions = false;
            alertController.setOnlyWrongQuestions(false);
            return currentQuestion;
        } else {
            LOG.info("Get first Question of the Question List.");
            currentQuestion = questionList.get(0);
            currentQuestionIndex = 0;
            wrongQuestions = new ArrayList<>();
            currentWrongQuestionIndex = 0;
            showOnlyWrongQuestions = false;
            alertController.setOnlyWrongQuestions(false);
            return currentQuestion;
        }
    }

    private void resetWrongQuestionList() {
        LOG.info("Reset Wrong Question List");
        alertController.setOnlyWrongQuestions(false);
        showOnlyWrongQuestions = false;
        wrongQuestions.clear();
        currentWrongQuestionIndex = 0;
    }


    @Override
    public int getCorrectAnswers() {
        int count = 0;
        for (Question aQuestionList : questionList) {
            String givenAnswers = aQuestionList.getCheckedAnswers();
            String correctAnswers = aQuestionList.getCorrectAnswers();
            if (givenAnswers != null && givenAnswers.trim().equals(correctAnswers.trim())) {
                count++;
            }
        }
        LOG.info("Get correct answers count: " + count);
        return count;
    }

    @Override
    public int getWrongAnswers() {
        int count = 0;
        for (Question aQuestionList : questionList) {
            String givenAnswers = aQuestionList.getCheckedAnswers();
            String correctAnswers = aQuestionList.getCorrectAnswers();
            if (givenAnswers != null && !givenAnswers.trim().equals(correctAnswers.trim())) {
                count++;
            }
        }
        count = count - getIgnoredAnswers();
        LOG.info("Get wrong answer count: " + count);
        return count;
    }

    @Override
    public int getIgnoredAnswers() {
        int count = 0;
        for (Question aQuestionList : questionList) {
            String givenAnswers = aQuestionList.getCheckedAnswers();
            if (givenAnswers == null || givenAnswers.trim().equals("")) {
                count++;
            }
        }
        LOG.info("Get ignored answers count: " + count);
        return count;
    }

    @Override
    public double getPercent() {
        double share = (double) getCorrectAnswers();
        double base = (double) questionList.size() - getIgnoredAnswers();
        if (base <= 0) {
            return 0.0;
        }
        double percent = base != 0 ? (share / base) * 100.00 : 0;
        LOG.info("Get Percentage of correctly answered questions");
        int temp = (int) (percent * Math.pow(10, 2));
        return ((double) temp) / Math.pow(10, 2);
    }

}