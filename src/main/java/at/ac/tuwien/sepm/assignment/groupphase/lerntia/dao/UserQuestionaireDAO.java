package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public UserQuestionaireDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
            LOG.info("Succesfully found connection for UserQuestionaireDAO.");
        } catch (SQLException e) {
            LOG.error("Couldn't find connection for UserQuestionaireDAO");
            throw new PersistenceException(e.getMessage());
        }
    }
    @Override
    public void create(UserQuestionnaire userQuestionnaire) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for new UserQuestionaire entry.");
            PreparedStatement pscreate = connection.prepareStatement(SQL_USERQUESTIONNAIREQ_CREATE_STATEMENT);
            pscreate.setString(1, userQuestionnaire.getMatriculationNumber());
            pscreate.setLong(2, userQuestionnaire.getQid());
            pscreate.setBoolean(3, userQuestionnaire.getDeleted());
            pscreate.execute();
            LOG.info("Statement for new UserQuestionaire entry succesfully sent.");
        } catch (SQLException e) {
            LOG.error("UserQuestionnaire CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }
}
