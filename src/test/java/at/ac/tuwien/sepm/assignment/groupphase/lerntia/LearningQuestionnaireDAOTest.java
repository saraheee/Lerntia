package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.CourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.LearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ICourseDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;

public class LearningQuestionnaireDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionnaireDAO questionnaireDAO;
    private ILearningQuestionnaireDAO learningQuestionnaireDAO;
    private ICourseDAO courseDAO;

    @Before
    public void setUp() {
        try {
            connection = JDBCConnectionManager.getTestConnection();
            this.IQuestionnaireDAO(new QuestionnaireDAO());
            this.ILearningQuestionnaireDAO(new LearningQuestionnaireDAO((QuestionnaireDAO) questionnaireDAO));
            this.ICourseDAO(new CourseDAO());

        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    private void ILearningQuestionnaireDAO(LearningQuestionnaireDAO learningQuestionnaireDAO) {
        this.learningQuestionnaireDAO = learningQuestionnaireDAO;
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
            Course tgi = new Course();
            tgi.setSemester("2015S");
            tgi.setMark("123.349");
            courseDAO.create(tgi);
            LearningQuestionnaire chapter1 = new LearningQuestionnaire();
            chapter1.setName("Success chapter");
            //chapter1.setCmark("123.349");
            //chapter1.setSemester("2015S");
            chapter1.setCourseID(tgi.getId());
            learningQuestionnaireDAO.create(chapter1);
            Long expected = Long.valueOf(2);
            Assert.assertEquals(expected, chapter1.getId());
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test(expected = Exception.class)
    public void createNewExamQuestionnaireError() throws PersistenceException{
        try {
            LearningQuestionnaire chapter1 = new LearningQuestionnaire();
            chapter1.setName("Error chapter");
            //chapter1.setCmark("123.349");
            //chapter1.setSemester("2015S");
            learningQuestionnaireDAO.create(chapter1);
        }catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }
}
