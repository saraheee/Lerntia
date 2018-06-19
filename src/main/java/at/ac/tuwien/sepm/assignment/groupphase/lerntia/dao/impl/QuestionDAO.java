package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;
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
    private static final String SQL_QUESTION_UPDATE_STATEMENT = "UPDATE Question SET questionText = ?, picture = ?, answer1 = ?, answer2 = ?, answer3 = ?, answer4 = ?, answer5 = ?, correctAnswers = ?, optionalFeedback = ? WHERE id = ?";
    private static final String SQL_QUESTION_SEARCH_STATEMENT = "SELECT * FROM QUESTION";
    private static final String SQL_QUESTION_DELETE_STATEMENT = "UPDATE Question SET isDeleted = TRUE WHERE id = ?";
    private static final String SQL_QUESTION_GET_STATEMENT = "SELECT * FROM question where id=";
    private static final String SQL_QUESTION_SEARCH_UPPER_STATEMENT = "SELECT * FROM QUESTION WHERE UPPER(questionText) LIKE UPPER(?) AND UPPER(answer1) LIKE UPPER(?) AND UPPER(answer2) LIKE UPPER(?) AND UPPER(answer3) LIKE UPPER(?) AND UPPER(answer4) LIKE UPPER(?)" +
        "AND UPPER(answer5) LIKE UPPER(?) AND isDeleted = FALSE";

    private Connection connection;

    private ILearnAlgorithmDAO learnAlgorithmDAO;

    @Autowired
    public QuestionDAO(JDBCConnectionManager jdbcConnectionManager, ILearnAlgorithmDAO learnAlgorithmDAO) throws PersistenceException {
        this.learnAlgorithmDAO = learnAlgorithmDAO;
        if (jdbcConnectionManager.isTestConnection()) {
            connection = jdbcConnectionManager.getTestConnection();
            LOG.info("Test database connection for QuestionDAO retrieved.");
        } else {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for QuestionDAO retrieved.");
        }
    }

    @Override
    public void create(Question question) throws PersistenceException {
        if (question == null || question.getQuestionText() == null || question.getAnswer1() == null
            || question.getAnswer2() == null || question.getCorrectAnswers() == null) {
            throw new PersistenceException("At least one of the question values is null!");
        }
        try {
            LOG.info("Prepare Statement for Question creation.");
            try (PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTION_CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
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

                QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();

                LOG.info("Question successfully saved in Database");
                try (ResultSet generatedKeys = psCreate.getGeneratedKeys()) {
                    generatedKeys.next();
                    question.setId(generatedKeys.getLong(1));
                    questionLearnAlgorithm.setID(question.getId());

                    learnAlgorithmDAO.create(questionLearnAlgorithm);
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionDAO CREATE error: question couldn't be created, check if the mandatory values have been added or if the connection to the Database is valid.");
        }
    }

    @Override
    public void update(Question question) throws PersistenceException {
        if (question == null || question.getQuestionText() == null || question.getPicture() == null || question.getAnswer1() == null
            || question.getAnswer2() == null || question.getCorrectAnswers() == null || question.getId() == null) {
            throw new PersistenceException("At least one of the question values is null!");
        }
        try (PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTION_UPDATE_STATEMENT)) {
            LOG.info("Prepare statement for question update.");
            psUpdate.setString(1, question.getQuestionText());
            psUpdate.setString(2, question.getPicture());
            psUpdate.setString(3, question.getAnswer1());
            psUpdate.setString(4, question.getAnswer2());
            psUpdate.setString(5, question.getAnswer3());
            psUpdate.setString(6, question.getAnswer4());
            psUpdate.setString(7, question.getAnswer5());
            psUpdate.setString(8, question.getCorrectAnswers());
            psUpdate.setString(9, question.getOptionalFeedback());
            psUpdate.setLong(10, question.getId());
            psUpdate.executeUpdate();

            QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
            questionLearnAlgorithm.setID(question.getId());
            learnAlgorithmDAO.reset(questionLearnAlgorithm);
            LOG.info("Question successfully updated in Database.");
        } catch (SQLException e) {
            throw new PersistenceException("QuestionDAO UPDATE error: question couldn't be updated, check if the mandatory values have been added or if the connection to the Database is valid.");
        }
    }

    @Override
    public List<Question> search(List<Question> questionList) throws PersistenceException {
        if (questionList == null) {
            throw new PersistenceException("Question list is null!");
        }
        try {
            LOG.info("Prepare search parameters for the Question search.");
            List<Question> searchResults = new ArrayList<>();
            Question questionParameter;
            Question foundQuestion;
            StringBuilder parameters = new StringBuilder();
            while (!questionList.isEmpty()) {
                questionParameter = questionList.get(0);
                parameters.append(parameters.length() == 0 ? " WHERE id= " + questionParameter.getId() : " OR id= " + questionParameter.getId());
                questionList.remove(0);
            }
            LOG.info("Question search parameters set.");
            String searchStatement = SQL_QUESTION_SEARCH_STATEMENT + parameters;
            LOG.info("Prepare Statement for Question search.");
            try (ResultSet rs = connection.prepareStatement(searchStatement).executeQuery()) {
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
            }
            LOG.info("All Questions matching the parameters found.");
            return searchResults;
        } catch (SQLException e) {
            throw new PersistenceException("QuestionDAO SEARCH error: questions couldn't have been found. Check if the search parameters are not null or/and if the connection to the Database is valid.");
        }
    }

    @Override
    public void delete(Question question) throws PersistenceException {
        if (question == null || question.getId() == null) {
            throw new PersistenceException("Question or question id is null!");
        }
        try {
            LOG.info("Prepare statement for question deletion.");
            try (PreparedStatement psDelete = connection.prepareStatement(SQL_QUESTION_DELETE_STATEMENT)) {
                psDelete.setLong(1, question.getId());
                psDelete.executeUpdate();
                question.setDeleted(true);
                LOG.info("Question successfully soft-deleted in Database.");
                QuestionLearnAlgorithm questionLearnAlgorithm = new QuestionLearnAlgorithm();
                questionLearnAlgorithm.setID(question.getId());
                learnAlgorithmDAO.delete(questionLearnAlgorithm);
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionDAO DELETE error: question couldn't be deleted, check if the connection to the Database is valid.");
        }
    }

    @Override
    public Question get(long id) throws PersistenceException {
        try {
            LOG.info("Create GET statement for Question.");
            Question q;
            try (ResultSet rsGet = connection.prepareStatement(SQL_QUESTION_GET_STATEMENT + id).executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionDAO GET error : matching question couldn't be found! Check if proper value given or if the connection to the database is valid.");
        }
    }

    @Override
    public List<Question> searchForQuestions(Question questionInput) throws PersistenceException {
        if (questionInput == null) {
            throw new PersistenceException("At least one of the question search values is null!");
        }
        List<Question> results = new ArrayList<>();
        LOG.info("Search a Questions which contains a part of a input String");
        try (PreparedStatement ps = connection.prepareStatement(SQL_QUESTION_SEARCH_UPPER_STATEMENT)) {
            ps.setString(1, "%" + questionInput.getQuestionText() + "%");
            ps.setString(2, "%" + questionInput.getAnswer1() + "%");
            ps.setString(3, "%" + questionInput.getAnswer2() + "%");
            ps.setString(4, "%" + questionInput.getAnswer3() + "%");
            ps.setString(5, "%" + questionInput.getAnswer4() + "%");
            ps.setString(6, "%" + questionInput.getAnswer5() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question();
                    q.setId(rs.getLong(1));
                    q.setQuestionText(rs.getString(2));
                    q.setPicture(rs.getString(3));
                    q.setAnswer1(rs.getString(4));
                    q.setAnswer2(rs.getString(5));
                    q.setAnswer3(rs.getString(6));
                    q.setAnswer4(rs.getString(7));
                    q.setAnswer5(rs.getString(8));
                    q.setCorrectAnswers(rs.getString(9));
                    q.setOptionalFeedback(rs.getString(10));
                    results.add(q);
                }
                LOG.info("All questions matching the search parameter found.");
            }
            return results;
        } catch (SQLException e) {
            throw new PersistenceException("QuestionDAO SEARCHFORQUESTION error: method didn't work, check if proper values have been added or if the connection to the Database is valid.");
        }
    }

}

