package at.ac.tuwien.lerntia.lerntia.dao.impl;

import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.lerntia.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.lerntia.lerntia.dto.Questionnaire;
import at.ac.tuwien.lerntia.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.*;


@Component
public class QuestionnaireDAO implements IQuestionnaireDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_QUESTIONNAIRE_CREATE_STATEMENT = "INSERT INTO Questionnaire(courseid,id,name) VALUES (?,default,?)";
    private static final String SQL_QUESTIONNAIRE_GETNAME_STATEMENT = "SELECT name FROM Questionnaire WHERE id = ?";
    private static final String SQL_QUESTIONNAIRE_SELECT_STATEMENT = "UPDATE Questionnaire SET selected = TRUE WHERE id = ?";
    private static final String SQL_QUESTIONNAIRE_DESELECT_STATEMENT = "UPDATE Questionnaire SET selected = FALSE WHERE id = ?";
    private static final String SQL_QUESTIONNAIRE_GETSELECTED_STATEMENT = "SELECT * FROM Questionnaire WHERE selected = TRUE";

    private Connection connection;

    @Autowired
    public QuestionnaireDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        if (jdbcConnectionManager.isTestConnection()) {
            connection = jdbcConnectionManager.getTestConnection();
            LOG.info("Test database connection for QuestionnaireDAO retrieved.");
        } else {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for QuestionnaireDAO retrieved.");
        }
    }

    @Override
    public void create(Questionnaire questionnaire) throws PersistenceException {
        LOG.info("Prepare Statement for Questionnaire creation");
        if (questionnaire == null || questionnaire.getCourseID() == null || questionnaire.getName() == null) {
            throw new PersistenceException("Mindestens ein Wert des Fragenbogens ist null!");
        }
        try (PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTIONNAIRE_CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
            try {
                psCreate.setLong(1, questionnaire.getCourseID());
                psCreate.setString(2, questionnaire.getName());
                psCreate.executeUpdate();
                LOG.info("Statement successfully sent for Questionnaire creation.");
                try (ResultSet generatedKeys = psCreate.getGeneratedKeys()) {
                    generatedKeys.next();
                    questionnaire.setId(generatedKeys.getLong(1));
                }
            } catch (SQLException e) {
                throw new PersistenceException("QuestionnaireDAO CREATE Fehler: Der Fragenbogen konnte nicht erzeugt werden, bitte überprüfen, ob alle notwendigen Felder ausgefüllt sind und ob die Datenbankverbindung gültig ist.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireDAO CREATE Fehler: Der Fragenbogen konnte nicht erzeugt werden, bitte überprüfen, ob alle notwendigen Felder ausgefüllt sind und ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public String getQuestionnaireName(Long id) throws PersistenceException {
        if (id == null) {
            throw new PersistenceException("Die ID ist null!");
        }
        LOG.info("Prepare Statement to get Questionnaire Name from the Database.");
        try (PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTIONNAIRE_GETNAME_STATEMENT)) {
            psCreate.setLong(1, id);
            try (ResultSet rsreadall = psCreate.executeQuery()) {
                if (rsreadall.next()) {
                    LOG.info("Questionnaire name found");
                    return rsreadall.getString(1);
                } else {
                    LOG.info("No name found.");
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireDAO getName Fehler: Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public void select(Questionnaire questionnaire) throws PersistenceException {
        if (questionnaire == null || questionnaire.getId() == null) {
            throw new PersistenceException("Mindestens ein Wert des Fragenbogens oder der Fragenbogen selbst ist null!");
        }
        LOG.info("Prepare statement for learning questionnaire selection.");
        try (PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTIONNAIRE_SELECT_STATEMENT)) {
            psUpdate.setLong(1, questionnaire.getId());
            psUpdate.executeUpdate();

            LOG.info("Learningquestionnaire successfully selected in Database.");
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireDAO SELECT Fehler: Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public void deselect(Questionnaire questionnaire) throws PersistenceException {
        if (questionnaire == null || questionnaire.getId() == null) {
            throw new PersistenceException("Mindestens ein Wert des Fragenbogens oder der Fragenbogen selbst ist null!");
        }
        LOG.info("Prepare statement for learning questionnaire selection.");
        try (PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTIONNAIRE_DESELECT_STATEMENT)) {
            psUpdate.setLong(1, questionnaire.getId());
            psUpdate.executeUpdate();
            LOG.info("Learning questionnaire successfully deselected in Database.");
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireDAO DESELECT Fehler: Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }

    }

    @Override
    public Questionnaire getSelected() throws PersistenceException {
        LOG.info("Prepare Statement to get selected LearingQuestionnaire from the Database.");
        try (ResultSet rsReadAll = connection.prepareStatement(SQL_QUESTIONNAIRE_GETSELECTED_STATEMENT).executeQuery()) {
            Questionnaire questionnaire;
            if (rsReadAll.next()) {
                questionnaire = new LearningQuestionnaire();
                questionnaire.setId(rsReadAll.getLong(2));
                questionnaire.setName(rsReadAll.getString(3));
                LOG.info("Selected Questionnaire found.");
                return questionnaire;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireDAO GETSELECTED Fehler: Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }
    }
}