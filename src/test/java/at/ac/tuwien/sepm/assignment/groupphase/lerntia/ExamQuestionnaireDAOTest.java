package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.ExamQuestionaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
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
import java.time.LocalDate;

public class ExamQuestionnaireDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionnaireDAO questionnaireDAO;
    private IExamQuestionnaireDAO examQuestionnaireDAO;
    private ICourseDAO courseDAO;

    @Before
    public void setUp() {
        try {
            connection = JDBCConnectionManager.getTestConnection();
            this.IQuestionnaireDAO(new QuestionnaireDAO());
            this.IExamQuestionaireDAO(new ExamQuestionaireDAO((QuestionnaireDAO) questionnaireDAO));
            this.ICourseDAO(new CourseDAO());

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    @After
    public void rollback() throws SQLException {
        connection.rollback();
    }

    private void IExamQuestionaireDAO(ExamQuestionaireDAO examQuestionaireDAO) {
        this.examQuestionnaireDAO = examQuestionaireDAO;
    }

    private void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO=courseDAO;
    }

    private void IQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }

    @Test
    public void createNewExamQuestionnaire() throws PersistenceException {
        try {
            Course course = new Course();
            course.setSemester("2018W");
            course.setMark("123.14232");
            courseDAO.create(course);
            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            chapter1.setCourseID(course.getId());
            examQuestionnaireDAO.create(chapter1);
            Long expected = Long.valueOf(1);
            Assert.assertEquals(expected, chapter1.getId());
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = Exception.class)
    public void createNewExamQuestionnaireError() throws PersistenceException{
        try {
            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            //chapter1.setCmark("123.349");
            //chapter1.setSemester("2015S");
            examQuestionnaireDAO.create(chapter1);
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

}
