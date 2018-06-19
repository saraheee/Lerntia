package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.ExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleExamQuestionnaireService;
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
import java.util.List;

public class ExamQuestionnaireServiceTest {

    private IExamQuestionnaireService examQuestionnaireService;
    private ICourseDAO courseDAO;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.ICourseDAO(new CourseDAO(jdbcConnectionManager));
            this.IExamQuestionnaireService(new SimpleExamQuestionnaireService(new ExamQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager),jdbcConnectionManager)));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
    }

    private void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void IExamQuestionnaireService(SimpleExamQuestionnaireService simpleExamQuestionnaireService) {
        this.examQuestionnaireService = simpleExamQuestionnaireService;
    }


    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

    @Test
    public void createExamQuestionnaireTest(){
        try {
            Course samplecourse = new Course();
            samplecourse.setName("SEPM");
            samplecourse.setSemester("2014W");
            samplecourse.setMark("123.123");
            courseDAO.create(samplecourse);
            ExamQuestionnaire sample = new ExamQuestionnaire();
            sample.setName("Name");
            sample.setCourseID(samplecourse.getId());
            LocalDate ld = LocalDate.now();
            sample.setDate(ld);
            examQuestionnaireService.create(sample);
            Long test = Long.valueOf(1);
            Assert.assertEquals(test,sample.getId());
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readAllExamTest(){
        try {
            Course samplecourse = new Course();
            samplecourse.setName("TIL");
            samplecourse.setSemester("2019W");
            samplecourse.setMark("123.555");
            courseDAO.create(samplecourse);
            ExamQuestionnaire sample = new ExamQuestionnaire();
            sample.setName("Chapter 1");
            sample.setCourseID(samplecourse.getId());
            LocalDate ld = LocalDate.now();
            sample.setDate(ld);
            examQuestionnaireService.create(sample);
            ExamQuestionnaire anotherSample = new ExamQuestionnaire();
            anotherSample.setDate(ld);
            anotherSample.setCourseID(samplecourse.getId());
            anotherSample.setName("Chapter 2");
            List<ExamQuestionnaire> list = examQuestionnaireService.readAll();
            Assert.assertEquals(2,list.size());
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
