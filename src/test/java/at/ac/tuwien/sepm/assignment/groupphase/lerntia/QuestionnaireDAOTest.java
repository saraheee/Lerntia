package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.ExamQuestionaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.LearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IExamQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.util.List;


public class QuestionnaireDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionnaireDAO questionnaireDAO;
    private ICourseDAO courseDAO;

    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager));
            this.ICourseDAO(new CourseDAO(jdbcConnectionManager));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    private void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO=courseDAO;
    }

    private void IQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }

    @Test
    public void createNewQuestionnaire() throws PersistenceException {
        try {
            Long expected = Long.valueOf(0);

            Course tgi = new Course();
            tgi.setSemester("2018S");
            tgi.setMark("999.349");
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

            Assert.assertEquals(expected,chapter2.getId());

        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = Exception.class)
    public void createNewQuestionnaireError() throws PersistenceException {
        try {

            LearningQuestionnaire chapter1 = new LearningQuestionnaire();
            chapter1.setName("Chapter 1");
            //chapter1.setCmark("123.349");
            //chapter1.setSemester("2018S");
            questionnaireDAO.create(chapter1);


        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}
