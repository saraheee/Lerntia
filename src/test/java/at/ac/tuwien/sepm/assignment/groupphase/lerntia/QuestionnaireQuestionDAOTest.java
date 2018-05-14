package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class QuestionnaireQuestionDAOTest {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private IQuestionnaireQuestionDAO questionnaireQuestionDAO;
    private IQuestionnaireDAO questionnaireDAO;
    private IExamQuestionnaireDAO examQuestionnaireDAO;
    private IQuestionDAO questionDAO;
    private ICourseDAO courseDAO;

    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionnaireQuestionDAO(new QuestionnaireQuestionDAO(jdbcConnectionManager));
            this.IQuestionnaireDAO(new QuestionnaireDAO(jdbcConnectionManager));
            this.ICourseDAO(new CourseDAO(jdbcConnectionManager));
            this.IExamQuestionnaireDAO(new ExamQuestionaireDAO((QuestionnaireDAO) questionnaireDAO,jdbcConnectionManager));
            this.IQuestionDAO(new QuestionDAO(jdbcConnectionManager));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database '{}'", e.getMessage(), e);
        }
    }

    private void IQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO=questionDAO;
    }

    private void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void IExamQuestionnaireDAO(ExamQuestionaireDAO examQuestionaireDAO) {
        this.examQuestionnaireDAO=examQuestionaireDAO;
    }

    private void IQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO=questionnaireDAO;
    }

    private void IQuestionnaireQuestionDAO(QuestionnaireQuestionDAO questionnaireQuestionDAO) {
        this.questionnaireQuestionDAO = questionnaireQuestionDAO;
    }

    @Test
    public void createNewQuestionnaireQuestion(){
        try {
            Course tgi = new Course();
            tgi.setSemester("2015S");
            tgi.setMark("123.349");
            courseDAO.create(tgi);

            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            //chapter1.setCmark("123.349");
            //chapter1.setSemester("2015S");
            chapter1.setCourseID(tgi.getId());
            examQuestionnaireDAO.create(chapter1);

            Question firstquestion = new Question();
            firstquestion.setQuestionText("How you doing");
            firstquestion.setAnswer1("No");
            firstquestion.setAnswer2("yes");
            firstquestion.setCorrectAnswers("1");
            questionDAO.create(firstquestion);
            Assert.assertEquals(Long.valueOf(3),firstquestion.getId());

            QuestionnaireQuestion firstquestionfirstchapter = new QuestionnaireQuestion();
            firstquestionfirstchapter.setQid(chapter1.getId());
            firstquestionfirstchapter.setQuestionid(firstquestion.getId());
            questionnaireQuestionDAO.create(firstquestionfirstchapter);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = PersistenceException.class)
    public void createNewQuestionnaireQuestionError() throws PersistenceException {
        try {
            QuestionnaireQuestion firstquestionfirstchapter = new QuestionnaireQuestion();
            firstquestionfirstchapter.setQid(Long.valueOf(1));
            firstquestionfirstchapter.setQuestionid(Long.valueOf(34));
            questionnaireQuestionDAO.create(firstquestionfirstchapter);
            Assert.assertEquals(Long.valueOf(1),firstquestionfirstchapter.getQid());
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Test
    public void checkpersistenceQuestionnaireQuestionDAO(){
        try {
            Course course = new Course();
            course.setSemester("2013S");
            course.setMark("123.555");
            courseDAO.create(course);

            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            //chapter1.setCmark("123.349");
            //chapter1.setSemester("2015S");
            chapter1.setCourseID(course.getId());
            examQuestionnaireDAO.create(chapter1);

            Question firstquestion = new Question();
            firstquestion.setQuestionText("How you doing");
            firstquestion.setAnswer1("Dont know");
            firstquestion.setAnswer2("yes");
            firstquestion.setCorrectAnswers("1");
            questionDAO.create(firstquestion);
            Assert.assertEquals(Long.valueOf(6),firstquestion.getId());

            QuestionnaireQuestion firstquestionfirstchapter = new QuestionnaireQuestion();
            firstquestionfirstchapter.setQid(chapter1.getId());
            firstquestionfirstchapter.setQuestionid(firstquestion.getId());
            questionnaireQuestionDAO.create(firstquestionfirstchapter);

            Question secondquestion = new Question();
            secondquestion.setQuestionText("What day is it?");
            secondquestion.setAnswer1("Saturday");
            secondquestion.setAnswer2("Monday");
            secondquestion.setCorrectAnswers("1");
            questionDAO.create(secondquestion);
            Assert.assertEquals(Long.valueOf(7),secondquestion.getId());

            QuestionnaireQuestion secondquestionfirstchapter = new QuestionnaireQuestion();
            secondquestionfirstchapter.setQid(chapter1.getId());
            secondquestionfirstchapter.setQuestionid(secondquestion.getId());
            questionnaireQuestionDAO.create(secondquestionfirstchapter);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchQuestionnaireQuestions(){
        try {
            Course tgi = new Course();
            tgi.setSemester("2010S");
            tgi.setMark("123.349");
            courseDAO.create(tgi);

            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            //chapter1.setCmark("123.349");
            //chapter1.setSemester("2015S");
            chapter1.setCourseID(tgi.getId());
            examQuestionnaireDAO.create(chapter1);

            Question firstquestion = new Question();
            firstquestion.setQuestionText("How you doing");
            firstquestion.setAnswer1("Dont know");
            firstquestion.setAnswer2("yes");
            firstquestion.setCorrectAnswers("1");
            questionDAO.create(firstquestion);
            Assert.assertEquals(Long.valueOf(4),firstquestion.getId());

            QuestionnaireQuestion firstquestionfirstchapter = new QuestionnaireQuestion();
            firstquestionfirstchapter.setQid(chapter1.getId());
            System.out.println(chapter1.getId());
            firstquestionfirstchapter.setQuestionid(firstquestion.getId());
            questionnaireQuestionDAO.create(firstquestionfirstchapter);

            Question secondquestion = new Question();
            secondquestion.setQuestionText("What day is it?");
            secondquestion.setAnswer1("Saturday");
            secondquestion.setAnswer2("Monday");
            secondquestion.setCorrectAnswers("12");
            questionDAO.create(secondquestion);
            Assert.assertEquals(Long.valueOf(5),secondquestion.getId());

            QuestionnaireQuestion secondquestionfirstchapter = new QuestionnaireQuestion();
            secondquestionfirstchapter.setQid(chapter1.getId());
            secondquestionfirstchapter.setQuestionid(secondquestion.getId());
            questionnaireQuestionDAO.create(secondquestionfirstchapter);

            QuestionnaireQuestion searchparameters = new QuestionnaireQuestion();
            Long id = Long.valueOf(5);
            searchparameters.setQid(id);
            List list = questionnaireQuestionDAO.search(searchparameters);
            System.out.println(list.size());
            Assert.assertEquals(2,list.size());
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = Exception.class)
    public void deleteQuestionnaireQuestionError() throws PersistenceException {

        try {
            Course tgi = new Course();
            tgi.setSemester("2015S");
            tgi.setMark("123.349");
            courseDAO.create(tgi);

            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            //chapter1.setCmark("123.349");
            //chapter1.setSemester("2015S");
            examQuestionnaireDAO.create(chapter1);

            Question firstquestion = new Question();
            firstquestion.setQuestionText("How you doing");
            firstquestion.setAnswer1("Dont know");
            firstquestion.setAnswer2("yes");
            firstquestion.setCorrectAnswers("1");
            questionDAO.create(firstquestion);
            Assert.assertEquals(Long.valueOf(1),firstquestion.getId());

            QuestionnaireQuestion firstquestionfirstchapter = new QuestionnaireQuestion();
            firstquestionfirstchapter.setQid(chapter1.getId());
            firstquestionfirstchapter.setQuestionid(firstquestion.getId());
            questionnaireQuestionDAO.create(firstquestionfirstchapter);

            Question secondquestion = new Question();
            secondquestion.setQuestionText("What day is it?");
            secondquestion.setAnswer1("Saturday");
            secondquestion.setAnswer2("Monday");
            secondquestion.setCorrectAnswers("12");
            questionDAO.create(secondquestion);
            Assert.assertEquals(Long.valueOf(2),secondquestion.getId());

            QuestionnaireQuestion secondquestionfirstchapter = new QuestionnaireQuestion();
            secondquestionfirstchapter.setQid(chapter1.getId());
            secondquestionfirstchapter.setQuestionid(secondquestion.getId());
            questionnaireQuestionDAO.create(secondquestionfirstchapter);

            firstquestionfirstchapter.setQid(null);
            questionnaireQuestionDAO.delete(firstquestionfirstchapter);
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }
}
