package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.LearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class QuestionServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IQuestionService questionService;

    private ConfigReader configReaderQuestions;

    private JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        configReaderQuestions = new ConfigReader("questions");
        try {
            this.IQuestionService(new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager,new LearnAlgorithmDAO(jdbcConnectionManager))));
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    private void IQuestionService(SimpleQuestionService simpleQuestionService) {
        this.questionService = simpleQuestionService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // creation
    // -----------------------------------------------------------------------------------------------------------------

    @Ignore
    @Test
    public void createCorrectQuestion() throws PersistenceException, ServiceException {
        Question q1 = new Question((long) 0, "asdf", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);
        questionService.create(q1);
        long id1 = q1.getId();
        Question q2 = new Question((long) 0, "asdf", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);
        questionService.create(q2);
        long id2 = q2.getId();
        assertTrue(id1 < id2);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // update
    // -----------------------------------------------------------------------------------------------------------------

    @Ignore
    @Test
    public void updateCorrectCourse() throws ServiceException, PersistenceException {
        Question q = new Question((long) 0, "asdf", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);
        questionService.create(q);
        String old = q.getQuestionText();
        String after = "qwert";
        q.setQuestionText(after);
        questionService.update(q);
        assertTrue(!old.equals(after));
        assertTrue(q.getQuestionText().equals(after));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // search
    // -----------------------------------------------------------------------------------------------------------------

    @Ignore
    @Test
    public void searchQuestions() throws PersistenceException, ServiceException {
        List<Question> questionlist = new ArrayList<>();
        Question q1 = new Question();
        q1.setPicture("");
        q1.setQuestionText("asdf");
        q1.setAnswer1("a1");
        q1.setAnswer2("a2");
        q1.setAnswer3("a3");
        q1.setAnswer4("a4");
        q1.setAnswer5("a5");
        q1.setCorrectAnswers("23");
        q1.setOptionalFeedback("feedback");
        questionlist.add(q1);
        questionService.create(q1);
        Question q2 = new Question();
        q2.setPicture("");
        q2.setQuestionText("qwert");
        q2.setAnswer1("a1");
        q2.setAnswer2("a2");
        q2.setAnswer3("a3");
        q2.setAnswer4("a4");
        q2.setAnswer5("a5");
        q2.setCorrectAnswers("23");
        q2.setOptionalFeedback("feedback");
        questionlist.add(q2);
        questionService.create(q2);
        List<Question> questions = questionService.search(questionlist);
        assertTrue(questionlist.get(0).getQuestionText().equals(questions.get(0).getQuestionText()));
        assertTrue(questionlist.get(1).getQuestionText().equals(questions.get(1).getQuestionText()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // validation
    // -----------------------------------------------------------------------------------------------------------------

    // correct question

    @Test
    public void validateCorrectQuestion() throws ServiceException {
        Question q = new Question((long) 0, "asdf", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);
        questionService.validate(q);
    }

    // no question text

    @Test(expected = ServiceException.class)
    public void validateNoQuestionText() throws ServiceException {
        Question q = new Question((long) 0, "", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);
        questionService.validate(q);
    }

    // question text too long

    @Test(expected = ServiceException.class)
    public void validateTooLongQuestionText() throws ServiceException {
        Question q = new Question((long) 0, "", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);

        var maxLength = this.configReaderQuestions.getValueInt("maxLengthQuestion");

        String tooLongQuestionText = "";
        for (var i = 0; i < maxLength + 1; i++) {
            tooLongQuestionText += "a";
        }
        q.setQuestionText(tooLongQuestionText);

        questionService.validate(q);
    }

    // fewer than 2 answers present

    @Test(expected = ServiceException.class)
    public void validateFewerThanTwoAnswersPresent() throws ServiceException {
        Question q = new Question((long) 0, "asdf", "", "a1", "", "", "", "", "1", "feedback", false);
        questionService.validate(q);
    }

    // answer is too long

    @Test(expected = ServiceException.class)
    public void validateTooLongAnswer() throws ServiceException {
        Question q = new Question((long) 0, "asdf", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);

        var maxLength = this.configReaderQuestions.getValueInt("maxLengthAnswer");

        String tooLongAnswer = "";
        for (var i = 0; i < maxLength + 1; i++) {
            tooLongAnswer += "a";
        }
        q.setAnswer1(tooLongAnswer);

        questionService.validate(q);
    }

    // answer is marked as correct but not present (has no text)

    @Test(expected = ServiceException.class)
    public void validateCorrectAnswerNotPresent() throws ServiceException {
        Question q = new Question();
        q.setQuestionText("asdf");
        q.setAnswer1("a1");
        q.setAnswer2("a2");
        q.setAnswer3("a3");
        q.setAnswer4("a4");
        q.setAnswer5("5");
        q.setOptionalFeedback("feedback");
        questionService.validate(q);
    }

    // answer is marked as correct with index 0

    @Test(expected = ServiceException.class)
    public void validateCorrectAnswerWithIndexZero() throws ServiceException {
        Question q = new Question();
        q.setQuestionText("asdf");
        q.setAnswer1("a1");
        q.setAnswer2("a2");
        q.setAnswer3("a3");
        q.setAnswer4("a4");
        q.setAnswer5("");
        q.setCorrectAnswers("120");
        q.setOptionalFeedback("feedback");
        questionService.validate(q);
    }

    // answer is marked as correct with index higher than 5

    @Test(expected = ServiceException.class)
    public void validateCorrectAnswerWithIndexHigherThanFive() throws ServiceException {
        Question q = new Question();
        q.setQuestionText("asdf");
        q.setAnswer1("a1");
        q.setAnswer2("a2");
        q.setAnswer3("a3");
        q.setAnswer4("a4");
        q.setAnswer5("");
        q.setCorrectAnswers("126");
        q.setOptionalFeedback("feedback");
        questionService.validate(q);
    }

    @After
    public void wrapUp() {
        this.configReaderQuestions.close();
    }
}
