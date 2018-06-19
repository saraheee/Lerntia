package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
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
public class LearningQuestionnaireDAO implements ILearningQuestionnaireDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_LEARNINGQUESTIONNAIRE_CREATE_STATEMENT = "INSERT INTO LearningQuestionnaire(id) VALUES (?)";
    private static final String SQL_LEARNINGQUESTIONNAIRE_READALL_STATEMENT = "SELECT * FROM LearningQuestionnaire WHERE id IN (SELECT id FROM Questionnaire WHERE isDeleted = FALSE)";

    private Connection connection;
    private IQuestionnaireDAO questionnaireDAO;

    @Autowired
    public LearningQuestionnaireDAO(IQuestionnaireDAO questionnaireDAO, JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        this.questionnaireDAO = questionnaireDAO;
        if (jdbcConnectionManager.isTestConnection()) {
            connection = jdbcConnectionManager.getTestConnection();
            LOG.info("Test database connection for LearningQuestionnaireDAO retrieved.");
        } else {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for LearningQuestionnaireDAO retrieved.");
        }
    }


    @Override
    public void create(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {

        if (learningQuestionnaire == null) {
            throw new PersistenceException("Lernfragenbogen oder die ID ist null!");
        }
        try {
            LOG.info("Create preparation for LearningQuestionnaire and Questionnaire.");
            questionnaireDAO.create(learningQuestionnaire);
            LOG.info("Entry for general Questionnaire successful.");
            LOG.info("Prepare Statement for LearningQuestionnaire...");
            try (PreparedStatement psCreate = connection.prepareStatement(SQL_LEARNINGQUESTIONNAIRE_CREATE_STATEMENT)) {

                psCreate.setLong(1, learningQuestionnaire.getId());
                psCreate.executeUpdate();
                LOG.info("Statement for LearningQuestionnaire successfully sent.");

            } catch (SQLException e) {
                throw new PersistenceException("LearningQuestionnaireDAO CREATE Fehler: Der Lernfragenbogen konnte nicht erzeugt werden. Bitte überprüfen, ob alle notwendigen Felder ausgefüllt sind und ob die Datenbankverbindung gültig ist.");
            }

        } catch (PersistenceException e) {
            throw new PersistenceException("LearningQuestionnaireDAO CREATE Fehler: Der Fragenbogen konnte nicht erzeugt werden. Folglich, der Lernfragenbogen konnte nicht erzeugt werden.");
        }
    }

    @Override
    public List<LearningQuestionnaire> readAll() throws PersistenceException {
        try {
            LOG.info("Prepare Statement to read all LearingQuestionnaires from the Database.");
            ArrayList<LearningQuestionnaire> list = new ArrayList<>();
            try (ResultSet rsReadAll = connection.prepareStatement(SQL_LEARNINGQUESTIONNAIRE_READALL_STATEMENT).executeQuery()) {
                LearningQuestionnaire learning;
                while (rsReadAll.next()) {
                    learning = new LearningQuestionnaire();
                    learning.setId(rsReadAll.getLong(1));
                    learning.setName(questionnaireDAO.getQuestionnaireName(rsReadAll.getLong(1)));
                    list.add(learning);
                }
                LOG.info("All LearningQuestionnaires found.");
                return list;
            }
        } catch (SQLException e) {
            throw new PersistenceException("LearningQuestionnaireDAO READALL Fehler: manche Lernfragenbogen waren nicht gefünden, bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }
    }

    @Override
    public void select(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {
        if (learningQuestionnaire == null) {
            throw new PersistenceException("Lernfragenbogen ist null!");
        }
        LOG.info("Select LearningQuestionnaire");
        questionnaireDAO.select(learningQuestionnaire);
    }

    @Override
    public void deselect(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {
        if (learningQuestionnaire == null) {
            throw new PersistenceException("Lernfragenbogen ist null!");
        }
        LOG.info("Deselect LearningQuestionnaire");
        questionnaireDAO.deselect(learningQuestionnaire);
    }

    @Override
    public LearningQuestionnaire getSelected() throws PersistenceException {
        LOG.info("Get selected LearningQuestionnaire.");
        return (LearningQuestionnaire) questionnaireDAO.getSelected();
    }

}