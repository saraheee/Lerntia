package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.LearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionDAO;
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
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionDAO(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager)));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    private void IQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    @After
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Test
    public void createNewQuestion() throws PersistenceException {
        try {
            Question firstquestion = new Question();
            firstquestion.setQuestionText("How you doing");
            firstquestion.setAnswer1("No");
            firstquestion.setAnswer2("yes");
            firstquestion.setCorrectAnswers("1");
            questionDAO.create(firstquestion);
            Assert.assertEquals(Long.valueOf(2),firstquestion.getId());
        } catch (PersistenceException e) {
            //throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = PersistenceException.class)
    public void createNewQuestionError() throws PersistenceException {
        try {
            Question errorquestion = new Question();
            questionDAO.create(errorquestion);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }
}
