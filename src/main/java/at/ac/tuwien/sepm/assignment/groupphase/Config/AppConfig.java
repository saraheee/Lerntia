package at.ac.tuwien.sepm.assignment.groupphase.Config;


import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ExamQuestionaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.LearningQuestionnaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionnaireImportDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleQuestionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ExamQuestionaireDAO examQuestionaireDAO() throws PersistenceException {
        return new ExamQuestionaireDAO();
    }

    @Bean
    public LearningQuestionnaireDAO learningQuestionnaireDAO() throws PersistenceException {
        return new LearningQuestionnaireDAO();
    }

    @Bean
    public QuestionnaireImportDAO questionnaireImportDAO(){
        return new QuestionnaireImportDAO();
    }

    @Bean
    public QuestionDAO questionDAO() throws PersistenceException {
        return new QuestionDAO();
    }


}
