package at.ac.tuwien.lerntia.lerntia;

import at.ac.tuwien.lerntia.lerntia.dao.impl.ExamResultsWriterDAO;
import at.ac.tuwien.lerntia.lerntia.dto.ExamWriter;
import at.ac.tuwien.lerntia.lerntia.dto.Question;
import at.ac.tuwien.lerntia.lerntia.dto.User;
import at.ac.tuwien.lerntia.lerntia.service.IExamResultsWriterService;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.service.impl.ExamResultsWriterService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class ExamResultsWriterServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private IExamResultsWriterService examResultsWriterService;

    private String path;

    @Before
    public void setUp() {
        try {
            this.IexamResultsWriterService(new ExamResultsWriterService(new ExamResultsWriterDAO()));
        } catch (PersistenceException e) {
            LOG.error("Failed create ExamResultsWriterDAO test-database");
        }

        this.path = System.getProperty("user.dir") + File.separator + "test.pdf";
    }

    private void IexamResultsWriterService(IExamResultsWriterService examResultsWriterService) {
        this.examResultsWriterService = examResultsWriterService;
    }

    @After
    public void cleanup() {

        File file = new File(this.path);

        if (file.exists()) {
            if (file.delete()) {
                LOG.debug("File deleted.");
            }
        }
    }

    @Test
    public void exportPDF() throws ServiceException {

        Question firstQuestion = new Question();
        firstQuestion.setQuestionText("How you doing now");
        firstQuestion.setAnswer1("No");
        firstQuestion.setAnswer2("yes");
        firstQuestion.setCorrectAnswers("1");
        firstQuestion.setPicture("");

        Question secondQuestion = new Question();
        secondQuestion.setQuestionText("How you doing");
        secondQuestion.setAnswer1("No");
        secondQuestion.setAnswer2("yes");
        secondQuestion.setAnswer3("yes");
        secondQuestion.setAnswer4("yes");
        secondQuestion.setAnswer5("yes");
        secondQuestion.setCorrectAnswers("1");
        secondQuestion.setPicture("test.png");

        Question thirdQuestion = new Question();
        thirdQuestion.setQuestionText("How you doing");
        thirdQuestion.setAnswer3("yes");
        thirdQuestion.setAnswer4("yes");
        thirdQuestion.setAnswer5("yes");
        thirdQuestion.setCorrectAnswers("1");
        thirdQuestion.setCheckedAnswers("1");
        thirdQuestion.setPicture("test_2.jpg");

        ArrayList<Question> questions = new ArrayList<>();

        questions.add(firstQuestion);
        questions.add(secondQuestion);
        questions.add(thirdQuestion);

        User student = new User();

        student.setName("Test Student");
        student.setMatriculationNumber("123456");

        ExamWriter examWriter = new ExamWriter();

        examWriter.setName("test_exam");
        examWriter.setPath(path);
        examWriter.setQuestions(questions);
        examWriter.setUser(student);

        examResultsWriterService.writeExamResults(examWriter);

        File file = new File(this.path);
        Assert.assertTrue(file.exists());
    }

    @Test(expected = ServiceException.class)
    public void exportPDFWrongImg() throws ServiceException {

        Question firstQuestion = new Question();
        firstQuestion.setQuestionText("How you doing");
        firstQuestion.setAnswer1("No");
        firstQuestion.setAnswer2("yes");
        firstQuestion.setCorrectAnswers("1");
        firstQuestion.setPicture("asdf");

        ArrayList<Question> questions = new ArrayList<>();

        questions.add(firstQuestion);

        User student = new User();

        student.setName("Test Student");
        student.setMatriculationNumber("123456");

        ExamWriter examWriter = new ExamWriter();

        examWriter.setName("test_exam");
        examWriter.setPath(path);
        examWriter.setQuestions(questions);
        examWriter.setUser(student);

        examResultsWriterService.writeExamResults(examWriter);
    }
}
