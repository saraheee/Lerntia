package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.*;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.lang.invoke.MethodHandles;

@Service
public class SimpleLerntiaService implements LerntiaService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int SLEEP_SECONDS = 2;
    private CourseDAO courseDAO;
    private UserDAO userDAO;
    private QuestionnaireDAO questionnaireDAO;
    private ExamQuestionaireDAO examQuestionaireDAO;
    private LearningQuestionnaireDAO learningQuestionnaireDAO;
    private QuestionDAO questionDAO;
    private QuestionnaireQuestionDAO questionnaireQuestionDAO;
    private UserCourseDAO userCourseDAO;
    private UserQuestionaireDAO userQuestionaireDAO;

    @Autowired
    public SimpleLerntiaService(CourseDAO courseDAO, UserDAO userDAO, QuestionnaireDAO questionnaireDAO,ExamQuestionaireDAO examQuestionaireDAO,LearningQuestionnaireDAO learningQuestionnaireDAO,QuestionDAO questionDAO,QuestionnaireQuestionDAO questionnaireQuestionDAO,UserCourseDAO userCourseDAO,UserQuestionaireDAO userQuestionaireDAO){
         this.courseDAO = courseDAO;
         this.userDAO = userDAO;
         this.questionnaireDAO = questionnaireDAO;
         this.examQuestionaireDAO = examQuestionaireDAO;
         this.learningQuestionnaireDAO =learningQuestionnaireDAO;
         this.questionDAO = questionDAO;
         this.questionnaireQuestionDAO = questionnaireQuestionDAO;
         this.userCourseDAO = userCourseDAO;
         this.userQuestionaireDAO = userQuestionaireDAO;

    }

    private Question getQuestion() throws ServiceException {
        try {
            Question question = questionDAO.get(1);
            return question;
            //TODO better implementation after more related stories regarding choosing of question are done.
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }



}
