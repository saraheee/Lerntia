package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireQuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionnaireQuestion;
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
public class QuestionnaireQuestionDAO implements IQuestionnaireQuestionDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_QUESTIONNAIREQUESTION_CREATE_STATEMENT ="INSERT INTO Questionnairequestion(qid,questionid,isDeleted) VALUES (?,?,?)";
    private static final String SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT = "UPDATE questionnairequestion set isDeleted=true where qid=? and questionid=?";
    private static final String SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT = "UPDATE questionnairequestion set qid=?, questionid=? where qid=? and questionid=?";
    private static final String SQL_QUESTIONNAIREQUESTION_SEARCH_STATEMENT = "SELECT * from Questionnairequestion where isDeleted = false and qid=";
    private Connection connection;

    @Autowired
    public QuestionnaireQuestionDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
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
            try {
                pscreate.setLong(1, questionnaireQuestion.getQid());
                pscreate.setLong(2, questionnaireQuestion.getQuestionid());
                pscreate.setBoolean(3, false);
                pscreate.execute();
                LOG.info("Statement for new QuestionnaireQuestion entry succesfully sent.");
            }finally {
                pscreate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO CREATE error: item couldn't have been created, check if all mandatory values have been added and if the connection to the Database is valid.");
        }
    }

    @Override
    public List<QuestionnaireQuestion> search(QuestionnaireQuestion searchparameters) throws PersistenceException{
        try {
            LOG.info("Prepare SEARCH QuestionnaireQuestion Statement. ");
            List<QuestionnaireQuestion> searchresults = new ArrayList<>();
            QuestionnaireQuestion questionnaireQuestion;
            String searchStatement= SQL_QUESTIONNAIREQUESTION_SEARCH_STATEMENT+searchparameters.getQid();
            try (ResultSet rs = connection.prepareStatement(searchStatement).executeQuery()) {
                    while (rs.next()) {
                        questionnaireQuestion = new QuestionnaireQuestion();
                        questionnaireQuestion.setQid(rs.getLong(1));
                        questionnaireQuestion.setQuestionid(rs.getLong(2));
                        questionnaireQuestion.setDeleted(rs.getBoolean(3));
                        searchresults.add(questionnaireQuestion);
                    }
                    LOG.info("All QuestionnaireQuestion matching the searchparameters found.");
                    return searchresults;
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO SEARCH error: couldn't find items, check if the searchparameters are valid and if the connection to the database is valid.");
        }
    }

    @Override
    public void delete(QuestionnaireQuestion questionnaireQuestion) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for QuestionnaireQuestion delete from Database.");
            PreparedStatement psdelete= connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_DELETE_STATEMENT);
            try {
                psdelete.setLong(1, questionnaireQuestion.getQid());
                psdelete.setLong(2, questionnaireQuestion.getQuestionid());
                psdelete.executeUpdate();
                LOG.info("Statement for QuestionnaireQuestion Deletion succesfully sent.");
            }finally {
                psdelete.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO DELETE error: couldn't delete item in question, check if the connection to the Database is valid.");
        }
    }

    @Override
    public void update(QuestionnaireQuestion questionnaireQuestion, long newQid,long newQuestionid) throws PersistenceException {
        try {
            LOG.info("Prepare Statement for updating existing QuestionnaireQuestion with new values.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_QUESTIONNAIREQUESTION_UPDATE_STATEMENT);
            try {
                psupdate.setLong(1, newQid);
                psupdate.setLong(2, newQuestionid);
                psupdate.setLong(3, questionnaireQuestion.getQid());
                psupdate.setLong(4, questionnaireQuestion.getQuestionid());
                psupdate.executeUpdate();
                LOG.info("Statement for QuestionnaireQuestion Update succesfully sent.");
            }finally {
                psupdate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("QuestionnaireQuestionDAO UPDATE error: item couldn't be updated, check if mandatory values have been added or if the connection to the Database is valid.");
        }
    }

    @Override
    public List<QuestionnaireQuestion> readAll() throws PersistenceException {
        //currently no implementation of this method due to the indiciciveness of if it is necessary to have such a method. currently left empty
        return null;
    }
}