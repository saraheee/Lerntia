package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.IExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
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
public class ExamQuestionnaireDAO implements IExamQuestionnaireDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_EXAMQUESTIONNAIRE_CREATE_STATEMENT = "INSERT INTO ExamQuestionnaire(id,qdate) VALUES (?,?)";
    private static final String SQL_EXAMQUESTIONNAIRE_READALL_STATEMENT = "SELECT * FROM ExamQuestionnaire WHERE id IN (SELECT id FROM Questionnaire WHERE isDeleted = FALSE)";
    private QuestionnaireDAO questionnaireDAO;
    private Connection connection;

    @Autowired
    public ExamQuestionnaireDAO(QuestionnaireDAO questionnaireDAO, JDBCConnectionManager jdbcConnectionManager) throws PersistenceException {
        try {
            this.questionnaireDAO = questionnaireDAO;
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for the ExamQuestionnaireDAO created.");
        } catch (PersistenceException e) {
            LOG.error("Connection for the ExamQuestionnaireDAO couldn't be created!");
            throw e;
        }
    }

    @Override
    public void create(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        try {
            LOG.info("Create preparation for ExamQuestionnaire and Questionnaire.");
            questionnaireDAO.create(examQuestionnaire);
            LOG.info("Entry for general Questionnaire successful.");
            Timestamp timestamp = Timestamp.valueOf(examQuestionnaire.getDate().atStartOfDay());
            LOG.info("Prepare Statement for ExamQuestionnaire...");
            PreparedStatement psCreate = connection.prepareStatement(SQL_EXAMQUESTIONNAIRE_CREATE_STATEMENT);
            try {
                psCreate.setLong(1, examQuestionnaire.getId());
                psCreate.setTimestamp(2, timestamp);
                psCreate.executeUpdate();
                LOG.info("Statement successfully sent.");
            } finally {
                psCreate.close();
            }
        } catch (SQLException e) {
            throw new PersistenceException("ExamQuestionnaireDAO CREATE error: ExamQuestionnaire couldn't be created, check if all mandatory values have been inserted or if connection to the Database is valid.");
        }
    }

    @Override
    public void update(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        //this method is currently empty because there is not yet a feature implemented which would use this method effectively
    }

    @Override
    public void search(ExamQuestionnaire searchparameters) throws PersistenceException {
        //this method is currently empty because there is not yet a feature implemented which would use this method effectively
    }

    @Override
    public void delete(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        //this method is currently empty because there is not yet a feature implemented which would use this method effectively
    }

    @Override
    public List<ExamQuestionnaire> readAll() throws PersistenceException {
        try {
            LOG.info("Prepare Statement to read all ExamQuestionnaires from the Database.");
            ArrayList<ExamQuestionnaire> list = new ArrayList<>();
            try (ResultSet rsReadAll = connection.prepareStatement(SQL_EXAMQUESTIONNAIRE_READALL_STATEMENT).executeQuery()) {
                ExamQuestionnaire exam;
                while (rsReadAll.next()) {
                    exam = new ExamQuestionnaire();
                    exam.setId(rsReadAll.getLong(1));
                    exam.setDate(rsReadAll.getDate(2).toLocalDate());
                    exam.setName(questionnaireDAO.getQuestionnaireName(rsReadAll.getLong(1)));
                    list.add(exam);
                }
            }
            LOG.info("All ExamQuestionnaire found.");
            return list;
        } catch (SQLException e) {
            throw new PersistenceException("ExamQuestionnaireDAO READALL error: readall method couldn't find all ExamQuestionnaires, check if connection to the Database is valid.");
        }
    }

    @Override
    public void select(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        LOG.info("Select the Questionnaire in question");
        questionnaireDAO.select(examQuestionnaire);
    }

    @Override
    public void deselect(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        LOG.info("Deselect the Questionnaire in Question");
        questionnaireDAO.deselect(examQuestionnaire);
    }

    @Override
    public ExamQuestionnaire getSelected() throws PersistenceException {
        LOG.info("Get selected Questionnaire from Database");
        return (ExamQuestionnaire) questionnaireDAO.getSelected();
    }
}
