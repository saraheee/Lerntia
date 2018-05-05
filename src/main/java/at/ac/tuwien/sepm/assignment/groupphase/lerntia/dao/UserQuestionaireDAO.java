package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.UserQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UserQuestionaireDAO implements IUserQuestionaireDAO {

    private static final String SQL_USERQUESTIONNAIREQ_CREATE_STATEMENT ="INSERT INTO PUserQuestionnaire(matriculationNumber,qid,isDeleted) VALUES (?,?,?)";

    private Connection connection;

    public UserQuestionaireDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }
    @Override
    public void create(UserQuestionnaire userQuestionnaire) throws PersistenceException {
        try {
            PreparedStatement pscreate = connection.prepareStatement(SQL_USERQUESTIONNAIREQ_CREATE_STATEMENT);
            pscreate.setString(1, userQuestionnaire.getMatriculationNumber());
            pscreate.setLong(2, userQuestionnaire.getQid());
            pscreate.setBoolean(3, userQuestionnaire.getDeleted());
            pscreate.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException(e.getMessage());
        }
    }
}
