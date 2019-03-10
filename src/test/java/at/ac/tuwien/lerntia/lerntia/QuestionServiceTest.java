package at.ac.tuwien.lerntia.lerntia;

import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dao.impl.LearnAlgorithmDAO;
import at.ac.tuwien.lerntia.lerntia.dao.impl.QuestionDAO;
import at.ac.tuwien.lerntia.lerntia.dto.Question;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionService;
import at.ac.tuwien.lerntia.util.ConfigReader;
import at.ac.tuwien.lerntia.util.JDBCConnectionManager;
import at.ac.tuwien.lerntia.exception.ConfigReaderException;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.service.impl.SimpleQuestionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class QuestionServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IQuestionService questionService;
    private Connection connection;
    private ConfigReader configReaderQuestions;

    private final JDBCConnectionManager jdbcConnectionManager = new JDBCConnectionManager();

    @Before
    public void setUp() {
        try {
            configReaderQuestions = new ConfigReader("questions");
        } catch (ConfigReaderException e) {
            LOG.error("Failed to get config reader for questions");
        }
        try {
            JDBCConnectionManager.setIsTestConnection(true);
            connection = jdbcConnectionManager.getTestConnection();
            this.IQuestionService(new SimpleQuestionService(new QuestionDAO(jdbcConnectionManager, new LearnAlgorithmDAO(jdbcConnectionManager))));
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

    private void IQuestionService(IQuestionService simpleQuestionService) {
        this.questionService = simpleQuestionService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // creation
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void createCorrectQuestion() throws ServiceException {
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

    @Test
    public void updateCorrectCourse() throws ServiceException {
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

    @Test
    public void searchQuestions() throws ServiceException {
        List<Question> questionlist = new ArrayList<>();
        Question q1 = new Question();
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
        List<Question> questions = questionService.search(new ArrayList<>(questionlist));
        assertEquals(questionlist.size(), questions.size());
        assertTrue(questionlist.get(0).getQuestionText().equals(questions.get(0).getQuestionText()));
        assertTrue(questionlist.get(1).getQuestionText().equals(questions.get(1).getQuestionText()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // delete
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void deleteQuestion() throws ServiceException {
        Question q = new Question((long) 0, "asdf", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);
        questionService.create(q);
        boolean before = q.getDeleted();
        questionService.delete(q);
        boolean after = q.getDeleted();
        assertTrue(!before);
        assertTrue(after);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // get
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void getQuestionwithId() throws ServiceException {
        Question q1 = new Question();
        q1.setQuestionText("asdf");
        q1.setAnswer1("a1");
        q1.setAnswer2("a2");
        q1.setAnswer3("a3");
        q1.setAnswer4("a4");
        q1.setAnswer5("a5");
        q1.setCorrectAnswers("23");
        q1.setOptionalFeedback("feedback");
        questionService.create(q1);
        long id = q1.getId();
        Question q2 = questionService.get(id);
        assertTrue(q1.getQuestionText().equals(q2.getQuestionText()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // getAllAnswers
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllAnswersOfQuestion() throws ServiceException {
        Question q1 = new Question();
        q1.setQuestionText("asdf");
        q1.setAnswer1("a1");
        q1.setAnswer2("a2");
        q1.setAnswer3("a3");
        q1.setAnswer4("a4");
        q1.setAnswer5("a5");
        q1.setCorrectAnswers("23");
        q1.setOptionalFeedback("feedback");
        questionService.create(q1);
        ArrayList<String> answers = questionService.getAllAnswers(q1);
        assertTrue(q1.getAnswer1().equals(answers.get(0)));
        assertTrue(q1.getAnswer2().equals(answers.get(1)));
        assertTrue(q1.getAnswer3().equals(answers.get(2)));
        assertTrue(q1.getAnswer4().equals(answers.get(3)));
        assertTrue(q1.getAnswer5().equals(answers.get(4)));
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

        int maxLength = this.configReaderQuestions.getValueInt("maxLengthQuestion");

        StringBuilder tooLongQuestionText = new StringBuilder();
        for (int i = 0; i < maxLength + 1; i++) {
            tooLongQuestionText.append("a");
        }
        q.setQuestionText(tooLongQuestionText.toString());

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

        int maxLength = this.configReaderQuestions.getValueInt("maxLengthAnswer");

        StringBuilder tooLongAnswer = new StringBuilder();
        for (int i = 0; i < maxLength + 1; i++) {
            tooLongAnswer.append("a");
        }
        q.setAnswer1(tooLongAnswer.toString());

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

    // answer is marked as correct that doesn't exist

    @Test(expected = ServiceException.class)
    public void validateCorrectAnswerWithoutAnswerText() throws ServiceException {
        Question q = new Question();
        q.setQuestionText("Question");
        q.setAnswer1("answer");
        q.setAnswer2("another answer");
        q.setCorrectAnswers("123");
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

    // -----------------------------------------------------------------------------------------------------------------
    // search for questions
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void searchForQuestionsWithTwoResults() throws ServiceException {
        Question q1 = new Question();
        q1.setQuestionText("asdf");
        q1.setAnswer1("a1");
        q1.setAnswer2("a2");
        q1.setAnswer3("a3");
        q1.setAnswer4("a4");
        q1.setAnswer5("a5");
        q1.setCorrectAnswers("23");
        q1.setOptionalFeedback("feedback");
        questionService.create(q1);
        Question q2 = new Question();
        q2.setQuestionText("asdf");
        q2.setAnswer1("a1");
        q2.setAnswer2("a2");
        q2.setAnswer3("a3");
        q2.setAnswer4("a4");
        q2.setAnswer5("a5");
        q2.setCorrectAnswers("23");
        q2.setOptionalFeedback("feedback");
        questionService.create(q2);
        List<Question> questions = questionService.searchForQuestions(q1);
        assertTrue(questions.get(0).getQuestionText().equals(q1.getQuestionText()));
        assertTrue(questions.get(1).getQuestionText().equals(q2.getQuestionText()));
    }

    @Test
    public void searchForQuestionsWithOneResult() throws ServiceException {
        Question q1 = new Question();
        q1.setQuestionText("asdf");
        q1.setAnswer1("a1");
        q1.setAnswer2("a2");
        q1.setAnswer3("a3");
        q1.setAnswer4("a4");
        q1.setAnswer5("a5");
        q1.setCorrectAnswers("23");
        q1.setOptionalFeedback("feedback");
        questionService.create(q1);
        Question q2 = new Question();
        q2.setQuestionText("qwert");
        q2.setAnswer1("a1");
        q2.setAnswer2("a2");
        q2.setAnswer3("a3");
        q2.setAnswer4("a4");
        q2.setAnswer5("a5");
        q2.setCorrectAnswers("23");
        q2.setOptionalFeedback("feedback");
        questionService.create(q2);
        List<Question> questions = questionService.searchForQuestions(q1);
        assertTrue(questions.get(0).getQuestionText().equals(q1.getQuestionText()));
    }

    @Test
    public void searchForQuestionsWithNoResult() throws ServiceException {
        Question q1 = new Question();
        q1.setQuestionText("asdf");
        q1.setAnswer1("a1");
        q1.setAnswer2("a2");
        q1.setAnswer3("a3");
        q1.setAnswer4("a4");
        q1.setAnswer5("a5");
        q1.setCorrectAnswers("23");
        q1.setOptionalFeedback("feedback");
        questionService.create(q1);
        Question q = new Question((long) 0, "qwert", null, null, null, null, null, null, null, null, false);
        List<Question> questions = questionService.searchForQuestions(q);
        assertTrue(questions.size() == 0);
    }

    @Test
    public void searchForDeletedQuestion() throws ServiceException {
        Question q1 = new Question();
        q1.setQuestionText("asdf");
        q1.setAnswer1("a1");
        q1.setAnswer2("a2");
        q1.setAnswer3("a3");
        q1.setAnswer4("a4");
        q1.setAnswer5("a5");
        q1.setCorrectAnswers("23");
        q1.setOptionalFeedback("feedback");
        questionService.create(q1);
        questionService.delete(q1);
        List<Question> questions = questionService.searchForQuestions(q1);
        assertTrue(questions.size() == 0);
    }

    @After
    public void wrapUp() {
        try {
            this.configReaderQuestions.close();
        } catch (ConfigReaderException e) {
            LOG.error("Failed to close config reader for questions");
        }
    }
}
