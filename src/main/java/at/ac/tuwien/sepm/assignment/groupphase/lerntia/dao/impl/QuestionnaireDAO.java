package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.*;

@Component
public class QuestionnaireDAO implements IQuestionnaireDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_QUESTIONAIRE_CREATE_STATEMENT = "INSERT INTO Questionnaire(cmark, semester,id) VALUES (?,?,default)";
    private static final String SQL_QUESTIONAIRE_UPDATE_STATEMENT = "";
    private static final String SQL_QUESTIONAIRE_SEARCH_STATEMENT = "";
    private static final String SQL_QUESTIONAIRE_DELETE_STATEMENT = "";
    private static final String SQL_QUESTIONAIRE_READALL_STATEMENT = "";
    private Connection connection = null;

    public QuestionnaireDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
            LOG.info("Succesfully found connection for QuestionnaireDAO!");
        } catch (PersistenceException e) {
            LOG.error("Couldn't find connection for QuestionnaireDAO!");
            throw e;
        }
    }

    @Override
    public void create(Questionnaire questionnaire) throws PersistenceException {
        try {

            LOG.info("Prepare Statement for Questionnaire creation");

            System.out.println(questionnaire.getCmark());
            System.out.println(questionnaire.getSemester());

            PreparedStatement pscreate = connection.prepareStatement(SQL_QUESTIONAIRE_CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            pscreate.setString(1, questionnaire.getCmark());
            pscreate.setString(2, questionnaire.getSemester());
            pscreate.executeUpdate();

            LOG.info("Statement succesfully sent for Questionnaire creation.");

            ResultSet generatedKeys = pscreate.getGeneratedKeys();
            generatedKeys.next();
            questionnaire.setId(generatedKeys.getLong(1));
            
        } catch (SQLException e) {
            LOG.error("Questionnaire CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(Questionnaire questionnaire) throws PersistenceException {

    }


}