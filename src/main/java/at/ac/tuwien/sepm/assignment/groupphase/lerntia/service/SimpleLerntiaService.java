package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class SimpleLerntiaService implements LerntiaService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int SLEEP_SECONDS = 2;

    private List<QuestionnaireQuestion> questionnaireQuestionsList;
    private List<Question> questionList;
    private Question currentQuestion;
    private int listcounter = 0;
    private int currentQuestionIndex = 0;

    private CourseDAO courseDAO;
    private UserDAO userDAO;
    private QuestionnaireDAO questionnaireDAO;
    private ExamQuestionaireDAO examQuestionaireDAO;
    private LearningQuestionnaireDAO learningQuestionnaireDAO;
    private QuestionDAO questionDAO;
    private QuestionnaireQuestionDAO questionnaireQuestionDAO;
    private UserCourseDAO userCourseDAO;
    private UserQuestionaireDAO userQuestionaireDAO;


    @Autowired
    public SimpleLerntiaService(CourseDAO courseDAO, UserDAO userDAO, QuestionnaireDAO questionnaireDAO,ExamQuestionaireDAO examQuestionaireDAO,LearningQuestionnaireDAO learningQuestionnaireDAO,QuestionDAO questionDAO,QuestionnaireQuestionDAO questionnaireQuestionDAO,UserCourseDAO userCourseDAO,UserQuestionaireDAO userQuestionaireDAO){
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

    @Override
    public void getQuestionsFromExamQuestionnaire(ExamQuestionnaire eQ) throws ServiceException{
        try {
            questionnaireQuestionsList = new ArrayList<>();
            questionList = new ArrayList<>();
            List<Question> searchparameters = new ArrayList<>();
            Question question;
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQid(eQ.getId());
            questionnaireQuestionsList = questionnaireQuestionDAO.search(questionnaireQuestion);
            //TODO send updated DATE to ExamQuestionnaireDAO
            while (!questionnaireQuestionsList.isEmpty()){
                question = new Question();
                questionnaireQuestion = questionnaireQuestionsList.get(0);
                question.setId(questionnaireQuestion.getQuestionid());
                searchparameters.add(question);
                questionnaireQuestionsList.remove(0);
                LOG.info("ExamQuestionnaire Question found.");
            }
            questionList = questionDAO.search(searchparameters);
            for (Question q:questionList) {
                listcounter++;
            }
        } catch (PersistenceException e) {
           throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public void getQuestionsFromLearningQuestionnaire(LearningQuestionnaire lQ) throws ServiceException{
        try {
            questionnaireQuestionsList = new ArrayList<>();
            questionList = new ArrayList<>();
            List<Question> searchparameters = new ArrayList<>();
            Question question;
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQid(lQ.getId());
            questionnaireQuestionsList = questionnaireQuestionDAO.search(questionnaireQuestion);
            while (!questionnaireQuestionsList.isEmpty()){
                question = new Question();
                questionnaireQuestion = questionnaireQuestionsList.get(0);
                question.setId(questionnaireQuestion.getQuestionid());
                searchparameters.add(question);
                questionnaireQuestionsList.remove(0);
                LOG.info("LearningQuestionnaire Question found.");
            }
            questionList = questionDAO.search(searchparameters);
            for (Question q:questionList) {
                listcounter++;
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }


    }

    @Override
    public Question getQuestion() throws ServiceException {
        try {
            Question question = questionDAO.get(1);
            return question;
            //TODO better implementation after more related stories regarding choosing of question are done.
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
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
}



