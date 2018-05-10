package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import com.google.inject.internal.UniqueAnnotations;
import com.sun.jdi.IntegerValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.ArrayList;

@Service
public class SimpleQuestionService implements IQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private QuestionDAO questionDAO;

    public SimpleQuestionService(QuestionDAO questionDAO){
        this.questionDAO = questionDAO;
    }

    public SimpleQuestionService(){
        try {
            this.QuestionDAO(new QuestionDAO());
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    private void QuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    @Override
    public void create(Question question) throws ServiceException {
        try {
            questionDAO.create(question);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Question question) throws ServiceException {
        try {
            questionDAO.update(question);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void search(Question question) throws ServiceException {
        try {
            questionDAO.search(question);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Question question) throws ServiceException {
        try {
            questionDAO.delete(question);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Question get(long id) throws ServiceException {

        Question question = null;

        try {
            question = questionDAO.get(id);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return question;
    }

    @Override
    public ArrayList<String> getAllAnswers(Question question){

        ArrayList<String> allAnswers = new ArrayList<>();

        allAnswers.add(question.getAnswer1());
        allAnswers.add(question.getAnswer2());
        allAnswers.add(question.getAnswer3());
        allAnswers.add(question.getAnswer4());
        allAnswers.add(question.getAnswer5());

        return allAnswers;
    }

    @Override
    public void validate(Question question) throws ServiceException {

        ArrayList<String> allAnswers = getAllAnswers(question);

        // -------------------------------------------------------------------------------------------------------------
        // validate question
        // -------------------------------------------------------------------------------------------------------------

        if (question.getQuestionText().equals("")){
            throw new ServiceException("The Question has no question text");
        }

        if (question.getQuestionText().length() > 200){
            throw new ServiceException("The Question is too long and cannot be displayed in the user interface");
        }

        // -------------------------------------------------------------------------------------------------------------
        // validate answers
        // -------------------------------------------------------------------------------------------------------------

        // at least 2 answers present

        int numberOfAnswersPresent = 0;

        // check for every possible answer check if text is present
        for (var i = 0; i < allAnswers.size(); i++){

            // count the number of answers that are present
            if ( ! allAnswers.get(i).equals("") ) {
                numberOfAnswersPresent++;
            }
        }

        if (numberOfAnswersPresent < 2) {
            throw new ServiceException("There have to be at least 2 answers present");
        }

        // answers not too long
        for (var i = 0; i < allAnswers.size(); i++) {

            // answer is not "" and longer than 200 chars
            if (( ! allAnswers.get(i).equals("") ) && ( allAnswers.get(i).length() > 200 )) {
                throw new ServiceException("The Answer is too long and cannot be displayed in the user interface");
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        // validate correct answers
        // -------------------------------------------------------------------------------------------------------------

        String correctAnswers = question.getCorrectAnswers();
        var currentCorrectAnswerIndex = 0;

        // go through the correct answers string one char at a time and check if the value is valid

        for (var i = 0; i < correctAnswers.length(); i++){

            currentCorrectAnswerIndex = Character.getNumericValue(correctAnswers.charAt(i));

            // index is 0
            if (currentCorrectAnswerIndex == 0){
                throw new ServiceException("The Answers are numbered starting with 1 and not 0");
            }

            // index is to high
            if (currentCorrectAnswerIndex > 5){
                // TODO - better error text
                throw new ServiceException("There can only be 5 correct answers. However a number with a higher index has been maked as correct");
            }

            // check if the corresponding answer does in fact exist

            // the ArrayList index starts at 0. the answers however start with 1.
            // so we have to decrement the index that we get from the correct answer string so we can
            // access the ArrayList.
            if (allAnswers.get(currentCorrectAnswerIndex - 1).equals("")){
                throw new ServiceException("A Answer marked as correct does not exist");
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        // validate image
        // -------------------------------------------------------------------------------------------------------------

        // TODO - validate image

    }
}
