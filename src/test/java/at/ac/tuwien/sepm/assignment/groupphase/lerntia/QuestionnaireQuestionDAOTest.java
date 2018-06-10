package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
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
            this.IExamQuestionnaireDAO(new ExamQuestionnaireDAO((QuestionnaireDAO) questionnaireDAO, jdbcConnectionManager));
            this.IQuestionDAO(new QuestionDAO(jdbcConnectionManager, new LearnAlgorithmDAO(jdbcConnectionManager)));
        } catch (PersistenceException e) {
            LOG.error("Failed to get connection to test-database");
        }
    }

    private void IQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    private void ICourseDAO(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    private void IExamQuestionnaireDAO(ExamQuestionnaireDAO examQuestionnaireDAO) {
        this.examQuestionnaireDAO = examQuestionnaireDAO;
    }

    private void IQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }

    private void IQuestionnaireQuestionDAO(QuestionnaireQuestionDAO questionnaireQuestionDAO) {
        this.questionnaireQuestionDAO = questionnaireQuestionDAO;
    }

    @Test
    public void createNewQuestionnaireQuestion() {
        try {
            Course tgi = new Course();
            tgi.setSemester(Semester.SS + "15");
            tgi.setMark("123.349");
            tgi.setName("TGI");
            courseDAO.create(tgi);

            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            chapter1.setName("asdf");
            chapter1.setCourseID(tgi.getId());
            examQuestionnaireDAO.create(chapter1);

            Question firstQuestion = new Question();
            firstQuestion.setQuestionText("How you doing");
            firstQuestion.setAnswer1("No");
            firstQuestion.setAnswer2("yes");
            firstQuestion.setCorrectAnswers("1");
            questionDAO.create(firstQuestion);
            Assert.assertEquals(Long.valueOf(3), firstQuestion.getId());

            QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
            firstQuestionFirstChapter.setQid(chapter1.getId());
            firstQuestionFirstChapter.setQuestionid(firstQuestion.getId());
            questionnaireQuestionDAO.create(firstQuestionFirstChapter);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = PersistenceException.class)
    public void createNewQuestionnaireQuestionError() throws PersistenceException {
        try {
            QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
            firstQuestionFirstChapter.setQid(Long.valueOf(1));
            firstQuestionFirstChapter.setQuestionid(Long.valueOf(34));
            questionnaireQuestionDAO.create(firstQuestionFirstChapter);
            Assert.assertEquals(Long.valueOf(1), firstQuestionFirstChapter.getQid());
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getCustommessage());
        }
    }

    @Test
    public void checkPersistenceQuestionnaireQuestionDAO() {
        try {
            Course course = new Course();
            course.setSemester(Semester.SS + "13");
            course.setMark("123.555");
            course.setName("name");
            courseDAO.create(course);

            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            chapter1.setName("asdf3");
            chapter1.setCourseID(course.getId());
            examQuestionnaireDAO.create(chapter1);

            Question firstQuestion = new Question();
            firstQuestion.setQuestionText("How you doing");
            firstQuestion.setAnswer1("Dont know");
            firstQuestion.setAnswer2("yes");
            firstQuestion.setCorrectAnswers("1");
            questionDAO.create(firstQuestion);
            Assert.assertEquals(Long.valueOf(6), firstQuestion.getId());

            QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
            firstQuestionFirstChapter.setQid(chapter1.getId());
            firstQuestionFirstChapter.setQuestionid(firstQuestion.getId());
            questionnaireQuestionDAO.create(firstQuestionFirstChapter);

            Question secondQuestion = new Question();
            secondQuestion.setQuestionText("What day is it?");
            secondQuestion.setAnswer1("Saturday");
            secondQuestion.setAnswer2("Monday");
            secondQuestion.setCorrectAnswers("1");
            questionDAO.create(secondQuestion);
            Assert.assertEquals(Long.valueOf(7), secondQuestion.getId());

            QuestionnaireQuestion secondQuestionFirstChapter = new QuestionnaireQuestion();
            secondQuestionFirstChapter.setQid(chapter1.getId());
            secondQuestionFirstChapter.setQuestionid(secondQuestion.getId());
            questionnaireQuestionDAO.create(secondQuestionFirstChapter);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    // search for the questions associated with an exam questionnaire
    // we expect that 2 questions will be found

    @Test
    public void searchQuestionnaireQuestions() {
        try {
            Long examQuestionnaireID = Long.valueOf(0);
            Long firstQuestionID = Long.valueOf(0);
            Long secondQuestionID = Long.valueOf(0);

            Course tgi = new Course();
            tgi.setSemester(Semester.SS + "10");
            tgi.setMark("123.349");
            tgi.setName("TGI");
            courseDAO.create(tgi);

            ExamQuestionnaire chapter1 = new ExamQuestionnaire();
            chapter1.setDate(LocalDate.now());
            chapter1.setName("asdf2");
            chapter1.setCourseID(tgi.getId());
            examQuestionnaireDAO.create(chapter1);

            examQuestionnaireID = chapter1.getId();

            Question firstQuestion = new Question();
            firstQuestion.setQuestionText("How you doing");
            firstQuestion.setAnswer1("Dont know");
            firstQuestion.setAnswer2("yes");
            firstQuestion.setCorrectAnswers("1");
            questionDAO.create(firstQuestion);

            firstQuestionID = firstQuestion.getId();

            QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
            firstQuestionFirstChapter.setQid(chapter1.getId());
            firstQuestionFirstChapter.setQuestionid(firstQuestion.getId());
            questionnaireQuestionDAO.create(firstQuestionFirstChapter);

            Question secondQuestion = new Question();
            secondQuestion.setQuestionText("What day is it?");
            secondQuestion.setAnswer1("Saturday");
            secondQuestion.setAnswer2("Monday");
            secondQuestion.setCorrectAnswers("12");
            questionDAO.create(secondQuestion);
            Assert.assertEquals(Long.valueOf(firstQuestionID + 1), secondQuestion.getId());

            secondQuestionID = secondQuestion.getId();

            QuestionnaireQuestion secondQuestionFirstChapter = new QuestionnaireQuestion();
            secondQuestionFirstChapter.setQid(chapter1.getId());
            secondQuestionFirstChapter.setQuestionid(secondQuestion.getId());
            questionnaireQuestionDAO.create(secondQuestionFirstChapter);
            QuestionnaireQuestion searchParameters = new QuestionnaireQuestion();
            searchParameters.setQid(examQuestionnaireID);

            List list = questionnaireQuestionDAO.search(searchParameters);
            Assert.assertEquals(2, list.size());
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = Exception.class)
    public void deleteQuestionnaireQuestionError() throws PersistenceException {

        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "15");
        tgi.setMark("123.349");
        tgi.setName("TGI");
        courseDAO.create(tgi);

        ExamQuestionnaire chapter1 = new ExamQuestionnaire();
        chapter1.setDate(LocalDate.now());
        //chapter1.setCmark("123.349");
        //chapter1.setSemester("2015S");
        examQuestionnaireDAO.create(chapter1);

        Question firstQuestion = new Question();
        firstQuestion.setQuestionText("How you doing");
        firstQuestion.setAnswer1("Dont know");
        firstQuestion.setAnswer2("yes");
        firstQuestion.setCorrectAnswers("1");
        questionDAO.create(firstQuestion);
        Assert.assertEquals(Long.valueOf(1), firstQuestion.getId());

        QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
        firstQuestionFirstChapter.setQid(chapter1.getId());
        firstQuestionFirstChapter.setQuestionid(firstQuestion.getId());
        questionnaireQuestionDAO.create(firstQuestionFirstChapter);

        Question secondQuestion = new Question();
        secondQuestion.setQuestionText("What day is it?");
        secondQuestion.setAnswer1("Saturday");
        secondQuestion.setAnswer2("Monday");
        secondQuestion.setCorrectAnswers("12");
        questionDAO.create(secondQuestion);
        Assert.assertEquals(Long.valueOf(2), secondQuestion.getId());

        QuestionnaireQuestion secondQuestionFirstChapter = new QuestionnaireQuestion();
        secondQuestionFirstChapter.setQid(chapter1.getId());
        secondQuestionFirstChapter.setQuestionid(secondQuestion.getId());
        questionnaireQuestionDAO.create(secondQuestionFirstChapter);

        firstQuestionFirstChapter.setQid(null);
        questionnaireQuestionDAO.delete(firstQuestionFirstChapter);

    }
}
