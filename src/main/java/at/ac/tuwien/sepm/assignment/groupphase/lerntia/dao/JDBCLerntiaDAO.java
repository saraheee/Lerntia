package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Answer;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class JDBCLerntiaDAO implements LerntiaDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public JDBCLerntiaDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            final var connection = jdbcConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Answer readAnswerForQuestion(Question question) throws PersistenceException {
        LOG.debug("called readAnswerForQuestion with question '{}'", question);
        return null;
    }
}
