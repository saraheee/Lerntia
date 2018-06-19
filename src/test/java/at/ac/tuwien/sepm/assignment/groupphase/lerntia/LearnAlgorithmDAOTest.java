package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.LearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;
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

public class LearnAlgorithmDAOTest{

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionDAO questionDAO;
    private ILearnAlgorithmDAO learnAlgorithmDAO;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionDAO(new QuestionDAO(jdbcConnectionManager, new LearnAlgorithmDAO(jdbcConnectionManager)));
            this.ILearnQuestionDAO(new LearnAlgorithmDAO(jdbcConnectionManager));
            LOG.info("Created instances for the new learnAlgorithmDAO");
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
    }

    private void ILearnQuestionDAO(LearnAlgorithmDAO learnAlgorithmDAO) {
        this.learnAlgorithmDAO = learnAlgorithmDAO;
    }

    private void IQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    @Test
    public void createNewQuestionAndAlgorithmValue() throws PersistenceException {
        try {

            Question refQuestion = new Question();
            refQuestion.setQuestionText("Test Question");
            refQuestion.setAnswer1("First answer");
            refQuestion.setAnswer2("Second answer");
            refQuestion.setCorrectAnswers("1");
            questionDAO.create(refQuestion);

            Long refId = refQuestion.getId();

            QuestionLearnAlgorithm refValue = new QuestionLearnAlgorithm();
            QuestionLearnAlgorithm getValue = new QuestionLearnAlgorithm();
            refValue.setID(refQuestion.getId());
            List<QuestionLearnAlgorithm> results = learnAlgorithmDAO.readAll();
            for (QuestionLearnAlgorithm q: results){
                if (refId==q.getID()){
                    getValue.setID(q.getID());
                }
            }

            Assert.assertEquals(refValue.getID(),getValue.getID());


        } catch (PersistenceException e) {
            throw new PersistenceException(e.getCustomMessage());
        }
    }

    @Test(expected = PersistenceException.class)
    public void createNewLearnAlgorithmValueError() throws PersistenceException {
        QuestionLearnAlgorithm error = new QuestionLearnAlgorithm();
        learnAlgorithmDAO.create(error);
    }

    @Test
    public void resetExistingAlgorithmValue()throws PersistenceException{
        Question refQuestion = new Question();
        refQuestion.setQuestionText("Test Question");
        refQuestion.setAnswer1("First answer");
        refQuestion.setAnswer2("Second answer");
        refQuestion.setCorrectAnswers("1");
        questionDAO.create(refQuestion);

        Long refId = refQuestion.getId();

        QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
        questionLearnAlgorithm.setID(refId);
        questionLearnAlgorithm.setPoints(200);
        questionLearnAlgorithm.setFailureValue(15);
        questionLearnAlgorithm.setSuccessValue(12);
        List<QuestionLearnAlgorithm> update = new ArrayList<>();
        update.add(questionLearnAlgorithm);
        learnAlgorithmDAO.update(update);

        learnAlgorithmDAO.reset(questionLearnAlgorithm);

        QuestionLearnAlgorithm refValue = new QuestionLearnAlgorithm();
        QuestionLearnAlgorithm getValue = new QuestionLearnAlgorithm();
        refValue.setID(refQuestion.getId());
        List<QuestionLearnAlgorithm> results = learnAlgorithmDAO.readAll();
        for (QuestionLearnAlgorithm q: results){
            if (refId==q.getID()){
                getValue.setID(q.getID());
                getValue.setSuccessValue(q.getSuccessValue());
                getValue.setFailureValue(q.getFailureValue());
                getValue.setPoints(q.getPoints());
            }
        }

        Assert.assertEquals(refValue.getID(),getValue.getID());
        Assert.assertEquals(100.0,getValue.getPoints(),0);
        Assert.assertEquals(refValue.getFailureValue(),getValue.getFailureValue());
        Assert.assertEquals(refValue.getSuccessValue(),getValue.getSuccessValue());

    }

    @Test
    public void updateLearnAlgorithm() throws PersistenceException{
        Question refQuestion = new Question();
        refQuestion.setQuestionText("Test Question");
        refQuestion.setAnswer1("First answer");
        refQuestion.setAnswer2("Second answer");
        refQuestion.setCorrectAnswers("1");

        questionDAO.create(refQuestion);

        Long refId = refQuestion.getId();
        QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
        questionLearnAlgorithm.setID(refId);
        questionLearnAlgorithm.setPoints(200);
        questionLearnAlgorithm.setFailureValue(15);
        questionLearnAlgorithm.setSuccessValue(12);
        List<QuestionLearnAlgorithm> update = new ArrayList<>();
        update.add(questionLearnAlgorithm);
        learnAlgorithmDAO.update(update);
        List<QuestionLearnAlgorithm> search = new ArrayList<>();
        List<QuestionLearnAlgorithm> results;
        QuestionLearnAlgorithm searchparameter = new QuestionLearnAlgorithm();
        searchparameter.setID(refId);
        search.add(searchparameter);
        results = learnAlgorithmDAO.search(search);

        Assert.assertEquals(searchparameter.getID(),results.get(0).getID());
        Assert.assertEquals(questionLearnAlgorithm.getSuccessValue(),results.get(0).getSuccessValue());
        Assert.assertEquals(questionLearnAlgorithm.getFailureValue(),results.get(0).getFailureValue());
        Assert.assertEquals(questionLearnAlgorithm.getPoints(),results.get(0).getPoints(),0);
    }


}
