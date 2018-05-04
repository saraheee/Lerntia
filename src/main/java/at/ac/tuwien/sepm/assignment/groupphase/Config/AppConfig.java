package at.ac.tuwien.sepm.assignment.groupphase.Config;


import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ExamQuestionaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.LearningQuestionnaireDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ExamQuestionaireDAO examQuestionaireDAOJDBC(){
        return new ExamQuestionaireDAO();
    }

    @Bean
    public LearningQuestionnaireDAO questionaireDAOJDBC() throws PersistenceException {
        return new LearningQuestionnaireDAO();
    }
}
