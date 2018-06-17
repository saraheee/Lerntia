package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IUserService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleUserService;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.Semester;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExamResultsWriterDAOTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;

    private IExamResultsWriterDAO examResultsWriterDAO;
    private IUserService userService;

    private String path;

    @Before
    public void setUp() {
        try {
            this.IexamResultsWriterDAO(new ExamResultsWriterDAO());
        } catch (PersistenceException e) {
            LOG.error("Failed create ExamResultsWriterDAO test-database");
        }

        this.path = System.getProperty("user.dir") + File.separator + "test.pdf";

        this.IUserService(new SimpleUserService());
    }

    private void IexamResultsWriterDAO(ExamResultsWriterDAO examResultsWriterDAO){
        this.examResultsWriterDAO = examResultsWriterDAO;
    }

    private void IUserService(SimpleUserService userService){
        this.userService = userService;
    }

    @After
    public void cleanup(){

        File file = new File(this.path);

        if (file.exists()){
            file.delete();
        }

    }

    @Test
    public void exportPDF() throws PersistenceException {

        Question firstQuestion = new Question();
        firstQuestion.setQuestionText("How you doing");
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

        examResultsWriterDAO.writeExamResults(examWriter);

        File file = new File(this.path);
        Assert.assertTrue(file.exists());
    }

    @Test(expected = Exception.class)
    public void exportPDFWrongImg() throws PersistenceException {

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

        examResultsWriterDAO.writeExamResults(examWriter);
    }
}
