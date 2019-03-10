package at.ac.tuwien.lerntia.lerntia;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dao.impl.LearningQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dao.impl.QuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.lerntia.lerntia.service.impl.SimpleLearningQuestionnaireService;
import at.ac.tuwien.lerntia.util.JDBCConnectionManager;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.service.impl.SimpleQuestionnaireService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

public class QuestionnaireServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IQuestionnaireService questionnaireService;
    private Connection connection;
    private final JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();
    private ILearningQuestionnaireService learningQuestionnaireService;

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.ILearningQuestionnaireService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager), jdbcConnectionManager)));
            this.IQuestionnaireService(new SimpleQuestionnaireService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager), jdbcConnectionManager))));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
    }

    private void ILearningQuestionnaireService(ILearningQuestionnaireService simpleLearningQuestionnaireService) {
        this.learningQuestionnaireService = simpleLearningQuestionnaireService;
    }

    private void IQuestionnaireService(IQuestionnaireService simpleQuestionnaireService) {
        this.questionnaireService = simpleQuestionnaireService;
    }

    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    @Test
    public void deselectAllQuestionnaires() {
        try {
            questionnaireService.deselectAllQuestionnaires();
            Assert.assertNull(learningQuestionnaireService.getSelected());
        } catch (ServiceException e) {
            LOG.error("Failed to deselect all questionnaires");
        }
    }
}
