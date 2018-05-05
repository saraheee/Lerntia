package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class LearningQuestionnaireDAO implements ILearningQuestionnaireDAO{

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_LEARNINGQUESTIONNAIRE_CREATE_STATEMENT = "INSERT INTO LearningQuestionnaire(id,name) VALUES (?,?)";
    private static final String SQL_LEARNINGQUESTIONNAIRE_UPDATE_STATEMENT = "";
    private Connection connection;
    private QuestionnaireDAO questionaireDAO;

    @Autowired
    public LearningQuestionnaireDAO() throws PersistenceException {
        try {
            questionaireDAO =new QuestionnaireDAO();
            connection = JDBCConnectionManager.getConnection();
            LOG.info("Connection succesfully found for LearningQuestionnaireDAO.");
        } catch (SQLException e) {
            LOG.error("Connection couldn't be found for LearningQuestionnaireDAO!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void create(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {
        try {
            LOG.info("Create preparation for ExamQuestionnaire and Questionnaire.");
            questionaireDAO.create(learningQuestionnaire);
            LOG.info("Entry for general Questionnaire succesfull.");
            LOG.info("Prepare Statement for LearningQuestionnaire...");
            PreparedStatement pscreate = connection.prepareStatement(SQL_LEARNINGQUESTIONNAIRE_CREATE_STATEMENT);
            pscreate.setLong(1,learningQuestionnaire.getId());
            pscreate.setString(2,learningQuestionnaire.getName());
            pscreate.executeUpdate();
            LOG.info("Statement for LearningQuestionnaire succesfully sent.");
        } catch (SQLException e) {
            LOG.error("LearningQuestionnaire CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }
        //TODO after questionaireDAO create for the learningquestionaire
    }

    @Override
    public void update(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {

    }

    @Override
    public void search(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {

    }

    @Override
    public void delete(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {

    }

    @Override
    public List readAll() throws PersistenceException {
        return null;
    }
}