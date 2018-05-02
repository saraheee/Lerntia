package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Questionnaire;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;


@Component
public class LearningLearningQuestionaireDAO implements ILearningQuestionaireDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SQL_QUESTIONAIRE_CREATE_STATEMENT="INSERT INTO Questionaire(cmark, semester) VALUES (?,?)";
    private static final String SQL_QUESTIONAIRE_UPDATE_STATEMENT="";
    private static final String SQL_QUESTIONAIRE_SEARCH_STATEMENT="";
    private static final String SQL_QUESTIONAIRE_DELETE_STATEMENT="";
    private static final String SQL_QUESTIONAIRE_READALL_STATEMENT="";


    @Override
    public void create(Questionnaire questionnaire) throws PersistenceException {

    }

    @Override
    public void update(Questionnaire questionnaire) throws PersistenceException {

    }

    @Override
    public void search(Questionnaire questionnaire) throws PersistenceException {

    }

    @Override
    public void delete(Questionnaire questionnaire) throws PersistenceException {

    }

    @Override
    public ObservableList readAll() throws PersistenceException{
        return null;
    }
}
