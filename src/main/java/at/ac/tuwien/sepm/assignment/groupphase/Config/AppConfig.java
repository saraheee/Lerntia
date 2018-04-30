package at.ac.tuwien.sepm.assignment.groupphase.Config;


import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.ExamQuestionaireDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionaireDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ExamQuestionaireDAO examQuestionaireDAOJDBC(){
        return new ExamQuestionaireDAO();
    }

    @Bean
    public QuestionaireDAO questionaireDAOJDBC(){
        return new QuestionaireDAO();
    }
}
