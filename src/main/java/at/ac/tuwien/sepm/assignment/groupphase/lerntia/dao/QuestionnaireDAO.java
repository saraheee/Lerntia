package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class QuestionnaireDAO implements IQuestionnaireDAO {
    private static final String SQL_QUESTIONAIRE_CREATE_STATEMENT="INSERT INTO Questionaire(cmark, semester,id) VALUES (?,?,default)";
    private static final String SQL_QUESTIONAIRE_UPDATE_STATEMENT="";
    private static final String SQL_QUESTIONAIRE_SEARCH_STATEMENT="";
    private static final String SQL_QUESTIONAIRE_DELETE_STATEMENT="";
    private static final String SQL_QUESTIONAIRE_READALL_STATEMENT="";
    private Connection connection = null;

    public QuestionnaireDAO() throws PersistenceException {
        try {
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void create(Questionnaire questionnaire) throws PersistenceException {
        try {
            PreparedStatement pscreate = connection.prepareStatement(SQL_QUESTIONAIRE_CREATE_STATEMENT);
            pscreate.setString(1,questionnaire.getCmark());
            pscreate.setString(2,questionnaire.getSemester());
            pscreate.executeUpdate();
            ResultSet generatedKeys = pscreate.getGeneratedKeys();
            generatedKeys.next();
            questionnaire.setId(generatedKeys.getLong(3));
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void update(Questionnaire questionnaire) throws PersistenceException {

    }


}