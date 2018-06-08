package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionnaireQuestionDAO implements IQuestionnaireQuestionDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_QUESTIONNAIREQUESTION_CREATE_STATEMENT = "INSERT INTO Questionnairequestion(qid,questionid,isDeleted) VALUES (?,?,?)";
    private static final String SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT = "UPDATE questionnairequestion SET isDeleted=TRUE WHERE qid=? AND questionid=?";
    private static final String SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT = "UPDATE questionnairequestion SET qid=?, questionid=? WHERE qid=? AND questionid=?";
    private static final String SQL_QUESTIONNAIREQUESTION_SEARCH_STATEMENT = "SELECT * from Questionnairequestion where isDeleted = false and qid=";
    private static final String SQL_QUESTIoNNAIREQUESTION_READALL_STATEMENT = "SELECT * FROM Questionnairequestion WHERE isDeleted = FALSE ";
    private Connection connection;

    @Autowired
    public QuestionnaireQuestionDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for QuestionnaireQuestionDAO found.");
        } catch (PersistenceException e) {
            LOG.error("Couldn't find connection for QuestionnaireQuestionDAO!");
            throw e;
        }
    }

    @Override
    public void create(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        LOG.info("Prepare Statement for new QuestionnaireQuestion entry.");
        try (PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_CREATE_STATEMENT)) {
            psCreate.setLong(1, questionnaireQuestion.getQid());
            psCreate.setLong(2, questionnaireQuestion.getQuestionid());
            psCreate.setBoolean(3, false);
            psCreate.execute();
            LOG.info("Statement for new QuestionnaireQuestion entry successfully sent.");
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO CREATE error: item couldn't have been created, check if all mandatory values have been added and if the connection to the Database is valid.");
        }
    }

    @Override
    public List<QuestionnaireQuestion> search(QuestionnaireQuestion searchparameters) throws PersistenceException {
        try {
            LOG.info("Prepare SEARCH QuestionnaireQuestion Statement. ");
            List<QuestionnaireQuestion> searchresults = new ArrayList<>();
            QuestionnaireQuestion questionnaireQuestion;
            String searchStatement = SQL_QUESTIONNAIREQUESTION_SEARCH_STATEMENT + searchparameters.getQid();
            try (ResultSet rs = connection.prepareStatement(searchStatement).executeQuery()) {
                while (rs.next()) {
                    questionnaireQuestion = new QuestionnaireQuestion();
                    questionnaireQuestion.setQid(searchparameters.getQid());
                    questionnaireQuestion.setQuestionid(rs.getLong(2));
                    questionnaireQuestion.setDeleted(rs.getBoolean(3));
                    searchresults.add(questionnaireQuestion);
                }
                LOG.info("All QuestionnaireQuestion matching the searchparameters found.");
                return searchresults;
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO SEARCH error: couldn't find items, check if the searchparameters are valid and if the connection to the database is valid.");
        }
    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        LOG.info("Prepare Statement for QuestionnaireQuestion delete from Database.");
        try (PreparedStatement psDelete = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT)) {
            psDelete.setLong(1, questionnaireQuestion.getQid());
            psDelete.setLong(2, questionnaireQuestion.getQuestionid());
            psDelete.executeUpdate();
            LOG.info("Statement for QuestionnaireQuestion Deletion successfully sent.");
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO DELETE error: couldn't delete item in question, check if the connection to the Database is valid.");
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid, long newQuestionid) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for updating existing QuestionnaireQuestion with new values.");
            PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT);
            try {
                psUpdate.setLong(1, newQid);
                psUpdate.setLong(2, newQuestionid);
                psUpdate.setLong(3, questionnaireQuestion.getQid());
                psUpdate.setLong(4, questionnaireQuestion.getQuestionid());
                psUpdate.executeUpdate();
                LOG.info("Statement for QuestionnaireQuestion Update successfully sent.");
            } finally {
                psUpdate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO UPDATE error: item couldn't be updated, check if mandatory values have been added or if the connection to the Database is valid.");
        }
    }

    @Override
    public List<QuestionnaireQuestion> readAll() throws PersistenceException {
        try {
            LOG.info("Prepare Statement to read all available Questions of one Questionnaire from the Database.");
            ArrayList<QuestionnaireQuestion> list = new ArrayList<>();
            try (ResultSet rsReadAll = connection.prepareStatement(SQL_QUESTIoNNAIREQUESTION_READALL_STATEMENT).executeQuery()) {
                QuestionnaireQuestion question;
                while (rsReadAll.next()) {
                    question = new QuestionnaireQuestion();
                    question.setQid(rsReadAll.getLong(1));
                    question.setQuestionid(rsReadAll.getLong(2));
                    question.setDeleted(rsReadAll.getBoolean(3));
                    list.add(question);
                }
                LOG.info("All Questionnairequestions found.");
                return list;
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO READALL error: not all questions could have been found, check if connection to the Database is valid.");
        }
    }
}