package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class QuestionServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IQuestionService questionService;

    private ConfigReader configReaderQuestions;

    @Before
    public void setUp(){
        configReaderQuestions = new ConfigReader("questions");
        try {
            this.IQuestionService(new SimpleQuestionService(new QuestionDAO()));
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    private void IQuestionService(SimpleQuestionService simpleQuestionService) {
        this.questionService = simpleQuestionService;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // validation
    // -----------------------------------------------------------------------------------------------------------------

    // correct question

    @Test
    public void validateCorrectQuestion() throws ServiceException {
        Question q = new Question((long)0, "asdf", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);
        questionService.validate(q);
    }

    // no question text

    @Test(expected = ServiceException.class)
    public void validateNoQuestionText() throws ServiceException {
        Question q = new Question((long)0, "", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);
        questionService.validate(q);
    }

    // question text too long

    @Test(expected = ServiceException.class)
    public void validateTooLongQuestionText() throws ServiceException {
        Question q = new Question((long)0, "", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);

        var maxLength = this.configReaderQuestions.getValueInt("maxLengthQuestion");

        String tooLongQuestionText = "";
        for (var i = 0; i < maxLength + 1; i++){
            tooLongQuestionText += "a";
        }
        q.setQuestionText(tooLongQuestionText);

        questionService.validate(q);
    }

    // fewer than 2 answers present

    @Test(expected = ServiceException.class)
    public void validateFewerThanTwoAnswersPresent() throws ServiceException {
        Question q = new Question((long)0, "asdf", "", "a1", "", "", "", "", "1", "feedback", false);
        questionService.validate(q);
    }

    // answer is too long

    @Test(expected = ServiceException.class)
    public void validateTooLongAnswer() throws ServiceException {
        Question q = new Question((long)0, "asdf", "", "a1", "a2", "a3", "a4", "a5", "23", "feedback", false);

        var maxLength = this.configReaderQuestions.getValueInt("maxLengthAnswer");

        String tooLongAnswer = "";
        for (var i = 0; i < maxLength + 1; i++){
            tooLongAnswer += "a";
        }
        q.setAnswer1(tooLongAnswer);

        questionService.validate(q);
    }

    // answer is marked as correct but not present (has no text)

    @Test(expected = ServiceException.class)
    public void validateCorrectAnswerNotPresent() throws ServiceException {
        Question q = new Question((long)0, "asdf", "", "a1", "a2", "a3", "a4", "", "5", "feedback", false);
        questionService.validate(q);
    }

    // answer is marked as correct with index 0

    @Test(expected = ServiceException.class)
    public void validateCorrectAnswerWithIndexZero() throws ServiceException {
        Question q = new Question((long)0, "asdf", "", "a1", "a2", "a3", "a4", "", "120", "feedback", false);
        questionService.validate(q);
    }

    // answer is makred as correct with index higher than 5

    @Test(expected = ServiceException.class)
    public void validateCorrectAnswerWithIndexHigherThanFive() throws ServiceException {
        Question q = new Question((long)0, "asdf", "", "a1", "a2", "a3", "a4", "", "126", "feedback", false);
        questionService.validate(q);
    }

    @After
    public void wrapUp(){
        this.configReaderQuestions.close();
    }
}
