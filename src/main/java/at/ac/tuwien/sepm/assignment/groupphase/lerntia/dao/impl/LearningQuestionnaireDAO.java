package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ILearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
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
public class LearningQuestionnaireDAO implements ILearningQuestionnaireDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_LEARNINGQUESTIONNAIRE_CREATE_STATEMENT = "INSERT INTO LearningQuestionnaire(id,name) VALUES (?,?)";
    private static final String SQL_LEARNINGQUESTIONNAIRE_UPDATE_STATEMENT = "";
    private static final String SQL_LEARNINGQUESTIONNAIRE_READALL_STATEMENT = "SELECT * FROM LearningQuestionnaire WHERE id IN (SELECT id FROM Questionnaire WHERE isDeleted = false)";
    private static final String SQL_LEARNINGQUESTIONNAIRE_SELECT_STATEMENT = "UPDATE LearningQuestionnaire SET selected = true where id = ?";
    private static final String SQL_LEARNINGQUESTIONNAIRE_DESELECT_STATEMENT = "UPDATE LearningQuestionnaire SET selected = false where id = ?";
    private static final String SQL_LEARNINGQUESTIONNAIRE_GETSELECTED_STATEMENT = "SELECT * FROM LearningQuestionnaire WHERE selected = true";

    private Connection connection;
    private QuestionnaireDAO questionaireDAO;

    @Autowired
    public LearningQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) throws PersistenceException {
        try {
            this.questionaireDAO = questionnaireDAO;
            connection = JDBCConnectionManager.getConnection();
            LOG.info("Connection succesfully found for LearningQuestionnaireDAO.");
        } catch (PersistenceException e) {
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
    public List<LearningQuestionnaire> readAll() throws PersistenceException {
        try {
            LOG.info("Prepare Statement to read all LearingQuestionnaires from the Database.");
            ArrayList<LearningQuestionnaire> list = new ArrayList<>();
            ResultSet rsreadall = connection.prepareStatement(SQL_LEARNINGQUESTIONNAIRE_READALL_STATEMENT).executeQuery();
            LearningQuestionnaire learning;
            while (rsreadall.next()){
                learning = new LearningQuestionnaire();
                learning.setId(rsreadall.getLong(1));
                learning.setName(rsreadall.getString(2));
                list.add(learning);
            }
            LOG.info("All LearningQuestionnaires found.");
            return list;
        } catch (SQLException e) {
            LOG.error("LearningQuestionnaire DAO READALL error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void select(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {

        try {
            LOG.info("Prepare statement for learningquestionnaire selectione.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_LEARNINGQUESTIONNAIRE_SELECT_STATEMENT);
            psupdate.setLong(1,learningQuestionnaire.getId());
            psupdate.executeUpdate();
            LOG.info("Learningquestionnaire succesfully selected in Database.");
        } catch (SQLException e) {
            LOG.error("Learningquestionnaire selection DAO error!");
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public void deselect(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {
        try {
            LOG.info("Prepare statement for learningquestionnaire selectione.");
            PreparedStatement psupdate = connection.prepareStatement(SQL_LEARNINGQUESTIONNAIRE_DESELECT_STATEMENT);
            psupdate.setLong(1,learningQuestionnaire.getId());
            psupdate.executeUpdate();
            LOG.info("Learningquestionnaire succesfully selected in Database.");
        } catch (SQLException e) {
            LOG.error("Learningquestionnaire selection DAO error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public LearningQuestionnaire getSelected() throws PersistenceException {

        try {
            LOG.info("Prepare Statement to read all LearingQuestionnaires from the Database.");
            ArrayList<LearningQuestionnaire> list = new ArrayList<>();
            ResultSet rsreadall = connection.prepareStatement(SQL_LEARNINGQUESTIONNAIRE_GETSELECTED_STATEMENT).executeQuery();
            LearningQuestionnaire learning;

            while (rsreadall.next()){
                learning = new LearningQuestionnaire();
                learning.setId(rsreadall.getLong(1));
                learning.setName(rsreadall.getString(2));
                list.add(learning);
            }
            LOG.info("All LearningQuestionnaires found.");

            System.out.println("===================");
            System.out.println(list.size());
            System.out.println(list);

            if (list.isEmpty()) {
                return null;
            }
            return list.get(0);

        } catch (SQLException e) {
            LOG.error("LearningQuestionnaire DAO READALL error!");
            throw new PersistenceException(e.getMessage());
        }

    }

}