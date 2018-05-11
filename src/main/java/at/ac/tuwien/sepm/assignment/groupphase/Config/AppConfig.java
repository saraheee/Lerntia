package at.ac.tuwien.sepm.assignment.groupphase.Config;


import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    QuestionnaireDAO questionanireDAO() throws PersistenceException {
        return new QuestionnaireDAO();
    }

    @Bean
    public ExamQuestionaireDAO examQuestionaireDAO() throws PersistenceException {
        return new ExamQuestionaireDAO(questionanireDAO());
    }

    @Bean
    public LearningQuestionnaireDAO learningQuestionnaireDAO() throws PersistenceException {
        return new LearningQuestionnaireDAO(questionanireDAO());
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
