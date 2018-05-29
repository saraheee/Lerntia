package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearnAlgorithmDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.QuestionLearnAlgorithm;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.hsqldb.persist.Log;
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
public class LearnAlgorithmDAO implements ILearnAlgorithmDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_QUESTIONLEARNALGORITHM_CREATE_STATEMENT="INSERT INTO QUESTIONALGOVALUE(QUESTIONID, SUCCESSVALUE, FAILUREVALUE,POINTS) VALUES (?,0,0,100.0)";
    private static final String SQL_QUESTIONLEARNALGORITHM_UPDATE_STATEMENT="UPDATE QUESTIONALGOVALUE SET SUCCESSVALUE = ?, FAILUREVALUE = ?, POINTS = ? where QUESTIONID = ?";
    private static final String SQL_QUESTIONLEARNALGORITHM_DELETE_STATEMENT="DELETE FROM QUESTIONALGOVALUE WHERE QUESTIONID = ?";
    private static final String SQL_QUESTIONLEARNALGORITHM_RESET_STATEMENT="UPDATE QUESTIONALGOVALUE SET SUCCESSVALUE = 0.0, FAILUREVALUE = 0.0, POINTS = 100.0 where QUESTIONID = ?";
    private static final String SQL_QUESTIONLEARNALGORITHM_READALL_STATEMENT="SELECT * from QUESTIONALGOVALUE";
    private static final String SQL_QUESTIONLEARNALGORITHM_SEARCH_STATEMENT="SELECT * FROM QUESTIONALGOVALUE";
    private Connection connection;


    public LearnAlgorithmDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Created connection for LearnAlgorithmDAO");
        } catch (PersistenceException e) {
            LOG.error("Connection Database error for LearnAlgorithmDAO");
        }
    }
    @Override
    public void create(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException {
        try {
            LOG.info("Prepare Create Statement for QuestionLearnAlgorithm.");
            try (PreparedStatement pscreate = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_CREATE_STATEMENT)) {
                pscreate.setLong(1, questionLearnAlgorithm.getID());
                pscreate.executeUpdate();
                LOG.info("Create statement for QuestionLearnAlgorithm succesfully sent.");
            }
        }catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO CREATE error: Check if the connection to the Database is valid or if one or multiple mandatory value are missing");
        }
    }

    @Override
    public void update(List<QuestionLearnAlgorithm> questionLearnAlgorithmList) throws PersistenceException {
        try (PreparedStatement psupdate = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_UPDATE_STATEMENT)) {
            LOG.info("Create list of update statements for QuestionLearnAlgorithms");
            for (QuestionLearnAlgorithm questionLearnAlgorithm : questionLearnAlgorithmList) {
                 psupdate.setInt(1, questionLearnAlgorithm.getSuccessvalue());
                 psupdate.setInt(2, questionLearnAlgorithm.getFailurevalue());
                 psupdate.setDouble(3, questionLearnAlgorithm.getPoints());
                 psupdate.setLong(4, questionLearnAlgorithm.getID());
                 psupdate.executeUpdate();
            }
            LOG.info("All QuestionLearnAlgorithms have been succesfully updated");
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO UPDATE error: Check if the connection to the Database is valid or if one or multiple mandatory values are missing.");
        }
    }

    @Override
    public void delete(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException {
        try (PreparedStatement psdelete = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_DELETE_STATEMENT)) {
            LOG.info("Create Delete statement for QuestionLearnAlgorithm.");
            psdelete.setLong(1, questionLearnAlgorithm.getID());
            psdelete.executeUpdate();
            LOG.info("Delete Statement for QuestionLearnAlgorithm has been succesfully sent");
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO DELETE error: Check if the connection to the Database is valid or if false question ID has been given");
        }
    }

    @Override
    public List<QuestionLearnAlgorithm> readAll() throws PersistenceException {
        try {
            try (ResultSet rsreadall = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_READALL_STATEMENT).executeQuery()) {
                List<QuestionLearnAlgorithm> readResults = new ArrayList<>();
                QuestionLearnAlgorithm questionLearnAlgorithm;
                try {
                    while (rsreadall.next()) {
                        questionLearnAlgorithm = new QuestionLearnAlgorithm();
                        questionLearnAlgorithm.setID(rsreadall.getLong(1));
                        questionLearnAlgorithm.setSuccessvalue(rsreadall.getInt(2));
                        questionLearnAlgorithm.setFailurevalue(rsreadall.getInt(3));
                        questionLearnAlgorithm.setPoints(rsreadall.getDouble(4));
                        readResults.add(questionLearnAlgorithm);
                    }
                    LOG.info("Found all LearnAlgorithm Values.");
                    return readResults;
                } finally {
                    rsreadall.close();
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO READALL error: Check if the connection to the Database is valid or if the method actually returns the List of all Entries");
        }

    }

    @Override
    public List<QuestionLearnAlgorithm> search(List<QuestionLearnAlgorithm> questionAlgorithmList) throws PersistenceException{
        try {
            LOG.info("Create search Statement for questionlearnalgorithms,");
            QuestionLearnAlgorithm searchparameter;
            List<QuestionLearnAlgorithm> searchResults = new ArrayList<>();
            QuestionLearnAlgorithm found;
            String parameters="";
            while (!questionAlgorithmList.isEmpty()){
                searchparameter = questionAlgorithmList.get(0);
                parameters+=parameters.length()==0?" WHERE questionid= "+searchparameter.getID(): " OR questionid= "+searchparameter.getID();
                questionAlgorithmList.remove(0);
            }

            String searchStatement = SQL_QUESTIONLEARNALGORITHM_SEARCH_STATEMENT+parameters;
            try (ResultSet rssearch = connection.prepareStatement(searchStatement).executeQuery()) {
                try {
                    while (rssearch.next()) {
                        found = new QuestionLearnAlgorithm();
                        found.setID(rssearch.getLong(1));
                        found.setSuccessvalue(rssearch.getInt(2));
                        found.setFailurevalue(rssearch.getInt(3));
                        found.setPoints(rssearch.getDouble(4));
                        searchResults.add(found);
                    }
                LOG.info("All search results have been found.");
                return searchResults;
                } finally {
                    rssearch.close();
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO SEARCH DAO: Check if the connection to the Database is valid.");
        }
    }

    @Override
    public void reset(QuestionLearnAlgorithm questionLearnAlgorithm) throws PersistenceException {
        try (PreparedStatement psreset = connection.prepareStatement(SQL_QUESTIONLEARNALGORITHM_RESET_STATEMENT)) {
            LOG.info("Prepare reset statement for QuestionLearnAlgorithm.");
            psreset.setLong(1, questionLearnAlgorithm.getID());
            psreset.executeUpdate();
            LOG.info("Reset Statetement succesfully sent.");
        } catch (SQLException e) {
            throw new PersistenceException("LearnAlgorithmDAO UPDATE error: Check if the connection to the Database is valid or if one or multiple mandatory values are missing.");
        }
    }
}