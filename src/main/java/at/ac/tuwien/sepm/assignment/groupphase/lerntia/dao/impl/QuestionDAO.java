package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionDAO implements IQuestionDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_QUESTION_CREATE_STATEMENT = "INSERT INTO Question(id,questionText,picture,answer1,answer2,answer3,answer4,answer5,correctAnswers,optionalFeedback) VALUES (default,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_QUESTION_UPDATE_STATEMENT = "UPDATE Question SET questionText = ?, picture = ?, answer1 = ?, answer2 = ?, answer3 = ?, answer4 = ?, answer5 = ?, correctAnswers = ?, optionalFeedback = ? ";
    private static final String SQL_QUESTION_SEARCH_STATEMENT = "SELECT * FROM QUESTION";
    private static final String SQL_QUESTION_DELETE_STATEMENT = "UPDATE Question SET isDeleted = TRUE WHERE id = ?";
    private static final String SQL_QUESTION_READALL_STATEMENT = "";
    private static final String SQL_QUESTION_GET_STATEMENT = "SELECT * FROM question where id=";
    private Connection connection;

    @Autowired
    public QuestionDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Database connection for QuestionDAO obtained.");
        } catch (PersistenceException e) {
            LOG.error("Question Constructor failed while trying to get connection!");
            throw e;
        }
    }

    @Override
    public void create(Question question) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for Question creation.");
            try (PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTION_CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
                try {
                    psCreate.setString(1, question.getQuestionText());
                    psCreate.setString(2, question.getPicture());
                    psCreate.setString(3, question.getAnswer1());
                    psCreate.setString(4, question.getAnswer2());
                    psCreate.setString(5, question.getAnswer3());
                    psCreate.setString(6, question.getAnswer4());
                    psCreate.setString(7, question.getAnswer5());
                    psCreate.setString(8, question.getCorrectAnswers());
                    psCreate.setString(9, question.getOptionalFeedback());
                    psCreate.executeUpdate();
                    LOG.info("Question succesfully saved in Database");
                    try (ResultSet generatedKeys = psCreate.getGeneratedKeys()) {
                        try {
                            generatedKeys.next();
                            question.setId(generatedKeys.getLong(1));
                        } finally {
                            generatedKeys.close();
                        }
                    }
                } finally {
                    psCreate.close();
                }
            }
        } catch (SQLException e) {
            LOG.error("Question CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(Question question) throws PersistenceException {

        LOG.info("Prepare statement for question update.");
        try (PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTION_UPDATE_STATEMENT)) {
            try {
                psUpdate.setString(1, question.getQuestionText());
                psUpdate.setString(2, question.getPicture());
                psUpdate.setString(3, question.getAnswer1());
                psUpdate.setString(4, question.getAnswer2());
                psUpdate.setString(5, question.getAnswer3());
                psUpdate.setString(6, question.getAnswer4());
                psUpdate.setString(7, question.getAnswer5());
                psUpdate.setString(8, question.getCorrectAnswers());
                psUpdate.setString(9, question.getOptionalFeedback());
                psUpdate.executeUpdate();
                LOG.info("Question successfully updated in Database.");
            } finally {
                psUpdate.close();
            }
        } catch (SQLException e) {
            LOG.error("Question UPDATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public List<Question> search(List<Question> questionList) throws PersistenceException {
        try {
            List<Question> searchResults = new ArrayList<>();
            Question questionParameter;
            Question foundQuestion;
            StringBuilder parameters = new StringBuilder();
            while (!questionList.isEmpty()) {
                questionParameter = questionList.get(0);
                parameters.append(parameters.length() == 0 ? " WHERE id= " + questionParameter.getId() : " OR id= " + questionParameter.getId());
                questionList.remove(0);
            }
            String searchStatement = SQL_QUESTION_SEARCH_STATEMENT + parameters;
            try (ResultSet rs = connection.prepareStatement(searchStatement).executeQuery()) {
                try {
                    //id,questionText,picture,answer1,answer2,answer3,answer4,answer5,correctAnswers,optionalFeedback
                    while (rs.next()) {
                        foundQuestion = new Question();
                        foundQuestion.setId(rs.getLong(1));
                        foundQuestion.setQuestionText(rs.getString(2));
                        foundQuestion.setPicture(rs.getString(3));
                        foundQuestion.setAnswer1(rs.getString(4));
                        foundQuestion.setAnswer2(rs.getString(5));
                        foundQuestion.setAnswer3(rs.getString(6));
                        foundQuestion.setAnswer4(rs.getString(7));
                        foundQuestion.setAnswer5(rs.getString(8));
                        foundQuestion.setCorrectAnswers(rs.getString(9));
                        foundQuestion.setOptionalFeedback(rs.getString(10));
                        searchResults.add(foundQuestion);
                    }
                } finally {
                    rs.close();
                }
            }
            return searchResults;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void delete(Question question) throws PersistenceException {
        try {
            LOG.info("Prepare statement for question deletion.");
            try (PreparedStatement psDelete = connection.prepareStatement(SQL_QUESTION_DELETE_STATEMENT)) {
                try {
                    psDelete.setLong(1, question.getId());
                    psDelete.executeUpdate();
                    LOG.info("Question successfully soft-deleted in Database.");
                } finally {
                    psDelete.close();
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public Question get(long id) throws PersistenceException {
        try {
            LOG.info("Create GET statement for Question.");
            String help;
            help = SQL_QUESTION_GET_STATEMENT + id;
            Question q;
            try (ResultSet rsGet = connection.prepareStatement(help).executeQuery()) {
                try {
                    q = new Question();
                    if (rsGet.next()) {
                        q.setId(id);
                        q.setQuestionText(rsGet.getString(2));
                        q.setPicture(rsGet.getString(3));
                        q.setAnswer1(rsGet.getString(4));
                        q.setAnswer2(rsGet.getString(5));
                        q.setAnswer3(rsGet.getString(6));
                        q.setAnswer4(rsGet.getString(7));
                        q.setAnswer5(rsGet.getString(8));
                        q.setCorrectAnswers(rsGet.getString(9));
                        q.setOptionalFeedback(rsGet.getString(10));
                        q.setDeleted(rsGet.getBoolean(11));
                    }
                    LOG.info("Matching Question found.");
                    return q;
                } finally {
                    rsGet.close();
                }
            }
        } catch (SQLException e) {
            LOG.error("Question GET DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }
}