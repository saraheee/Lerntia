package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ImportQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ICourseService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireImportService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

public class QuestionnaireImportServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IQuestionnaireImportService importService;

    private Connection connection;

    private final JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    private ICourseService courseService;

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();

            this.IQuestionnaireImportService(new SimpleQuestionnaireImportService(
                new QuestionnaireImportDAO()
                , new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager, new LearnAlgorithmDAO(jdbcConnectionManager)))
                , new SimpleLearningQuestionnaireService(new LearningQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager), jdbcConnectionManager))
                , new SimpleExamQuestionnaireService(new ExamQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager), jdbcConnectionManager))
                , new SimpleQuestionnaireQuestionService(new QuestionnaireQuestionDAO(jdbcConnectionManager))
            ));

            this.ICourseService(new SimpleCourseService(new CourseDAO(jdbcConnectionManager)));

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }

    }

    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    private void IQuestionnaireImportService(IQuestionnaireImportService importService) {
        this.importService = importService;
    }

    private void ICourseService(ICourseService simpleCourseService) {
        this.courseService = simpleCourseService;
    }

    @Test
    public void importLearningQuestionnaireWorks() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.create(course);
        File file = new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile.csv");
        ImportQuestionnaire importq = new ImportQuestionnaire(file, course, "learning", false);
        importService.importQuestionnaire(importq);
    }

    @Test
    public void importExamQuestionnaireWorks() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.create(course);
        File file = new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile.csv");
        ImportQuestionnaire importq = new ImportQuestionnaire(file, course, "exam", true);
        importService.importQuestionnaire(importq);
    }

    @Test(expected = ServiceException.class)
    public void importLearningQuestionnaireWithSameName() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        ImportQuestionnaire importq = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "double", false);
        importService.importQuestionnaire(importq);
        ImportQuestionnaire importr = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "double", false);
        importService.importQuestionnaire(importr);
    }

    @Test(expected = ServiceException.class)
    public void importExamQuestionnaireWithSameName() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        ImportQuestionnaire importq = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "double", true);
        importService.importQuestionnaire(importq);
        ImportQuestionnaire importr = new ImportQuestionnaire(new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_correctfile"), course, "double", true);
        importService.importQuestionnaire(importr);
    }

    @Test
    public void importPicturesWorks() throws IOException, ServiceException {
        importService.importPictures(new File(System.getProperty("user.dir") + File.separator + "img_original" + File.separator + "test_image.png"), "test");
    }

    @Test(expected = ServiceException.class)
    public void tooManyColumnsForImport() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.create(course);
        File file = new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_toomanycolumns.csv");
        ImportQuestionnaire importq = new ImportQuestionnaire(file, course, "toomany", false);
        importService.importQuestionnaire(importq);
    }

    @Test(expected = ServiceException.class)
    public void notANumberForImport() throws ServiceException {
        Course course = new Course("asdf", Semester.SS + "2018", "asdf", false);
        courseService.create(course);
        File file = new File(System.getProperty("user.dir") + File.separator + "csv" + File.separator + "test_wrongpicture.csv");
        ImportQuestionnaire importq = new ImportQuestionnaire(file, course, "wrongpicture", false);
        importService.importQuestionnaire(importq);
    }
}
