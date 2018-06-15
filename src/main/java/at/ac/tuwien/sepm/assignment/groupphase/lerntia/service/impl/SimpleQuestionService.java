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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleQuestionService implements IQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private QuestionDAO questionDAO;

    private ConfigReader configReaderQuestions = new ConfigReader("questions");

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
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustommessage());
        }
    }

    @Override
    public void update(Question question) throws ServiceException {
        try {
            validate(question);
            questionDAO.update(question);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustommessage());
        }
    }

    @Override
    public List<Question> search(List<Question> questionList) throws ServiceException {
        try {
            return questionDAO.search(questionList);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustommessage());
        }
    }

    @Override
    public void delete(Question question) throws ServiceException {
        try {
            questionDAO.delete(question);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustommessage());
        }
    }

    @Override
    public Question get(long id) throws ServiceException {
        try {
            return questionDAO.get(id);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustommessage());
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

        var maxLengthQuestion = configReaderQuestions.getValueInt("maxLengthQuestion");
        var maxLengthAnswer = configReaderQuestions.getValueInt("maxLengthAnswer");
        var maxHeightPicture = configReaderQuestions.getValueInt("maxHeightPicture");
        var maxWidthPicture = configReaderQuestions.getValueInt("maxWidthPicture");

        configReaderQuestions.close();

        ArrayList<String> allAnswers = getAllAnswers(question);

        // -------------------------------------------------------------------------------------------------------------
        // validate question
        // -------------------------------------------------------------------------------------------------------------

        boolean error = false;
        String message = "";

        if (question.getQuestionText().equals("")){
            error = true;
            message += "The Question has no question text\n";
        }

        if (question.getQuestionText().length() > maxLengthQuestion){
            error = true;
            message += "The Question is too long and cannot be displayed in the user interface\n";
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
            error = true;
            message += "There have to be at least 2 answers present\n";
        }

        // answers not too long
        for (var i = 0; i < allAnswers.size(); i++) {

            // answer is not "" and longer than 200 chars
            if (( ! allAnswers.get(i).equals("") ) && ( allAnswers.get(i).length() > maxLengthAnswer )) {
                error = true;
                message += "Answer "  + i + " is too long and cannot be displayed in the user interface\n";
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        // validate correct answers
        // -------------------------------------------------------------------------------------------------------------

        String correctAnswers = question.getCorrectAnswers();
        var currentCorrectAnswerIndex = 0;

        //check if the correct answers can be parsed to an integer
        if(!isInteger(correctAnswers)) {
            error = true;
            message += "The Answers contain invalid characters.\n";
        }

        // go through the correct answers string one char at a time and check if the value is valid

        for (var i = 0; i < correctAnswers.length(); i++){

            currentCorrectAnswerIndex = Character.getNumericValue(correctAnswers.charAt(i));

            // index is 0
            if (currentCorrectAnswerIndex == 0){
                error = true;
                message += "The Answers are numbered starting with 1 and not 0\n";
            }

            // index is to high
            if (currentCorrectAnswerIndex > 5){
                error = true;
                // TODO - better error text
                message += "There can only be 5 correct answers. However a number with a higher index has been marked as correct\n";
            }

            // check if the corresponding answer does in fact exist

            // the ArrayList index starts at 0. the answers however start with 1.
            // so we have to decrement the index that we get from the correct answer string so we can
            // access the ArrayList.
            if (allAnswers.get(currentCorrectAnswerIndex - 1).equals("")){
                error = true;
                message += "A Answer marked as correct does not exist\n";
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        // validate image
        // -------------------------------------------------------------------------------------------------------------

        // TODO - more image validation?
        FileInputStream input = null;
        try {
            if(question.getPicture() != null) {
                input = new FileInputStream(question.getPicture());
                Image image = new Image(input);

                if (image.getHeight() < maxHeightPicture) {
                    error = true;
                    LOG.error("image has too small height");
                    message += "Das Bild muss mindestens 200x200 Pixel haben\n";
                }
                if (image.getWidth() < maxWidthPicture) {
                    error = true;
                    LOG.error("image has too small width");
                    message += "Das Bild muss mindestens 200x200 Pixel haben\n";
                }
            }
        } catch (FileNotFoundException e) {
            LOG.error("cannot find image");
        }
        if (error == true) {
            throw new ServiceException(message);
        }
    }

    @Override
    public List<Question> searchForQuestions(Question questionInput) throws ServiceException {
            try {
                return questionDAO.searchForQuestions(questionInput);
            } catch (PersistenceException e) {
                LOG.warn("Persistence exception caught");
                throw new ServiceException(e.getCustommessage());
            }
    }

    private static boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

}
