package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UserQuestionnaireDAO implements IUserQuestionnaireDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_USERQUESTIONNAIREQ_CREATE_STATEMENT = "INSERT INTO PUserQuestionnaire(matriculationNumber,qid,isDeleted) VALUES (?,?,?)";
    private Connection connection;

    @Autowired
    public UserQuestionnaireDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Successfully found connection for UserQuestionnaireDAO.");
        } catch (PersistenceException e) {
            LOG.error("Couldn't find connection for UserQuestionnaireDAO");
            throw e;
        }
    }

    @Override
    public void create(UserQuestionnaire userQuestionnaire) throws PersistenceException {
        LOG.info("Prepare Statement for new UserQuestionnaire entry.");
        try (PreparedStatement psCreate = connection.prepareStatement(SQL_USERQUESTIONNAIREQ_CREATE_STATEMENT)) {
            psCreate.setString(1, userQuestionnaire.getMatriculationNumber());
            psCreate.setLong(2, userQuestionnaire.getQid());
            psCreate.setBoolean(3, userQuestionnaire.getDeleted());
            psCreate.execute();
            LOG.info("Statement for new UserQuestionnaire entry successfully sent.");
        } catch (SQLException e) {
            throw new PersistenceException("UserQuestionnaireDAO CREATE error: item couldn't been created, check if all mandatory values have been added and if the connection to the Database is valid.");
        }
    }
}
