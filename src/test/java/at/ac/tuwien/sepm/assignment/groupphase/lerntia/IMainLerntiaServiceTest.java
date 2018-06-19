package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearnAlgorithmService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ILearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.LearnAlgorithmService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleLearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionnaireQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.AlertController;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.LearnAlgorithmController;
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

import static org.hamcrest.core.Is.is;

public class IMainLerntiaServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;

    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();
    private QuestionnaireDAO questionnaireDAO;
    private ILearningQuestionnaireService learningQuestionnaireService;
    private IQuestionService questionService;
    private IQuestionnaireQuestionService questionnaireQuestionService;
    private ILearnAlgorithmService learnAlgorithmService;
    private LearnAlgorithmController learnAlgorithmController;
    private AlertController alertController;

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
            this.IQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager));
            this.IMainLerntiaService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(questionnaireDAO, jdbcConnectionManager)),
                new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager, new LearnAlgorithmDAO(jdbcConnectionManager))),
                new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager)),
                new LearnAlgorithmService(new LearnAlgorithmDAO(jdbcConnectionManager)),
                new LearnAlgorithmController(), new AlertController());

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
    }

    private void IQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }

    private void IMainLerntiaService(ILearningQuestionnaireService learningQuestionnaireService, IQuestionService questionService,
                                     IQuestionnaireQuestionService questionnaireQuestionService, ILearnAlgorithmService learnAlgorithmService,
                                     LearnAlgorithmController learnAlgorithmController, AlertController alertController) {
        this.learningQuestionnaireService = learningQuestionnaireService;
        this.questionService = questionService;
        this.questionnaireQuestionService = questionnaireQuestionService;
        this.learnAlgorithmService = learnAlgorithmService;
        this.learnAlgorithmController = learnAlgorithmController;
        this.alertController = alertController;
    }

    @Test // TODO: remove this test after testing the class MainLerntiaService
    public void testSimpleLerntiaService() throws ServiceException {
        Assert.assertThat(42, is(42));
    }

}
