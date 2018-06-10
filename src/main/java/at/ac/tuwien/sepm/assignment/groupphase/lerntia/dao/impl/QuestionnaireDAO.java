package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
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
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Successfully found connection for QuestionnaireDAO!");
        } catch (PersistenceException e) {
            LOG.error("Couldn't find connection for QuestionnaireDAO!");
            throw e;
        }
    }

    @Override
    public void create(Questionnaire questionnaire) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for Questionnaire creation");
            PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTIONNAIRE_CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            try {
                psCreate.setLong(1, questionnaire.getCourseID());
                psCreate.setString(2, questionnaire.getName());
                psCreate.executeUpdate();
                LOG.info("Statement successfully sent for Questionnaire creation.");
                ResultSet generatedKeys = psCreate.getGeneratedKeys();
                try {
                    generatedKeys.next();
                    questionnaire.setId(generatedKeys.getLong(1));
                } finally {
                    generatedKeys.close();
                }
            } finally {
                psCreate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireDAO CREATE error: questionnaire couldn't been created, check if all mandatory values have been added, or if the connection to the Database is valid.");
        }
    }

    @Override
    public void update(Questionnaire questionnaire) throws PersistenceException {
        //method not yet implemented because feature regarding that method hasn't been started implementing yet
    }

    @Override
    public String getQuestionnaireName(Long id) throws PersistenceException {
        try {
            LOG.info("Prepare Statement to get Questionnaire Name from the Database.");
            PreparedStatement psCreate = connection.prepareStatement(SQL_QUESTIONNAIRE_GETNAME_STATEMENT);
            try {
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
            } finally {
                psCreate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Questionnaire DAO getName error!");
        }
    }

    @Override
    public void select(Questionnaire questionnaire) throws PersistenceException {
        try {
            LOG.info("Prepare statement for learning questionnaire selection.");
            PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTIONNAIRE_SELECT_STATEMENT);
            try {
                if (questionnaire != null) {
                    psUpdate.setLong(1, questionnaire.getId());
                    psUpdate.executeUpdate();
                }
                LOG.info("Learningquestionnaire successfully selected in Database.");
            } finally {
                psUpdate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("Learning questionnaire SELECTION DAO error!");
        }
    }

    @Override
    public void deselect(Questionnaire questionnaire) throws PersistenceException {
        try {
            LOG.info("Prepare statement for learning questionnaire selection.");
            PreparedStatement psUpdate = connection.prepareStatement(SQL_QUESTIONNAIRE_DESELECT_STATEMENT);
            try {
                psUpdate.setLong(1, questionnaire.getId());
                psUpdate.executeUpdate();
                LOG.info("Learning questionnaire successfully deselected in Database.");
            } finally {
                psUpdate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireDAO DESELECT error");
        }
    }

    @Override
    public Questionnaire getSelected() throws PersistenceException {
        try {
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
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireDAO GETSELECTED error!");
        }
    }
}