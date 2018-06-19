package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;
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
public class LearnAlgorithmDAO implements ILearnAlgorithmDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_QUESTIONLEARNALGORITHM_CREATE_STATEMENT = "INSERT INTO QUESTIONALGOVALUE(QUESTIONID, SUCCESSVALUE, FAILUREVALUE,POINTS) VALUES (?,0,0,100.0)";
    private static final String SQL_QUESTIONLEARNALGORITHM_UPDATE_STATEMENT = "UPDATE QUESTIONALGOVALUE SET SUCCESSVALUE = ?, FAILUREVALUE = ?, POINTS = ? WHERE QUESTIONID = ?";
    private static final String SQL_QUESTIONLEARNALGORITHM_DELETE_STATEMENT = "DELETE FROM QUESTIONALGOVALUE WHERE QUESTIONID = ?";
    private static final String SQL_QUESTIONLEARNALGORITHM_RESET_STATEMENT = "UPDATE QUESTIONALGOVALUE SET SUCCESSVALUE = 0.0, FAILUREVALUE = 0.0, POINTS = 100.0 WHERE QUESTIONID = ?";
    private static final String SQL_QUESTIONLEARNALGORITHM_READALL_STATEMENT = "SELECT * FROM QUESTIONALGOVALUE";
    private static final String SQL_QUESTIONLEARNALGORITHM_SEARCH_STATEMENT = "SELECT * FROM QUESTIONALGOVALUE";
    private Connection connection;

    @Autowired
    public LearnAlgorithmDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        if (jdbcConnectionManager.isTestConnection()) {
            connection = jdbcConnectionManager.getTestConnection();
            LOG.info("Test database connection for LearnAlgorithmDAO retrieved.");
        } else {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for LearnAlgorithmDAO retrieved.");
        }
    }

    @Override
    public void create(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException {
        if (questionLearnAlgorithm == null) {
            throw new PersistenceException("Lernalgorithmus ist null!");
        }
        try {
            LOG.info("Prepare Create Statement for QuestionLearnAlgorithm.");
            try (PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_CREATE_STATEMENT)) {
                psCreate.setLong(1, questionLearnAlgorithm.getID());
                psCreate.executeUpdate();
                LOG.info("Create statement for QuestionLearnAlgorithm successfully sent.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO CREATE Fehler: Der Lernalgorithmus konnte nicht erzeugt werden. Bitte überprüfen, ob alle notwendigen Felder ausgefüllt sind und ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public void update(List<QuestionLearnAlgorithm> questionLearnAlgorithmList) throws PersistenceException {
        if (questionLearnAlgorithmList == null) {
            throw new PersistenceException("Die Liste der Fragen des Lernalgorithmus ist null!");
        }
        try (PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_UPDATE_STATEMENT)) {
            LOG.info("Create list of update statements for QuestionLearnAlgorithms");
            for (QuestionLearnAlgorithm questionLearnAlgorithm : questionLearnAlgorithmList) {
                psUpdate.setInt(1, questionLearnAlgorithm.getSuccessValue());
                psUpdate.setInt(2, questionLearnAlgorithm.getFailureValue());
                psUpdate.setDouble(3, questionLearnAlgorithm.getPoints());
                psUpdate.setLong(4, questionLearnAlgorithm.getID());
                psUpdate.executeUpdate();
            }
            LOG.info("All QuestionLearnAlgorithms have been successfully updated");
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO UPDATE Fehler: Der Lernalgorithmus konnte nicht aktuelisiert werden. Bitte überprüfen, ob alle notwendigen Felder ausgefüllt sind und ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public void delete(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException {
        if (questionLearnAlgorithm == null) {
            throw new PersistenceException("Lernalgorithmus ist null!");
        }
        try (PreparedStatement psDelete = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_DELETE_STATEMENT)) {
            LOG.info("Create Delete statement for QuestionLearnAlgorithm.");
            psDelete.setLong(1, questionLearnAlgorithm.getID());
            psDelete.executeUpdate();
            LOG.info("Delete Statement for QuestionLearnAlgorithm has been successfully sent");
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO DELETE Fehler: Der Lernalgorithmus konnte nicht gelöscht werden. Bitte überprüfen, ob die gegebene ID oder die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public List<QuestionLearnAlgorithm> readAll() throws PersistenceException {
        try {
            try (ResultSet rsReadAll = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_READALL_STATEMENT).executeQuery()) {
                List<QuestionLearnAlgorithm> readResults = new ArrayList<>();
                getResults(rsReadAll, readResults);
                LOG.info("Found all LearnAlgorithm Values.");
                return readResults;
            }
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO READALL Fehler: Der Lernalgorithmus konnte nicht aktuelisiert werden. Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }

    }

    @Override
    public void getResults(ResultSet rsReadAll, List<QuestionLearnAlgorithm> readResults) throws PersistenceException {
        if (readResults == null || rsReadAll == null) {
            throw new PersistenceException("Mindestens ein Wert des Lernalgorithmus oder der Lernalgorithmus selbst ist null!");
        }
        QuestionLearnAlgorithm questionLearnAlgorithm;
        try {
            while (rsReadAll.next()) {
                questionLearnAlgorithm = new QuestionLearnAlgorithm();
                questionLearnAlgorithm.setID(rsReadAll.getLong(1));
                questionLearnAlgorithm.setSuccessValue(rsReadAll.getInt(2));
                questionLearnAlgorithm.setFailureValue(rsReadAll.getInt(3));
                questionLearnAlgorithm.setPoints(rsReadAll.getDouble(4));
                readResults.add(questionLearnAlgorithm);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Ein Fehler ist passiert, während des Erzeugens der Resultat des Lernalgorithmus");
        }
    }

    @Override
    public List<QuestionLearnAlgorithm> search(List<QuestionLearnAlgorithm> questionAlgorithmList) throws PersistenceException {
        if (questionAlgorithmList == null) {
            throw new PersistenceException("Lernalgorithmus ist null!");
        }
        try {
            LOG.info("Create search Statement for QuestionLearnAlgorithm,");
            QuestionLearnAlgorithm searchParameter;
            List<QuestionLearnAlgorithm> searchResults = new ArrayList<>();
            StringBuilder parameters = new StringBuilder();
            while (!questionAlgorithmList.isEmpty()) {
                searchParameter = questionAlgorithmList.get(0);
                parameters.append(parameters.length() == 0 ? " WHERE questionid= " + searchParameter.getID() : " OR questionid= " + searchParameter.getID());
                questionAlgorithmList.remove(0);
            }

            String searchStatement = SQL_QUESTIONLEARNALGORITHM_SEARCH_STATEMENT + parameters;
            try (ResultSet rsSearch = connection.prepareStatement(searchStatement).executeQuery()) {
                getResults(rsSearch, searchResults);
                LOG.info("All search results have been found.");
                return searchResults;
            }
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO SEARCH Fehler: Der Lernalgorithmus konnte nicht gesucht werden. Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public void reset(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException {
        if (questionLearnAlgorithm == null) {
            throw new PersistenceException("Lernalgorithmus ist null!");
        }
        try (PreparedStatement psReset = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_RESET_STATEMENT)) {
            LOG.info("Prepare reset statement for QuestionLearnAlgorithm.");

            psReset.setLong(1, questionLearnAlgorithm.getID());
            psReset.executeUpdate();
            LOG.info("Reset Statement successfully sent.");
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO RESET Fehler: Der Lernalgorithmus konnte nicht neugestartet werden. Bitte überprüfen, ob mehrere notwendigen Felder nicht gegeben sind und ob die Datenbankverbindung gültig ist.");
        }
    }
}