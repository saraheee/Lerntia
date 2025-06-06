package at.ac.tuwien.lerntia.lerntia;

import at.ac.tuwien.lerntia.lerntia.dao.ICourseDAO;
import at.ac.tuwien.lerntia.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.lerntia.lerntia.dao.impl.LearningQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dao.impl.QuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dto.Course;
import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.util.JDBCConnectionManager;
import at.ac.tuwien.lerntia.util.Semester;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;

public class LearningQuestionnaireDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionnaireDAO questionnaireDAO;
    private ILearningQuestionnaireDAO learningQuestionnaireDAO;
    private ICourseDAO courseDAO;

    private final JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager));
            this.ILearningQuestionnaireDAO(new LearningQuestionnaireDAO(questionnaireDAO, jdbcConnectionManager));
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

    private void ILearningQuestionnaireDAO(ILearningQuestionnaireDAO learningQuestionnaireDAO) {
        this.learningQuestionnaireDAO = learningQuestionnaireDAO;
    }

    private void ICourseDAO(ICourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void IQuestionnaireDAO(IQuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }

    @Test
    public void createNewLearningQuestionnaire() throws PersistenceException {
        Long expected;

        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "15");
        tgi.setMark("123.349");
        tgi.setName("TGI");
        courseDAO.create(tgi);

        LearningQuestionnaire chapter1 = new LearningQuestionnaire();
        chapter1.setName("Success chapter");
        chapter1.setCourseID(tgi.getId());
        learningQuestionnaireDAO.create(chapter1);

        expected = chapter1.getId() + 1;

        LearningQuestionnaire chapter2 = new LearningQuestionnaire();
        chapter2.setName("Success chapter2");
        chapter2.setCourseID(tgi.getId());
        learningQuestionnaireDAO.create(chapter2);

        Assert.assertEquals(expected, chapter2.getId());
    }

    @Test(expected = PersistenceException.class)
    public void createNewLearningQuestionnaireError() throws PersistenceException {
        LearningQuestionnaire chapter1 = new LearningQuestionnaire();
        chapter1.setName("Error chapter");
        learningQuestionnaireDAO.create(chapter1);

    }

    @Test
    public void readAllLearningQuestionnaire() throws PersistenceException {
        int before = learningQuestionnaireDAO.readAll().size();

        Course course = new Course();
        course.setSemester(Semester.WS + "2018");
        course.setMark("123.14232");
        course.setName("asdf");
        courseDAO.create(course);

        LearningQuestionnaire chapter1 = new LearningQuestionnaire();
        chapter1.setCourseID(course.getId());
        chapter1.setName("Chapter 1");
        learningQuestionnaireDAO.create(chapter1);

        int after = learningQuestionnaireDAO.readAll().size();

        assertTrue(before + 1 == after);
    }

    @Test
    public void checkSelectAndDeselect() throws PersistenceException {
        Course course = new Course();
        course.setSemester(Semester.WS + "2018");
        course.setMark("123.14232");
        course.setName("asdf");
        courseDAO.create(course);

        LearningQuestionnaire chapter1 = new LearningQuestionnaire();
        chapter1.setCourseID(course.getId());
        chapter1.setName("Chapter 1");
        learningQuestionnaireDAO.create(chapter1);

        learningQuestionnaireDAO.select(chapter1);
        LearningQuestionnaire q = learningQuestionnaireDAO.getSelected();
        assertTrue(q.getName().equals("Chapter 1"));

        learningQuestionnaireDAO.deselect(chapter1);
        q = learningQuestionnaireDAO.getSelected();
        assertTrue(q == null);
    }
}
