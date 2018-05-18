package at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl;

import at.ac.tuwien.sepm.assignment.groupphase.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dao.impl.QuestionDAO;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Question;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IQuestionService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleQuestionService implements IQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private QuestionDAO questionDAO;


    @Autowired
    public SimpleQuestionService(QuestionDAO questionDAO){
            this.questionDAO = questionDAO;
    }

    @Override
    public void create(Question question) throws ServiceException {
        try {
            validate(question);
            questionDAO.create(question);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(Question question) throws ServiceException {
        try {
            validate(question);
            questionDAO.update(question);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Question> search(List<Question> questionList) throws ServiceException {
        try {
            return questionDAO.search(questionList);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(Question question) throws ServiceException {
        try {
            questionDAO.delete(question);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Question get(long id) throws ServiceException {
        try {
            return questionDAO.get(id);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught " + e.getLocalizedMessage());
            throw new ServiceException(e.getMessage());
        }
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

        ConfigReader configReaderQuestions = new ConfigReader("questions");

        var maxLengthQuestion = configReaderQuestions.getValueInt("maxLengthQuestion");
        var maxLengthAnswer = configReaderQuestions.getValueInt("maxLengthAnswer");
        var maxHeightPicture = configReaderQuestions.getValueInt("maxHeightPicture");
        var maxWidthPicture = configReaderQuestions.getValueInt("maxWidthPicture");

        configReaderQuestions.close();

        ArrayList<String> allAnswers = getAllAnswers(question);

        // -------------------------------------------------------------------------------------------------------------
        // validate question
        // -------------------------------------------------------------------------------------------------------------

        if (question.getQuestionText().equals("")){
            throw new ServiceException("The Question has no question text");
        }

        if (question.getQuestionText().length() > maxLengthQuestion){
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
            if (( ! allAnswers.get(i).equals("") ) && ( allAnswers.get(i).length() > maxLengthAnswer )) {
                throw new ServiceException("Answer "  + i + " is too long and cannot be displayed in the user interface");
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
                throw new ServiceException("There can only be 5 correct answers. However a number with a higher index has been marked as correct");
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

        // TODO - more image validation?
        FileInputStream input = null;
        try {
            input = new FileInputStream(question.getPicture());
            Image image = new Image(input);

            if (image.getHeight() < maxHeightPicture) {
                LOG.error("image has too small height");
                throw new ServiceException("Das Bild muss 200x200 Pixel haben");
            }
            if (image.getWidth() < maxWidthPicture) {
                LOG.error("image has too small width");
                throw new ServiceException("Das Bild muss 200x200 Pixel haben");
            }
        } catch (FileNotFoundException e) {
            LOG.error("cannot find image");
        }
    }
}
