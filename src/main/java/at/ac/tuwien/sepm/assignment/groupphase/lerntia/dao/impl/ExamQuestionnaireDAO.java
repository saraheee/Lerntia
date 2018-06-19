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
        this.questionnaireDAO = questionnaireDAO;
        if (jdbcConnectionManager.isTestConnection()) {
            connection = jdbcConnectionManager.getTestConnection();
            LOG.info("Test database connection for ExamQuestionnaireDAO retrieved.");
        } else {
            connection = jdbcConnectionManager.getConnection();
            LOG.info("Connection for ExamQuestionnaireDAO retrieved.");
        }
    }

    @Override
    public void create(ExamQuestionnaire examQuestionnaire) throws PersistenceException {
        if (examQuestionnaire == null) {
            throw new PersistenceException("Mindestens ein Wert des Prüfungsfragenbogens oder der Prüfungsfragenbogen selbst ist null!");
        }
        try {
            LOG.info("Create preparation for ExamQuestionnaire and Questionnaire.");
            questionnaireDAO.create(examQuestionnaire);
            LOG.info("Entry for general Questionnaire successful.");
            Timestamp timestamp = Timestamp.valueOf(examQuestionnaire.getDate().atStartOfDay());
            LOG.info("Prepare Statement for ExamQuestionnaire...");
            try (PreparedStatement psCreate = connection.prepareStatement(SQL_EXAMQUESTIONNAIRE_CREATE_STATEMENT)) {
                psCreate.setLong(1, examQuestionnaire.getId());
                psCreate.setTimestamp(2, timestamp);
                psCreate.executeUpdate();
                LOG.info("Statement successfully sent.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("ExamQuestionnaireDAO CREATE Fehler: Der Prüfungsfragenbogen konnte nicht erzeugt werden. Bitte überprüfen, ob alle notwendigen Felder ausgefüllt sind und ob die Datenbankverbindung gültig ist.");
        }
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
            throw new PersistenceException("ExamQuestionnaireDAO READALL Fehler: Nicht alle Prüfungsfragenbogen konnten gefunden werden. Bitte überprüfen, ob die Datenbankverbindung gültig ist.");
        }
    }


}
