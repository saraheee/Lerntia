package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.LearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
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

public class QuestionDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionDAO questionDAO;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionDAO(new QuestionDAO(jdbcConnectionManager, new LearnAlgorithmDAO(jdbcConnectionManager)));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
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
    public void createNewQuestion() throws PersistenceException {
        try {

            Question refQuestion = new Question();
            refQuestion.setQuestionText("How you doing");
            refQuestion.setAnswer1("No");
            refQuestion.setAnswer2("yes");
            refQuestion.setCorrectAnswers("1");
            questionDAO.create(refQuestion);

            Long refId = refQuestion.getId();

            Question firstQuestion = new Question();
            firstQuestion.setQuestionText("How you doing");
            firstQuestion.setAnswer1("No");
            firstQuestion.setAnswer2("yes");
            firstQuestion.setCorrectAnswers("1");
            questionDAO.create(firstQuestion);
            Assert.assertEquals(Long.valueOf(refId+1), firstQuestion.getId());
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getCustomMessage());
        }
    }

    @Test(expected = PersistenceException.class)
    public void createNewQuestionError() throws PersistenceException {
        Question errorQuestion = new Question();
        questionDAO.create(errorQuestion);
    }
}
