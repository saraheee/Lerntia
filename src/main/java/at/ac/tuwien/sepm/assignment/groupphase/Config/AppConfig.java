package at.ac.tuwien.sepm.assignment.groupphase.Config;


import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleCourseService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleLearningQuestionnaireService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.SimpleQuestionnaireImportService;
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

    @Bean
    public SimpleCourseService SimpleCourseService(ICourseDAO courseDAO){
        return new SimpleCourseService(courseDAO);
    }

    @Bean
    public SimpleQuestionnaireImportService SimpleCourseService(QuestionnaireImportDAO questionnaireImportDAO, SimpleQuestionService simpleQuestionService, SimpleLearningQuestionnaireService simpleLearningQuestionnaireService){
        return new SimpleQuestionnaireImportService(questionnaireImportDAO, simpleQuestionService, simpleLearningQuestionnaireService);
    }

}
