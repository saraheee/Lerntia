package at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.ExamQuestionnaire;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;

@Component
public class ExamQuestionaireDAO implements IExamQuestionnaireDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SQL_EXAMQUESTIONAIRE_CREATE_STATEMENT="";
    private static final String SQL_EXAMQUESTIONAIRE_UPDATE_STATEMENT="";
    private static final String SQL_EXAMQUESTIONAIRE_SEARCH_STATEMENT="";
    private static final String SQL_EXAMQUESTIONAIRE_DELETE_STATEMENT="";
    private static final String SQL_EXAMQUESTIONAIRE_READALL_STATEMENT="";


    @Override
    public void create(ExamQuestionnaire examQuestionnaire) throws PersistenceException {

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
