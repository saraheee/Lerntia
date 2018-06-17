package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.LearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearnAlgorithmService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.LearnAlgorithmService;
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
import java.util.ArrayList;
import java.util.List;

public class LearnAlgorithmServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ILearnAlgorithmService learnAlgorithmService;
    private Connection connection;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();
    private IQuestionDAO questionDAO;
    private ILearnAlgorithmDAO learnAlgorithmDAO;
    private Question refQuestion;


    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.ILearnAlgorithmService(new LearnAlgorithmService(new LearnAlgorithmDAO(jdbcConnectionManager)));
            this.IQuestionDAO(new QuestionDAO(jdbcConnectionManager, new LearnAlgorithmDAO(jdbcConnectionManager)));
            this.ILearnAlgorithmDAO(new LearnAlgorithmDAO(jdbcConnectionManager));

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
    }

    private void ILearnAlgorithmDAO(LearnAlgorithmDAO learnAlgorithmDAO) {
        this.learnAlgorithmDAO = learnAlgorithmDAO;
    }

    private void IQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO=questionDAO;
    }

    private void ILearnAlgorithmService(LearnAlgorithmService learnAlgorithmService) {
        this.learnAlgorithmService=learnAlgorithmService;
    }

    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    @Test
    public void prepareAlgorithmMap(){
        try {
            refQuestion = new Question();
            refQuestion.setQuestionText("How you doing");
            refQuestion.setAnswer1("No");
            refQuestion.setAnswer2("yes");
            refQuestion.setCorrectAnswers("1");
            questionDAO.create(refQuestion);
            QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
            questionLearnAlgorithm.setID(refQuestion.getId());
            List<QuestionLearnAlgorithm> value = new ArrayList<>();
            value.add(questionLearnAlgorithm);
            List<Long> result = learnAlgorithmService.prepareQuestionValues(value);

            Assert.assertEquals(1,result.size());
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

     @Test(expected = NullPointerException.class)
    public void prepareAlgorithMapError(){
         try {
            List<QuestionLearnAlgorithm> error = null;
            learnAlgorithmService.prepareQuestionValues(error);
         } catch (ServiceException e) {
             e.printStackTrace();
         }
     }

    @Test
    public void addSuccessValue(){
        try {
            prepareAlgorithmMap();
            learnAlgorithmService.updateSuccessValue(refQuestion);
            List<QuestionLearnAlgorithm> result = learnAlgorithmDAO.readAll();
            for (QuestionLearnAlgorithm q: result){
                if (q.getID() == refQuestion.getId()){
                    QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
                    questionLearnAlgorithm.setSuccessValue(2);
                    questionLearnAlgorithm.setFailureValue(0);
                    questionLearnAlgorithm.setPoints(102);
                    Assert.assertEquals(questionLearnAlgorithm.getSuccessValue(),q.getSuccessValue());
                    Assert.assertEquals(questionLearnAlgorithm.getFailureValue(),q.getFailureValue());
                    Assert.assertEquals(questionLearnAlgorithm.getPoints(),q.getPoints(),0);
                }
            }
            learnAlgorithmService.changeAlgorithmValues();
        }catch (ServiceException e){
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addFailureValue(){
        try {
            prepareAlgorithmMap();
            learnAlgorithmService.updateFailureValue(refQuestion);
            List<QuestionLearnAlgorithm> result = learnAlgorithmDAO.readAll();
            for (QuestionLearnAlgorithm q: result){
                if (q.getID() == refQuestion.getId()){
                    QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
                    questionLearnAlgorithm.setSuccessValue(0);
                    questionLearnAlgorithm.setFailureValue(1);
                    questionLearnAlgorithm.setPoints(99);
                    Assert.assertEquals(questionLearnAlgorithm.getSuccessValue(),q.getSuccessValue());
                    Assert.assertEquals(questionLearnAlgorithm.getFailureValue(),q.getFailureValue());
                    Assert.assertEquals(questionLearnAlgorithm.getPoints(),q.getPoints(),0);
                }
            }
            learnAlgorithmService.changeAlgorithmValues();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runAllFailureValues() {
        try {
            prepareAlgorithmMap();
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);

            List<QuestionLearnAlgorithm> result = learnAlgorithmDAO.readAll();
            for (QuestionLearnAlgorithm q : result) {
                if (q.getID() == refQuestion.getId()) {
                    QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
                    questionLearnAlgorithm.setSuccessValue(0);
                    questionLearnAlgorithm.setFailureValue(10);
                    questionLearnAlgorithm.setPoints(0);
                    Assert.assertEquals(questionLearnAlgorithm.getSuccessValue(), q.getSuccessValue());
                    Assert.assertEquals(questionLearnAlgorithm.getFailureValue(), q.getFailureValue());
                    Assert.assertEquals(questionLearnAlgorithm.getPoints(), q.getPoints(), 0);
                }
            }
            prepareAlgorithmMap();
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);

            List<QuestionLearnAlgorithm> result2 = learnAlgorithmDAO.readAll();
            for (QuestionLearnAlgorithm q : result2) {
                if (q.getID() == refQuestion.getId()) {
                    QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
                    questionLearnAlgorithm.setSuccessValue(0);
                    questionLearnAlgorithm.setFailureValue(10);
                    questionLearnAlgorithm.setPoints(41);
                    Assert.assertEquals(questionLearnAlgorithm.getSuccessValue(), q.getSuccessValue());
                    Assert.assertEquals(questionLearnAlgorithm.getFailureValue(), q.getFailureValue());
                    Assert.assertEquals(questionLearnAlgorithm.getPoints(), q.getPoints(), 0);
                }
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }
        @Test
    public void runAllSuccessvalues(){
        try {
            prepareAlgorithmMap();
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            List<QuestionLearnAlgorithm> result = learnAlgorithmDAO.readAll();
            for (QuestionLearnAlgorithm q: result){
                if (q.getID() == refQuestion.getId()){
                    QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
                    questionLearnAlgorithm.setSuccessValue(10);
                    questionLearnAlgorithm.setFailureValue(0);
                    questionLearnAlgorithm.setPoints(200);
                    Assert.assertEquals(questionLearnAlgorithm.getSuccessValue(),q.getSuccessValue());
                    Assert.assertEquals(questionLearnAlgorithm.getFailureValue(),q.getFailureValue());
                    Assert.assertEquals(questionLearnAlgorithm.getPoints(),q.getPoints(),0);
                }
            }
            prepareAlgorithmMap();
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateFailureValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            learnAlgorithmService.updateSuccessValue(refQuestion);
            List<QuestionLearnAlgorithm> result2 = learnAlgorithmDAO.readAll();
            for (QuestionLearnAlgorithm q: result2){
                if (q.getID() == refQuestion.getId()){
                    QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
                    questionLearnAlgorithm.setSuccessValue(10);
                    questionLearnAlgorithm.setFailureValue(0);
                    questionLearnAlgorithm.setPoints(171);
                    Assert.assertEquals(questionLearnAlgorithm.getSuccessValue(),q.getSuccessValue());
                    Assert.assertEquals(questionLearnAlgorithm.getFailureValue(),q.getFailureValue());
                    Assert.assertEquals(questionLearnAlgorithm.getPoints(),q.getPoints(),0);
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void algorithmSuccesUpdateError(){
        try {
            Question question = new Question();
            question.setQuestionText("How you doing");
            question.setAnswer1("No");
            question.setAnswer2("yes");
            question.setCorrectAnswers("1");

            learnAlgorithmService.updateFailureValue(question);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void algorithmFailureUpdateError(){
        try {
            Question question = new Question();
            question.setQuestionText("How you doing");
            question.setAnswer1("No");
            question.setAnswer2("yes");
            question.setAnswer3("Maybe");
            question.setCorrectAnswers("1");


            learnAlgorithmService.updateFailureValue(question);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shutdownTest(){
        try {
            learnAlgorithmService.shutdown();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
