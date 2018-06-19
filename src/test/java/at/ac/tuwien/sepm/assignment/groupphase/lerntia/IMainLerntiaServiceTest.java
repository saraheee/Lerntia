package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.*;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class IMainLerntiaServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private LerntiaMainController lerntiaMainController;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();
    private QuestionnaireDAO questionnaireDAO;
    private ICourseDAO courseDAO;
    private ExamQuestionnaireDAO examQuestionnaireDAO;
    private ILearningQuestionnaireService learningQuestionnaireService;
    private IQuestionService questionService;
    private IQuestionnaireQuestionService questionnaireQuestionService;
    private ILearnAlgorithmService learnAlgorithmService;
    private LearnAlgorithmController learnAlgorithmController;
    private AlertController alertController;
    private IMainLerntiaService mainLerntiaService;
    private QuestionnaireQuestionDAO questionnaireQuestionDAO;
    private QuestionDAO questionDAO;
    private LearningQuestionnaireDAO learningQuestionnaireDAO;
    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionDAO(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager)));
            this.IquestionnaireQuestionDAO(new QuestionnaireQuestionDAO(jdbcConnectionManager));
            this.ICourseDAO(new CourseDAO(jdbcConnectionManager));
            this.ILearnQuestionnaireDAO(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager));
            this.IExamQuestionnaireDAO(new ExamQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager));
            this.LearnAlgorithmController(new LearnAlgorithmController(new MainLerntiaService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)),
                new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager))),
                new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager)),
                new LearnAlgorithmService(new LearnAlgorithmDAO(jdbcConnectionManager)))));
            this.IQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager));

            this.IMainLerntiaService(new MainLerntiaService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)),
                  new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager))),
                new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager)),
                new LearnAlgorithmService(new LearnAlgorithmDAO(jdbcConnectionManager))));


            this.LerntiaMainController(new LerntiaMainController(new MainLerntiaService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)),
                new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager))),
                new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager)),
                new LearnAlgorithmService(new LearnAlgorithmDAO(jdbcConnectionManager))),

                new AudioController(new SimpleTextToSpeechService(),new AlertController()),
                new AlertController(),
                new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)),
                new ZoomedImageController(new AlertController(),new WindowController(),new AudioController(new SimpleTextToSpeechService(),new AlertController())),
                new SimpleExamResultsWriterService(new ExamResultsWriterDAO()),
                new LearnAlgorithmController(new MainLerntiaService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)),
                    new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager))),
                    new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager)),
                    new LearnAlgorithmService(new LearnAlgorithmDAO(jdbcConnectionManager)))),
                new DirectoryChooserController(),
                new SimpleUserService()
                ));
         //
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
    }

    private void ILearnQuestionnaireDAO(LearningQuestionnaireDAO learningQuestionnaireDAO) {
        this.learningQuestionnaireDAO = learningQuestionnaireDAO;
    }

    private void IQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    private void IquestionnaireQuestionDAO(QuestionnaireQuestionDAO questionnaireQuestionDAO) {
        this.questionnaireQuestionDAO = questionnaireQuestionDAO;
    }

    private void IExamQuestionnaireDAO(ExamQuestionnaireDAO examQuestionnaireDAO) {
        this.examQuestionnaireDAO = examQuestionnaireDAO;
    }

    private void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void LearnAlgorithmController(LearnAlgorithmController learnAlgorithmController) {
        this.learnAlgorithmController = learnAlgorithmController;
    }

    private void LerntiaMainController(LerntiaMainController lerntiaMainController) {
        this.lerntiaMainController = lerntiaMainController;
    }

    private void IMainLerntiaService(MainLerntiaService mainLerntiaService) {
        this.mainLerntiaService = mainLerntiaService;
    }

    private void IQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }


    @Test
    public void setCustomQuestionList(){
        try{
        Question q1 = new Question();
        q1.setId(Long.valueOf(1));
        q1.setQuestionText("What is wrong?");
        q1.setAnswer1("Nothing");
        q1.setAnswer2("Everything");
        ArrayList<Question> list = new ArrayList<>();
        list.add(q1);
        mainLerntiaService.setExamMode(true);
        mainLerntiaService.setCustomExamQuestions(list);

        List<Question> questionList = mainLerntiaService.getQuestionList();
        Assert.assertEquals(1,questionList.size());
        } catch (ServiceException e) {
            LOG.error("Failed to get question list");
        }
    }

    @Test(expected = NullPointerException.class)
    public void errorCustomQuestionList(){
        try {
            mainLerntiaService.setExamMode(true);
            ArrayList<Question> list = null;
            mainLerntiaService.setCustomExamQuestions(list);
        } catch (ServiceException e) {
            LOG.error("Failed to set exam questionnaire");
        }
    }

    @Test
    public void getFirstQuestionsFromExamQuestionnaire(){
        try {
            Course course = new Course();
            course.setMark("111.199");
            course.setSemester("2010W");
            course.setName("TGI");
            courseDAO.create(course);
            ExamQuestionnaire examQuestionnaire = new ExamQuestionnaire();
            examQuestionnaire.setName("Exam Questionnaire");
            examQuestionnaire.setCourseID(course.getId());
            examQuestionnaire.setDate(LocalDate.now());
            examQuestionnaireDAO.create(examQuestionnaire);

            Question question = new Question();
            question.setQuestionText("Random QuestionText");
            question.setAnswer1("Answer1");
            question.setAnswer2("Answer2");
            question.setCorrectAnswers("1");
            questionDAO.create(question);

            Question question2 = new Question();
            question2.setQuestionText("Exam Question");
            question2.setAnswer1("Answer1");
            question2.setAnswer2("Answer2");
            question2.setCorrectAnswers("2");
            questionDAO.create(question2);

            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQid(examQuestionnaire.getId());
            questionnaireQuestion.setQuestionid(question.getId());
            questionnaireQuestion.setCmark(course.getMark());
            questionnaireQuestion.setSemester(course.getSemester());
            questionnaireQuestionDAO.create(questionnaireQuestion);
            questionnaireQuestion.setQuestionid(question2.getId());
            questionnaireQuestionDAO.create(questionnaireQuestion);

            mainLerntiaService.getQuestionsFromExamQuestionnaire(examQuestionnaire);
            List<Question> list = mainLerntiaService.getQuestionList();
            Assert.assertEquals(question.getId(),list.get(0).getId());
            Assert.assertEquals(2,list.size());
        } catch (PersistenceException e) {
            LOG.error("Failed to get first question form exam questionnaire");
        } catch (ServiceException e) {
            LOG.error("Failed to get first question form exam questionnaire");
        }
    }
    @Test
    public void  TestToGetNextQuestionfromListInExamMode(){
        try {

            getFirstQuestionsFromExamQuestionnaire();
            mainLerntiaService.setExamMode(true);
            List<Question> list = mainLerntiaService.getQuestionList();
            Assert.assertEquals(2,list.size());
            System.out.println(list.get(1).toString());
            mainLerntiaService.getNextQuestionFromList();
            Question nextQuestion = mainLerntiaService.getNextQuestionFromList();
            System.out.println(nextQuestion.toString());
            Assert.assertEquals("Exam Question",nextQuestion.getQuestionText());
            mainLerntiaService.setExamMode(false);
        } catch (ServiceException e) {
            LOG.error("Failed to get next question form exam questionnaire");
        }
    }

    @Test
    public void getFirstQuestionsFromLearningQuestionnaire(){
        try {
            Course course = new Course();
            course.setMark("222.199");
            course.setSemester("2013W");
            course.setName("Algodaten");
            courseDAO.create(course);
            LearningQuestionnaire learningQuestionnaire = new LearningQuestionnaire();
            learningQuestionnaire.setName("Learning Questionnaire");
            learningQuestionnaire.setCourseID(course.getId());

            learningQuestionnaireDAO.create(learningQuestionnaire);
            learningQuestionnaireDAO.select(learningQuestionnaire);
            Question question = new Question();
            question.setQuestionText("Random QuestionText");
            question.setAnswer1("Answer1");
            question.setAnswer2("Answer2");
            question.setCorrectAnswers("1");
            questionDAO.create(question);

            Question question2 = new Question();
            question2.setQuestionText("Random QuestionText2");
            question2.setAnswer1("Ans1wer1");
            question2.setAnswer2("Answ1er2");
            question2.setCorrectAnswers("1");
            questionDAO.create(question2);


            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQid(learningQuestionnaire.getId());
            questionnaireQuestion.setQuestionid(question.getId());
            questionnaireQuestion.setCmark(course.getMark());
            questionnaireQuestion.setSemester(course.getSemester());
            questionnaireQuestionDAO.create(questionnaireQuestion);
            questionnaireQuestion.setQuestionid(question2.getId());
            questionnaireQuestionDAO.create(questionnaireQuestion);

            Question firstQuestion = mainLerntiaService.loadQuestionnaireAndGetFirstQuestion();
            Assert.assertEquals(question.getId(),firstQuestion.getId());
            questionnaireDAO.deselect(learningQuestionnaire);
        } catch (PersistenceException e) {
            LOG.error("Failed to get first question form learning questionnaire");
        } catch (ServiceException e) {
            LOG.error("Failed to get first question form learning questionnaire");
        }
    }

    @Test
    public void  TestToGetNextQuestionfromList(){
        try {
            getFirstQuestionsFromLearningQuestionnaire();
            List<Question> list = mainLerntiaService.getQuestionList();
            Assert.assertEquals(2,list.size());

            Question nextQuestion = mainLerntiaService.getNextQuestionFromList();
            Assert.assertEquals("Random QuestionText2",nextQuestion.getQuestionText());
        } catch (ServiceException e) {
            LOG.error("Failed to get next question form list");
        }
    }

    @Test
    public void testAlgorithmShutDown(){
        try {
            mainLerntiaService.stopAlgorithm();
            Assert.assertEquals(false,learnAlgorithmController.isSelected());
        } catch (ServiceException e) {
            LOG.error("Failed to shut down learn algorithm");
        }
    }

    @Test
    public void revertBackToFirstQuestion(){
        try {
            TestToGetNextQuestionfromList();
            Question firstQuestion = mainLerntiaService.getFirstQuestion();
            Assert.assertEquals("Random QuestionText",firstQuestion.getQuestionText());
        } catch (ServiceException e) {
            LOG.error("Failed to revert back to first question");
        }
    }

    @Test
    public void getPreviousQuestion(){
        try {
            TestToGetNextQuestionfromList();
            Question firstQuestion = mainLerntiaService.getPreviousQuestionFromList();
            Assert.assertEquals("Random QuestionText",firstQuestion.getQuestionText());
        } catch (ServiceException e) {
            LOG.error("Failed to get previous question");
        }
    }

    @Test
    public void setQuestioninWrongQuestionList(){
        try {
            getFirstQuestionsFromLearningQuestionnaire();
            Question question = mainLerntiaService.getFirstQuestion();
            mainLerntiaService.recordCheckedAnswers(question,false);
            Assert.assertEquals(1,mainLerntiaService.getWrongQuestionList().size());
        } catch (ServiceException e) {
            LOG.error("Failed to set question in wrong question list");
        }

    }

    @Test
    public void WrongQuestionListError(){
        Assert.assertNull(mainLerntiaService.getWrongQuestionList());
    }

    @Test
    public void checkSkippedQuestions(){
        try {
            getFirstQuestionsFromLearningQuestionnaire();
            Question question = mainLerntiaService.getNextQuestionFromList();
            mainLerntiaService.recordCheckedAnswers(question,true);
            int i = mainLerntiaService.getIgnoredAnswers();
            Assert.assertEquals(1,i);
        } catch (ServiceException e) {
            LOG.error("Failed to check skipped questions");
        }
    }

    @Test(expected = NullPointerException.class)
    public void checkSkippedQuestionsError(){
       int i = mainLerntiaService.getIgnoredAnswers();
    }

    @Test
    public void checkCorrectlyAnsweredQuestions(){
        try {
            getFirstQuestionsFromLearningQuestionnaire();
            Question question = mainLerntiaService.getFirstQuestion();
            mainLerntiaService.recordCheckedAnswers(question,true);
            question = mainLerntiaService.getNextQuestionFromList();
            mainLerntiaService.recordCheckedAnswers(question,true);
            int i = mainLerntiaService.getCorrectAnswers();
            Assert.assertEquals(2,i);
        } catch (ServiceException e) {
            LOG.error("Failed to check correctly answered questions");
        }
    }

    @Test
    public void checkWronglyAnsweredQuestions(){
        try {
            getFirstQuestionsFromLearningQuestionnaire();
            Question question = mainLerntiaService.getFirstQuestion();
            mainLerntiaService.recordCheckedAnswers(question,false);
            question = mainLerntiaService.getNextQuestionFromList();
            mainLerntiaService.recordCheckedAnswers(question,false);
            int i = mainLerntiaService.getWrongAnswers();
            Assert.assertEquals(2,i);
        } catch (ServiceException e) {
            LOG.error("Failed to check wrongly answered questions");
        }
    }

    @Test
    public void checkProcentAnsweredQuestions(){
        try {
            getFirstQuestionsFromLearningQuestionnaire();
            Question question = mainLerntiaService.getFirstQuestion();
            mainLerntiaService.recordCheckedAnswers(question,true);
            question = mainLerntiaService.getNextQuestionFromList();
            mainLerntiaService.recordCheckedAnswers(question,false);
            int i = mainLerntiaService.getWrongAnswers();
            Assert.assertEquals(1,i);
            Double x = mainLerntiaService.getPercent();
            Assert.assertEquals(50.0,x,0);
        } catch (ServiceException e) {
            LOG.error("Failed to check procent answered questions");
        }
    }

    @Test
    public void resetWrongAndCorrectSelectedQuestions(){
       try {
           getFirstQuestionsFromLearningQuestionnaire();
           Question question = mainLerntiaService.getFirstQuestion();
           mainLerntiaService.recordCheckedAnswers(question, true);
           question = mainLerntiaService.getNextQuestionFromList();
           mainLerntiaService.recordCheckedAnswers(question, false);
           int i = mainLerntiaService.getWrongAnswers();
           Assert.assertEquals(1, i);
           i = mainLerntiaService.getCorrectAnswers();
           Assert.assertEquals(1,i);
           mainLerntiaService.resetCounter();
           Assert.assertEquals(0,mainLerntiaService.getWrongAnswers());
           Assert.assertEquals(0,mainLerntiaService.getCorrectAnswers());
       } catch (ServiceException e) {
           LOG.error("Failed to reset wrong and correct selected questions");
       }
    }
}
