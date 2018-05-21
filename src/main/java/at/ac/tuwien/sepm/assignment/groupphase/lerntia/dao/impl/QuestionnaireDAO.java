package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Course;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionnaireDAO implements IQuestionnaireDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_QUESTIONAIRE_CREATE_STATEMENT = "INSERT INTO Questionnaire(courseid,id,name) VALUES (?,default,?)";
    private static final String SQL_QUESTIONAIRE_UPDATE_STATEMENT = "";
    private static final String SQL_QUESTIONAIRE_SEARCH_STATEMENT = "";
    private static final String SQL_QUESTIONAIRE_DELETE_STATEMENT = "";
    private static final String SQL_QUESTIONAIRE_READALL_STATEMENT = "SELECT * FROM Questionnaire WHERE isDeleted = false";
    private static final String SQL_QUESTIONAIRE_GETNAME_STATEMENT = "SELECT name FROM Questionnaire WHERE id = ?";

    private static final String SQL_QUESTIONNAIRE_SELECT_STATEMENT = "UPDATE Questionnaire SET selected = true where id = ?";
    private static final String SQL_QUESTIONNAIRE_DESELECT_STATEMENT = "UPDATE Questionnaire SET selected = false where id = ?";
    private static final String SQL_QUESTIONNAIRE_GETSELECTED_STATEMENT = "SELECT * FROM Questionnaire WHERE selected = true";

    private Connection connection = null;

    @Autowired
    public QuestionnaireDAO(JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            connection = jdbcConnectionManager.getConnection();
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

            PreparedStatement pscreate = connection.prepareStatement(SQL_QUESTIONAIRE_CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            try {
                pscreate.setLong(1, questionnaire.getCourseID());
                pscreate.setString(2,questionnaire.getName());
                pscreate.executeUpdate();

                LOG.info("Statement succesfully sent for Questionnaire creation.");

                ResultSet generatedKeys = pscreate.getGeneratedKeys();
                try {
                    generatedKeys.next();
                    questionnaire.setId(generatedKeys.getLong(1));
                }finally {
                    generatedKeys.close();
                }
            }finally {
                pscreate.close();
            }
        } catch (SQLException e) {
            LOG.error("Questionnaire CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(Questionnaire questionnaire) throws PersistenceException {
        //method not yet implemented because feature regarding that method hasn't been started implementing yet
    }

    @Override
    public String getQuestionnaireName(Long id) throws PersistenceException {

        try {
            LOG.info("Prepare Statement to get Questionnaire Name from the Database.");

            PreparedStatement pscreate = connection.prepareStatement(SQL_QUESTIONAIRE_GETNAME_STATEMENT);
            pscreate.setLong(1,id);
            ResultSet rsreadall = pscreate.executeQuery();

            if (rsreadall.next()){
                return rsreadall.getString(1);
            } else {
                return null;
            }

        } catch (SQLException e) {
            LOG.error("Questionnaire DAO getName error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void select(Questionnaire questionnaire) throws PersistenceException {

        try {
            LOG.info("Prepare statement for learningquestionnaire selectione.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_QUESTIONNAIRE_SELECT_STATEMENT);
            psupdate.setLong(1,questionnaire.getId());
            psupdate.executeUpdate();
            LOG.info("Learningquestionnaire succesfully selected in Database.");
        } catch (SQLException e) {
            LOG.error("Learningquestionnaire selection DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void deselect(Questionnaire questionnaire) throws PersistenceException {
        try {
            LOG.info("Prepare statement for learningquestionnaire selectione.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_QUESTIONNAIRE_DESELECT_STATEMENT);
            psupdate.setLong(1,questionnaire.getId());
            psupdate.executeUpdate();
            LOG.info("Learningquestionnaire succesfully selected in Database.");
        } catch (SQLException e) {
            LOG.error("Learningquestionnaire selection DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public Questionnaire getSelected() throws PersistenceException {

        try {
            LOG.info("Prepare Statement to get selected LearingQuestionnaire from the Database.");

            ResultSet rsreadall = connection.prepareStatement(SQL_QUESTIONNAIRE_GETSELECTED_STATEMENT).executeQuery();

            Questionnaire questionnaire;

            if (rsreadall.next()){
                questionnaire = new LearningQuestionnaire();

                questionnaire.setId(rsreadall.getLong(2));
                questionnaire.setName(rsreadall.getString(3));

                LOG.info("Selected Questionnaire found.");

                return questionnaire;
            } else {
                return null;
            }

        } catch (SQLException e) {
            LOG.error("Questionnaire DAO getSelected error!");
            throw new PersistenceException(e.getMessage());
        }
    }
}