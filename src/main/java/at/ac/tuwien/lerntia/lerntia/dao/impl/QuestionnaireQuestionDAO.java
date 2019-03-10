package at.ac.tuwien.lerntia.lerntia.dao.impl;

import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireQuestionDAO;
import at.ac.tuwien.lerntia.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.lerntia.util.JDBCConnectionManager;
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
        if (jdbcConnectionManager.isTestConnection()) {
            connection = jdbcConnectionManager.getTestConnection();
            LOG.info("Test database connection for CourseDAO retrieved.");
        } else {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for CourseDAO retrieved.");
        }
    }

    @Override
    public void create(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        if (questionnaireQuestion == null || questionnaireQuestion.getQid() == null || questionnaireQuestion.getQuestionid() == null) {
            throw new PersistenceException("Mindestens ein Wert der Instanz oder die QuestionnaireQuestion-Instanz selbst ist null!");
        }
        LOG.info("Prepare Statement for new QuestionnaireQuestion entry.");
        try (PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_CREATE_STATEMENT)) {
            psCreate.setLong(1, questionnaireQuestion.getQid());
            psCreate.setLong(2, questionnaireQuestion.getQuestionid());
            psCreate.setBoolean(3, false);
            psCreate.execute();
            LOG.info("Statement for new QuestionnaireQuestion entry successfully sent.");
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO CREATE Fehler: Die Istanz konnte nicht erzeugt werden. Bitte überprüfen, ob alle notwendigen Felder ausgefüllt sind und ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public List<QuestionnaireQuestion> search(QuestionnaireQuestion searchParameters) throws PersistenceException {
        if (searchParameters == null || searchParameters.getQid() == null) {
            throw new PersistenceException("Die Suchparameter oder die ID ist null!");
        }
        try {
            LOG.info("Prepare SEARCH QuestionnaireQuestion Statement. ");
            List<QuestionnaireQuestion> searchResults = new ArrayList<>();
            QuestionnaireQuestion questionnaireQuestion;
            String searchStatement = SQL_QUESTIONNAIREQUESTION_SEARCH_STATEMENT + searchParameters.getQid();
            try (ResultSet rs = connection.prepareStatement(searchStatement).executeQuery()) {
                while (rs.next()) {
                    questionnaireQuestion = new QuestionnaireQuestion();
                    questionnaireQuestion.setQid(searchParameters.getQid());
                    questionnaireQuestion.setQuestionid(rs.getLong(2));
                    questionnaireQuestion.setDeleted(rs.getBoolean(3));
                    searchResults.add(questionnaireQuestion);
                }
                LOG.info("All QuestionnaireQuestion matching the searchParameters found.");
                return searchResults;
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO SEARCH Fehler: Die Istanzen konnten nicht gesucht werden. Bitte überprüfen, ob die Suchparameter und die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        if (questionnaireQuestion == null || questionnaireQuestion.getQid() == null || questionnaireQuestion.getQuestionid() == null) {
            throw new PersistenceException("Mindestens ein Wert für das Löschen von QuestionnaireQuestion ist null!");
        }
        LOG.info("Prepare Statement for QuestionnaireQuestion delete from Database.");
        try (PreparedStatement psDelete = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT)) {
            psDelete.setLong(1, questionnaireQuestion.getQid());
            psDelete.setLong(2, questionnaireQuestion.getQuestionid());
            psDelete.executeUpdate();
            LOG.info("Statement for QuestionnaireQuestion Deletion successfully sent.");
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO DELETE Fehler: die Instanz konnte nicht gelöscht werden. Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid, long newQuestionId) throws PersistenceException {
        if (questionnaireQuestion == null || questionnaireQuestion.getQid() == null || questionnaireQuestion.getQuestionid() == null) {
            throw new PersistenceException("Mindestens ein Aktuelisierungswert von QuestionnaireQuestion ist null!");
        }
        try {
            LOG.info("Prepare Statement for updating existing QuestionnaireQuestion with new values.");
            try (PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT)) {
                psUpdate.setLong(1, newQid);
                psUpdate.setLong(2, newQuestionId);
                psUpdate.setLong(3, questionnaireQuestion.getQid());
                psUpdate.setLong(4, questionnaireQuestion.getQuestionid());
                psUpdate.executeUpdate();
                LOG.info("Statement for QuestionnaireQuestion Update successfully sent.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO UPDATE Fehler: die Instanz konnte nicht aktuelisiert werden. Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
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
                LOG.info("All questionnaire questions found.");
                return list;
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO READALL Fehler: nicht alle Instanzen konnten gelesen werden. Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }
    }
}