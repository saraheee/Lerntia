package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class QuestionnaireQuestionDAO implements IQuestionnaireQuestionDAO {
    private static final String SQL_QUESTIONNAIREQUESTION_CREATE_STATEMENT ="INSERT INTO Questionnairequestion(qid,questionid,isDeleted) VALUES (?,?,?)";
    private static final String SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT = "UPDATE questionnairequestion set isDeleted=true where qid=? and questionid=?";
    private static final String SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT = "UPDATE questionnairequestion set qid=?, questionid=? where qid=? and questionid=?";
    private static final String SQL_QUESTIONNAIREQUESTION_READALL_STATEMENT = "";
    private Connection connection = null;

    public QuestionnaireQuestionDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }
    @Override
    public void create(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        try {
            PreparedStatement pscreate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_CREATE_STATEMENT);
            pscreate.setLong(1,questionnaireQuestion.getQid());
            pscreate.setLong(2,questionnaireQuestion.getQuestionid());
            pscreate.setBoolean(3,questionnaireQuestion.getDeleted());
            pscreate.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        try {
            PreparedStatement psdelete= connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT);
            psdelete.setLong(1,questionnaireQuestion.getQid());
            psdelete.setLong(2,questionnaireQuestion.getQuestionid());
            psdelete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid,long newQuestionid) throws PersistenceException {
        try {
            PreparedStatement psupdate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT);
            psupdate.setLong(1,newQid);
            psupdate.setLong(2,newQuestionid);
            psupdate.setLong(3,questionnaireQuestion.getQid());
            psupdate.setLong(4,questionnaireQuestion.getQuestionid());
            psupdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List readAll() throws PersistenceException {
        //TODO see if there is any need for an readALl method or similar methods to it (readlAll,get,Search)
        return null;
    }
}