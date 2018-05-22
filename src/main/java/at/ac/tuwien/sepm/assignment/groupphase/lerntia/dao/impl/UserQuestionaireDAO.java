package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IUserQuestionaireDAO;
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
public class UserQuestionaireDAO implements IUserQuestionaireDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_USERQUESTIONNAIREQ_CREATE_STATEMENT ="INSERT INTO PUserQuestionnaire(matriculationNumber,qid,isDeleted) VALUES (?,?,?)";
    private Connection connection;

    @Autowired
    public UserQuestionaireDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Succesfully found connection for UserQuestionaireDAO.");
        } catch (PersistenceException e) {
            LOG.error("Couldn't find connection for UserQuestionaireDAO");
            throw e;
        }
    }
    @Override
    public void create(UserQuestionnaire userQuestionnaire) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for new UserQuestionaire entry.");
            PreparedStatement pscreate = connection.prepareStatement(SQL_USERQUESTIONNAIREQ_CREATE_STATEMENT);
            try {
                pscreate.setString(1, userQuestionnaire.getMatriculationNumber());
                pscreate.setLong(2, userQuestionnaire.getQid());
                pscreate.setBoolean(3, userQuestionnaire.getDeleted());
                pscreate.execute();
                LOG.info("Statement for new UserQuestionaire entry succesfully sent.");
            }finally {
                pscreate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("UserQuestionaireDAO CREATE error: item couldn't been created, check if all mandatory values have been added and if the connection to the Database is valid.");
        }
    }
}
