package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Classes;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Interfaces.IQuestionnaireQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionnaireQuestionDAO implements IQuestionnaireQuestionDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_QUESTIONNAIREQUESTION_CREATE_STATEMENT ="INSERT INTO Questionnairequestion(qid,questionid,isDeleted) VALUES (?,?,?)";
    private static final String SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT = "UPDATE questionnairequestion set isDeleted=true where qid=? and questionid=?";
    private static final String SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT = "UPDATE questionnairequestion set qid=?, questionid=? where qid=? and questionid=?";
    private static final String SQL_QUESTIONNAIREQUESTION_READALL_STATEMENT = "";
    private static final String SQL_QUESTIONNAIREQUESTION_SEARCH_STATEMENT = "SELECT * from Questionnairequestion where qid=";
    private Connection connection = null;


    public QuestionnaireQuestionDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
            LOG.info("Connection for QuestionnaireQuestionDAO found.");
        } catch (PersistenceException e) {
            LOG.error("Couldn't find connection for QuestionnaireQuestionDAO!");
            throw e;
        }
    }
    @Override
    public void create(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for new QuestionnaireQuestion entry.");
            PreparedStatement pscreate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_CREATE_STATEMENT);
            pscreate.setLong(1,questionnaireQuestion.getQid());
            pscreate.setLong(2,questionnaireQuestion.getQuestionid());
            pscreate.setBoolean(3,questionnaireQuestion.getDeleted());
            pscreate.execute();
            LOG.info("Statement for new QuestionnaireQuestion entry succesfully sent.");
        } catch (SQLException e) {
            LOG.error("QuestionnaireQuestion CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public List<QuestionnaireQuestion> search(QuestionnaireQuestion searchparameters) throws PersistenceException{

        try {
            List<QuestionnaireQuestion> searchresults = new ArrayList<>();
            QuestionnaireQuestion questionnaireQuestion;
            String searchStatement= SQL_QUESTIONNAIREQUESTION_SEARCH_STATEMENT+searchparameters.getQid();
            ResultSet rs = connection.prepareStatement(searchStatement).executeQuery();
            while (rs.next()){
                questionnaireQuestion = new QuestionnaireQuestion();
                questionnaireQuestion.setQid(rs.getLong(1));
                questionnaireQuestion.setQuestionid(rs.getLong(2));
                questionnaireQuestion.setDeleted(rs.getBoolean(3));
                searchresults.add(questionnaireQuestion);
            }

            return searchresults;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for QuestionnaireQuestion delete from Database.");
            PreparedStatement psdelete= connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT);
            psdelete.setLong(1,questionnaireQuestion.getQid());
            psdelete.setLong(2,questionnaireQuestion.getQuestionid());
            psdelete.executeUpdate();
            LOG.info("Statement for QuestionnaireQuestion Deletion succesfully sent.");
        } catch (SQLException e) {
            LOG.error("QuestionnaireQueestion DELETE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid,long newQuestionid) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for updating existing QuestionnaireQuestion with new values.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT);
            psupdate.setLong(1,newQid);
            psupdate.setLong(2,newQuestionid);
            psupdate.setLong(3,questionnaireQuestion.getQid());
            psupdate.setLong(4,questionnaireQuestion.getQuestionid());
            psupdate.executeUpdate();
            LOG.info("Statement for QuestionnaireQuestion Update succesfully sent.");
        } catch (SQLException e) {
            LOG.error("QuestionnaireQuestion UPDATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public List<QuestionnaireQuestion> readAll() throws PersistenceException {
        //TODO see if there is any need for an readALl method or similar methods to it (readlAll,get,Search)
        return null;
    }
}