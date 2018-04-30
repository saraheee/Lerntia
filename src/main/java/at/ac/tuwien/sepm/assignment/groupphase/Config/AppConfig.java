package at.ac.tuwien.sepm.assignment.groupphase.Config;


import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ExamQuestionaireDAOJDBC;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionaireDAOJDBC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ExamQuestionaireDAOJDBC examQuestionaireDAOJDBC(){
        return new ExamQuestionaireDAOJDBC();
    }

    @Bean
    public QuestionaireDAOJDBC questionaireDAOJDBC(){
        return new QuestionaireDAOJDBC();
    }
}
