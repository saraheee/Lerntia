package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;


public class QuestionnaireDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionnaireDAO questionnaireDAO;
    private ICourseDAO courseDAO;

    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager));
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

    private void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void IQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }

    @Test
    public void createNewQuestionnaire() throws PersistenceException {
        Long expected;

        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "2018");
        tgi.setMark("999.349");
        tgi.setName("TGI");
        courseDAO.create(tgi);

        LearningQuestionnaire chapter1 = new LearningQuestionnaire();
        chapter1.setName("Chapter 1");
        chapter1.setCourseID(tgi.getId());
        questionnaireDAO.create(chapter1);

        expected = chapter1.getId() + 1;

        LearningQuestionnaire chapter2 = new LearningQuestionnaire();
        chapter2.setName("Chapter 2");
        chapter2.setCourseID(tgi.getId());
        questionnaireDAO.create(chapter2);

        Assert.assertEquals(expected, chapter2.getId());
    }

    @Test(expected = Exception.class)
    public void createNewQuestionnaireError() throws PersistenceException {
        LearningQuestionnaire chapter1 = new LearningQuestionnaire();
        chapter1.setName("Chapter 1");
        questionnaireDAO.create(chapter1);
    }

}
