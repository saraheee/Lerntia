package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ImportQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireImportService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.*;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

public class QuestionnaireImportServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IQuestionnaireImportService importService;

    private Connection connection;

    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();
    private ILearningQuestionnaireService learningQuestionnaireService;
    private IQuestionService questionService;
    private IQuestionnaireQuestionService questionnaireQuestionService;
    private ILearnAlgorithmService learnAlgorithmService;
    private AlertController alertController;

    @Before
    public void setUp() {
        /*
        try {
            this.IQuestionnaireImportService(new SimpleQuestionnaireImportService(new QuestionnaireImportDAO(), new SimpleQuestionService(new QuestionDAO()), new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO()), new SimpleExamQuestionnaireService(), new SimpleQuestionnaireQuestionService()));

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
        */

        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();

            this.IQuestionnaireImportService( new SimpleQuestionnaireImportService(
                new QuestionnaireImportDAO()
                ,new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager, new LearnAlgorithmDAO(jdbcConnectionManager)))
                ,new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager))
                ,new SimpleExamQuestionnaireService(new ExamQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager))
                ,new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager))
            ) );

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }

    }

    private void IQuestionnaireImportService(IQuestionnaireImportService importService) {
        this.importService = importService;
    }

    //@Ignore
    @Test
    public void importLearningQuestionnaireWorks() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        ImportQuestionnaire importq = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "learningquestionnaire", false);
        importService.importQuestionnaire(importq);
    }

    @Ignore
    @Test
    public void importExamQuestionnaireWorks() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        ImportQuestionnaire importq = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "examquestionnaire", true);
        importService.importQuestionnaire(importq);
    }

    @Ignore
    @Test(expected = ServiceException.class)
    public void importLearningQuestionnaireWithSameName() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        ImportQuestionnaire importq = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "double", false);
        importService.importQuestionnaire(importq);
        ImportQuestionnaire importr = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "double", false);
        importService.importQuestionnaire(importr);
    }

    @Ignore
    @Test(expected = ServiceException.class)
    public void importExamQuestionnaireWithSameName() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        ImportQuestionnaire importq = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "double", true);
        importService.importQuestionnaire(importq);
        ImportQuestionnaire importr = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "double", true);
        importService.importQuestionnaire(importr);
    }

    @Ignore
    @Test
    public void importPicturesWorks() throws IOException, ServiceException {
        importService.importPictures(new File(System.getProperty("user.dir") + File.separator + "img_original" + File.separator + "test_image.png"), "test");
    }
}
