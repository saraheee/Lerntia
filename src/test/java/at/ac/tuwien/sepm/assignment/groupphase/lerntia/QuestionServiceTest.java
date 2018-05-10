package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleQuestionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class QuestionServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IQuestionService questionService;

    @Before
    public void setUp(){
        this.IQuestionService(new SimpleQuestionService());
    }

    private void IQuestionService(SimpleQuestionService simpleQuestionService) {
        this.questionService = simpleQuestionService;
    }

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

        String tooLongQuestionText = "";
        for (var i = 0; i < 201; i++){
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

        String tooLongAnswer = "";
        for (var i = 0; i < 201; i++){
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

    }
}
