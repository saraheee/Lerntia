package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class QuestionDAO implements IQuestionDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Connection connection;
    private static final String SQL_QUESTION_CREATE_STATEMENT="INSERT INTO Question(id,questionText,picture,answer1,answer2,answer3,answer4,answer5,correctAnswers,optionalFeedback) VALUES (default,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_QUESTION_UPDATE_STATEMENT="UPDATE Question SET questionText = ?, picture = ?, answer1 = ?, answer2 = ?, answer3 = ?, answer4 = ?, answer5 = ?, correctAnswers = ?, optionalFeedback = ? ";
    private static final String SQL_QUESTION_SEARCH_STATEMENT="";
    private static final String SQL_QUESTION_DELETE_STATEMENT="UPDATE Question SET isDeleted = true where id = ?";
    private static final String SQL_QUESTION_READALL_STATEMENT="";
    private static final String SQL_QUESTION_GET_STATEMENT = "SELECT * FROM question where id=";

    public QuestionDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
            LOG.info("Database connection for QuestionDAO obtained.");
        } catch (SQLException e) {
            LOG.error("Question Constructor failed while trying to get connection!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void create(Question question) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for Question creation.");
            PreparedStatement pscreate = connection.prepareStatement(SQL_QUESTION_CREATE_STATEMENT);
            pscreate.setString(1,question.getQuestionText());
            pscreate.setString(2,question.getPicture());
            pscreate.setString(3,question.getAnswer1());
            pscreate.setString(4,question.getAnswer2());
            pscreate.setString(5,question.getAnswer3());
            pscreate.setString(6,question.getAnswer4());
            pscreate.setString(7,question.getAnswer5());
            pscreate.setString(8,question.getCorrectAnswers());
            pscreate.setString(9,question.getOptionalFeedback());
            pscreate.executeUpdate();
            LOG.info("Question succesfully saved in Database");
        } catch (SQLException e) {
            LOG.error("Question CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(Question question) throws PersistenceException {
        try {
            LOG.info("Prepare statement for question update.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_QUESTION_UPDATE_STATEMENT);
            psupdate.setString(1,question.getQuestionText());
            psupdate.setString(2,question.getPicture());
            psupdate.setString(3,question.getAnswer1());
            psupdate.setString(4,question.getAnswer2());
            psupdate.setString(5,question.getAnswer3());
            psupdate.setString(6,question.getAnswer4());
            psupdate.setString(7,question.getAnswer5());
            psupdate.setString(8,question.getCorrectAnswers());
            psupdate.setString(9,question.getOptionalFeedback());
            psupdate.executeUpdate();
            LOG.info("Question succesfully updated in Database.");
        } catch (SQLException e) {
            LOG.error("Question UPDATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void search(Question question) throws PersistenceException {
        //TODO implement search for questions if necessary
    }

    @Override
    public void delete(Question question) throws PersistenceException {
        try {
            LOG.info("Prepare statement for question deletion.");
            PreparedStatement psdelete = connection.prepareStatement(SQL_QUESTION_DELETE_STATEMENT);
            psdelete.setLong(1,question.getId());
            psdelete.executeUpdate();
            LOG.info("Question succesfully soft-deleted in Database.");
        } catch (SQLException e) {
            LOG.error("Question DELETE DAO error!");
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public Question get(long id) throws PersistenceException {
        try {
            LOG.info("Create GET statement for Question.");
            String help ="";
            help= SQL_QUESTION_GET_STATEMENT + id;
            ResultSet rsget = connection.prepareStatement(help).executeQuery();

            Question q = new Question();
            if (rsget.next()){
                q.setId(id);
                q.setQuestionText(rsget.getString(2));
                q.setPicture(rsget.getString(3));
                q.setAnswer1(rsget.getString(4));
                q.setAnswer2(rsget.getString(5));
                q.setAnswer3(rsget.getString(6));
                q.setAnswer4(rsget.getString(7));
                q.setAnswer4(rsget.getString(8));
                q.setAnswer5(rsget.getString(9));
                q.setCorrectAnswers(rsget.getString(10));
                q.setOptionalFeedback(rsget.getString(11));
                q.setDeleted(rsget.getBoolean(12));
            }
            LOG.info("Matching Question found.");
            return q;
        } catch (SQLException e) {
            LOG.error("Question GET DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }
}
