package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.LearningQuestionnaire;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class LearningQuestionnaireDAO implements ILearningQuestionnaireDAO{

    private static final String SQL_LEARNINGQUESTIONNAIRE_CREATE_STATEMENT = "";
    private static final String SQL_LEARNINGQUESTIONNAIRE_UPDATE_STATEMENT = "";
    private Connection connection = null;
    private QuestionnaireDAO questionaireDAO;

    @Autowired
    public LearningQuestionnaireDAO() throws PersistenceException {
        try {
            questionaireDAO =new QuestionnaireDAO();
            connection = JDBCConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void create(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {
        questionaireDAO.create(learningQuestionnaire);
        //TODO after questionaireDAO create for the learningquestionaire
    }

    @Override
    public void update(LearningQuestionnaire learningQuestionnaire) throws PersistenceException {

    }
}