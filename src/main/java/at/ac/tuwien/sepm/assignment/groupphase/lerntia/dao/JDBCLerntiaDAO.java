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
    private static final String SELECT_ANSWER_WHERE_QUESTION_LIKE = "SELECT id, answer FROM qanda WHERE question = ?";

    private final PreparedStatement getAnswerForQuestion;

    public JDBCLerntiaDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            final var connection = jdbcConnectionManager.getConnection();
            getAnswerForQuestion = connection.prepareStatement(SELECT_ANSWER_WHERE_QUESTION_LIKE);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Answer readAnswerForQuestion(Question question) throws PersistenceException {
        LOG.debug("called readAnswerForQuestion with question '{}'", question);
        try {
            getAnswerForQuestion.setString(1, question.getText());
            final var resultSet = getAnswerForQuestion.executeQuery();
            if (resultSet.next()) {
                final var answer = new Answer(
                    resultSet.getLong("id"),
                    resultSet.getString("answer")
                );
                if (resultSet.next()) {
                    throw new PersistenceException("More than one answer found for given question");
                }
                return answer;
            } else {
                throw new PersistenceException("No answer found for given question");
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
