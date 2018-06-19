package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
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
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;

public class IMainLerntiaServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private LerntiaMainController lerntiaMainController;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();
    private QuestionnaireDAO questionnaireDAO;
    private ILearningQuestionnaireService learningQuestionnaireService;
    private IQuestionService questionService;
    private IQuestionnaireQuestionService questionnaireQuestionService;
    private ILearnAlgorithmService learnAlgorithmService;
    private LearnAlgorithmController learnAlgorithmController;
    private AlertController alertController;
    private IMainLerntiaService mainLerntiaService;

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
            this.IMainLerntiaService(new MainLerntiaService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)),
                new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager))),
                new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager)),
                new LearnAlgorithmService(new LearnAlgorithmDAO(jdbcConnectionManager)),
                new LearnAlgorithmController(),
                new AlertController()));

            this.LerntiaMainController(new LerntiaMainController(new MainLerntiaService(new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)),
                new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager))),
                new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager)),
                new LearnAlgorithmService(new LearnAlgorithmDAO(jdbcConnectionManager)),
                new LearnAlgorithmController(),
                new AlertController()),

                new AudioController(new SimpleTextToSpeechService(),new AlertController()),
                new AlertController(),
                new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)),
                new ZoomedImageController(new AlertController(),new WindowController(),new AudioController(new SimpleTextToSpeechService(),new AlertController())),
                new SimpleExamResultsWriterService(new ExamResultsWriterDAO()),
                new LearnAlgorithmController(),
                new DirectoryChooserController(),
                new SimpleUserService()));

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
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



}
