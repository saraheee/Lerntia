package at.ac.tuwien.lerntia.lerntia;

import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.dao.ICourseDAO;
import at.ac.tuwien.lerntia.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.lerntia.lerntia.dao.impl.ExamQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dao.impl.QuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dto.Course;
import at.ac.tuwien.lerntia.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.lerntia.util.JDBCConnectionManager;
import at.ac.tuwien.lerntia.util.Semester;
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

import static junit.framework.TestCase.assertTrue;

public class ExamQuestionnaireDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionnaireDAO questionnaireDAO;
    private IExamQuestionnaireDAO examQuestionnaireDAO;
    private ICourseDAO courseDAO;

    private final JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager));
            this.IExamQuestionnaireDAO(new ExamQuestionnaireDAO(questionnaireDAO, jdbcConnectionManager));
            this.ICourseDAO(new CourseDAO(jdbcConnectionManager));

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

    private void IExamQuestionnaireDAO(IExamQuestionnaireDAO examQuestionnaireDAO) {
        this.examQuestionnaireDAO = examQuestionnaireDAO;
    }

    private void ICourseDAO(ICourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void IQuestionnaireDAO(IQuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }

    @Test
    public void createNewExamQuestionnaire() throws PersistenceException {
        Long expected;

        Course course = new Course();
        course.setSemester(Semester.WS + "2018");
        course.setMark("123.14232");
        course.setName("asdf");
        courseDAO.create(course);

        ExamQuestionnaire chapter1 = new ExamQuestionnaire();
        chapter1.setDate(LocalDate.now());
        chapter1.setCourseID(course.getId());
        chapter1.setName("TILExam");
        examQuestionnaireDAO.create(chapter1);

        expected = chapter1.getId() + 1;

        ExamQuestionnaire chapter2 = new ExamQuestionnaire();
        chapter2.setDate(LocalDate.now());
        chapter2.setCourseID(course.getId());
        chapter2.setName("TILExam2");
        examQuestionnaireDAO.create(chapter2);

        Assert.assertEquals(expected, chapter2.getId());

    }

    @Test(expected = PersistenceException.class)
    public void createNewExamQuestionnaireError() throws PersistenceException {
        ExamQuestionnaire chapter1 = new ExamQuestionnaire();
        chapter1.setDate(LocalDate.now());
        examQuestionnaireDAO.create(chapter1);

    }

    @Test
    public void readAllExamQuestionnaire() throws PersistenceException {
        int before = examQuestionnaireDAO.readAll().size();

        Course course = new Course();
        course.setSemester(Semester.WS + "2018");
        course.setMark("123.14232");
        course.setName("asdf");
        courseDAO.create(course);

        ExamQuestionnaire chapter1 = new ExamQuestionnaire();
        chapter1.setDate(LocalDate.now());
        chapter1.setCourseID(course.getId());
        chapter1.setName("TILExam");
        examQuestionnaireDAO.create(chapter1);

        int after = examQuestionnaireDAO.readAll().size();

        assertTrue(before + 1 == after);
    }

}
