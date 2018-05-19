package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExamQuestionaireDAO implements IExamQuestionnaireDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_EXAMQUESTIONNAIRE_CREATE_STATEMENT="INSERT INTO ExamQuestionnaire(id,qdate) VALUES (?,?)";
    private static final String SQL_EXAMQUESTIONNAIRE_UPDATE_STATEMENT="";
    private static final String SQL_EXAMQUESTIONNAIRE_SEARCH_STATEMENT="";
    private static final String SQL_EXAMQUESTIONNAIRE_DELETE_STATEMENT="";
    private static final String SQL_EXAMQUESTIONNAIRE_READALL_STATEMENT = "SELECT * FROM ExamQuestionnaire WHERE id IN (SELECT id FROM Questionnaire WHERE isDeleted = false)";
    private QuestionnaireDAO questionaireDAO;
    private Connection connection;

    @Autowired
    public ExamQuestionaireDAO(QuestionnaireDAO questionnaireDAO, JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            this.questionaireDAO = questionnaireDAO;
            connection = jdbcConnectionManager.getConnection();
        } catch (PersistenceException e) {
            LOG.error("Connection for the ExamQuestionnaireDAO couldn't be created!");
            throw e;
        }
    }

    @Override
    public void create(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        try {
            LOG.info("Create preparation for ExamQuestionnaire and Questionnaire.");
            questionaireDAO.create(examQuestionnaire);
            LOG.info("Entry for general Questionnaire succesfull.");
            Timestamp timestamp = Timestamp.valueOf(examQuestionnaire.getDate().atStartOfDay());
            LOG.info("Prepare Statement for ExamQuestionnaire...");
            PreparedStatement pscreate = connection.prepareStatement(SQL_EXAMQUESTIONNAIRE_CREATE_STATEMENT);
            try {
                pscreate.setLong(1, examQuestionnaire.getId());
                pscreate.setTimestamp(2, timestamp);
                pscreate.executeUpdate();
                LOG.info("Statement succesfully sent.");
            }finally {
                pscreate.close();
            }
        } catch (SQLException e) {
            LOG.error("ExamQuestionnaire CREATE DAO error!");
            throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public void update(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
         //this method is currently empty because there is not yet a feature implemented which would use this method effectively
    }

    @Override
    public void search(ExamQuestionnaire searchparameters) throws PersistenceException{
        //this method is currently empty because there is not yet a feature implemented which would use this method effectively
    }

    @Override
    public void delete(ExamQuestionnaire examQuestionnaire) throws PersistenceException{
        //this method is currently empty because there is not yet a feature implemented which would use this method effectively
    }

    @Override
    public List<ExamQuestionnaire> readAll() throws PersistenceException{
        try {
            LOG.info("Prepare Statement to read all ExamQuestionnaires from the Database.");
            ArrayList<ExamQuestionnaire> list = new ArrayList<>();
            ResultSet rsreadall = connection.prepareStatement(SQL_EXAMQUESTIONNAIRE_READALL_STATEMENT).executeQuery();
            ExamQuestionnaire exam;

            while (rsreadall.next()){
                exam = new ExamQuestionnaire();
                exam.setId(rsreadall.getLong(1));
                exam.setDate(rsreadall.getDate(2).toLocalDate());
                exam.setName(questionaireDAO.getQuestionnaireName(rsreadall.getLong(1)));
                list.add(exam);
            }

            LOG.info("All ExamQuestionnaire found.");
            return list;
        } catch (SQLException e) {
            LOG.error("ExamQuestionnaire DAO READALL error!");
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void select(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        questionaireDAO.select(examQuestionnaire);
    }

    @Override
    public void deselect(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        questionaireDAO.deselect(examQuestionnaire);
    }

    @Override
    public ExamQuestionnaire getSelected() throws PersistenceException {
        return (ExamQuestionnaire) questionaireDAO.getSelected();
    }
}
