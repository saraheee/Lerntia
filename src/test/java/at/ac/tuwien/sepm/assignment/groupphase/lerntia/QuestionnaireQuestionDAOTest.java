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
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

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
            JDBCConnectionManager.setIsTestConnection(true);
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

    @After
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
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
    public void createNewQuestionnaireQuestion() throws PersistenceException {
        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "2015");
        tgi.setMark("123.349");
        tgi.setName("TGI");
        courseDAO.create(tgi);

        ExamQuestionnaire chapter1 = new ExamQuestionnaire();
        chapter1.setDate(LocalDate.now());
        chapter1.setName("asdf");
        chapter1.setCourseID(tgi.getId());
        examQuestionnaireDAO.create(chapter1);

        Question refQuestion = new Question();
        refQuestion.setQuestionText("How you doing");
        refQuestion.setAnswer1("No");
        refQuestion.setAnswer2("yes");
        refQuestion.setCorrectAnswers("1");
        questionDAO.create(refQuestion);
        long refId = refQuestion.getId();

        Question firstQuestion = new Question();
        firstQuestion.setQuestionText("How you doing");
        firstQuestion.setAnswer1("No");
        firstQuestion.setAnswer2("yes");
        firstQuestion.setCorrectAnswers("1");
        questionDAO.create(firstQuestion);
        Assert.assertEquals(Long.valueOf(refId + 1), firstQuestion.getId());

        QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
        firstQuestionFirstChapter.setQid(chapter1.getId());
        firstQuestionFirstChapter.setQuestionid(firstQuestion.getId());
        questionnaireQuestionDAO.create(firstQuestionFirstChapter);
    }

    @Test(expected = PersistenceException.class)
    public void createNewQuestionnaireQuestionError() throws PersistenceException {
        QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
        firstQuestionFirstChapter.setQid(212L);
        firstQuestionFirstChapter.setQuestionid(34L);
        questionnaireQuestionDAO.create(firstQuestionFirstChapter);
        Assert.assertEquals(Long.valueOf(212), firstQuestionFirstChapter.getQid());
    }

    @Test
    public void checkPersistenceQuestionnaireQuestionDAO() throws PersistenceException {
        Course course = new Course();
        course.setSemester(Semester.SS + "2013");
        course.setMark("123.555");
        course.setName("name");
        courseDAO.create(course);

        ExamQuestionnaire chapter1 = new ExamQuestionnaire();
        chapter1.setDate(LocalDate.now());
        chapter1.setName("asdf3");
        chapter1.setCourseID(course.getId());
        examQuestionnaireDAO.create(chapter1);

        Question refQuestion = new Question();
        refQuestion.setQuestionText("How you doing");
        refQuestion.setAnswer1("Dont know");
        refQuestion.setAnswer2("yes");
        refQuestion.setCorrectAnswers("1");
        questionDAO.create(refQuestion);
        long refId = refQuestion.getId();

        Question firstQuestion = new Question();
        firstQuestion.setQuestionText("How you doing");
        firstQuestion.setAnswer1("Dont know");
        firstQuestion.setAnswer2("yes");
        firstQuestion.setCorrectAnswers("1");
        questionDAO.create(firstQuestion);
        Assert.assertEquals(Long.valueOf(refId + 1), firstQuestion.getId());

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
        Assert.assertEquals(Long.valueOf(refId + 2), secondQuestion.getId());

        QuestionnaireQuestion secondQuestionFirstChapter = new QuestionnaireQuestion();
        secondQuestionFirstChapter.setQid(chapter1.getId());
        secondQuestionFirstChapter.setQuestionid(secondQuestion.getId());
        questionnaireQuestionDAO.create(secondQuestionFirstChapter);
    }

    // search for the questions associated with an exam questionnaire
    // we expect that 2 questions will be found

    @Test
    public void searchQuestionnaireQuestions() throws PersistenceException {
        Long examQuestionnaireID;
        Long firstQuestionID;
        Long secondQuestionID;

        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "2010");
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
        secondQuestionFirstChapter.setQuestionid(secondQuestionID);
        questionnaireQuestionDAO.create(secondQuestionFirstChapter);
        QuestionnaireQuestion searchParameters = new QuestionnaireQuestion();
        searchParameters.setQid(examQuestionnaireID);

        List list = questionnaireQuestionDAO.search(searchParameters);
        Assert.assertEquals(2, list.size());
    }

    @Test(expected = PersistenceException.class)
    public void deleteQuestionnaireQuestionError() throws PersistenceException {

        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "2015");
        tgi.setMark("123.349");
        tgi.setName("TGI");
        courseDAO.create(tgi);

        ExamQuestionnaire chapter1 = new ExamQuestionnaire();
        chapter1.setDate(LocalDate.now());
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

    @Test
    public void deleteQuestionnaireQuestion() throws PersistenceException {
        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "2015");
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

        QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
        firstQuestionFirstChapter.setQid(chapter1.getId());
        firstQuestionFirstChapter.setQuestionid(firstQuestion.getId());
        questionnaireQuestionDAO.create(firstQuestionFirstChapter);

        int before = questionnaireQuestionDAO.readAll().size();
        questionnaireQuestionDAO.delete(firstQuestionFirstChapter);
        int after = questionnaireQuestionDAO.readAll().size();

        assertTrue(before > after);
    }

    @Test
    public void updateQuestionnaireQuestion() throws PersistenceException {
        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "2015");
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

        Question secondQuestion = new Question();
        secondQuestion.setQuestionText("How was your day");
        secondQuestion.setAnswer1("Good");
        secondQuestion.setAnswer2("Bad");
        secondQuestion.setCorrectAnswers("1");
        questionDAO.create(secondQuestion);

        QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
        firstQuestionFirstChapter.setQid(chapter1.getId());
        firstQuestionFirstChapter.setQuestionid(firstQuestion.getId());
        questionnaireQuestionDAO.create(firstQuestionFirstChapter);

        firstQuestionFirstChapter.setQuestionid(secondQuestion.getId());
        questionnaireQuestionDAO.update(firstQuestionFirstChapter, chapter1.getId(), secondQuestion.getId());

        Long id = firstQuestionFirstChapter.getQuestionid();
        Question q = questionDAO.get(id);

        assertTrue(q.getQuestionText().equals("How was your day"));
    }

    @Test
    public void readAllQuestionnaireQuestion() throws PersistenceException {
        int before = questionnaireQuestionDAO.readAll().size();
        Course tgi = new Course();
        tgi.setSemester(Semester.SS + "2015");
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

        QuestionnaireQuestion firstQuestionFirstChapter = new QuestionnaireQuestion();
        firstQuestionFirstChapter.setQid(chapter1.getId());
        firstQuestionFirstChapter.setQuestionid(firstQuestion.getId());
        questionnaireQuestionDAO.create(firstQuestionFirstChapter);

        int after = questionnaireQuestionDAO.readAll().size();
        assertTrue(before < after);
    }
}
