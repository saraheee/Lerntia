package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Classes;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Interfaces.IExamQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.Interfaces.IQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class ExamQuestionaireDAO implements IExamQuestionnaireDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_EXAMQUESTIONAIRE_CREATE_STATEMENT="INSERT INTO ExamQuestionnaire(id,qdate) VALUES (?,?)";
    private static final String SQL_EXAMQUESTIONAIRE_UPDATE_STATEMENT="";
    private static final String SQL_EXAMQUESTIONAIRE_SEARCH_STATEMENT="";
    private static final String SQL_EXAMQUESTIONAIRE_DELETE_STATEMENT="";
    private static final String SQL_EXAMQUESTIONAIRE_READALL_STATEMENT="";
    private IQuestionnaireDAO questionaireDAO;
    private Connection connection;

    @Autowired
    public ExamQuestionaireDAO(QuestionnaireDAO questionnaireDAO) throws PersistenceException {
        try {
            this.questionaireDAO =questionnaireDAO;
            connection = JDBCConnectionManager.getConnection();
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
            PreparedStatement pscreate = connection.prepareStatement(SQL_EXAMQUESTIONAIRE_CREATE_STATEMENT);
            pscreate.setLong(1,examQuestionnaire.getId());
            pscreate.setTimestamp(2,timestamp);
            pscreate.executeUpdate();
            LOG.info("Statement succesfully sent.");
        } catch (SQLException e) {
            LOG.error("ExamQuestionnaire CREATE DAO error!");
           throw new PersistenceException(e.getMessage());
        }

    }

    @Override
    public void update(ExamQuestionnaire examQuestionnaire) throws PersistenceException {

    }

    @Override
    public void search(ExamQuestionnaire searchparameters) throws PersistenceException{

    }



    @Override
    public void delete(ExamQuestionnaire examQuestionnaire) throws PersistenceException{}

    @Override
    public ObservableList readAll() throws PersistenceException{
        return null;
    }
}
