package at.ac.tuwien.lerntia.lerntia.service.impl;

import at.ac.tuwien.lerntia.exception.ConfigReaderException;
import at.ac.tuwien.lerntia.exception.PersistenceException;
import at.ac.tuwien.lerntia.exception.ServiceException;
import at.ac.tuwien.lerntia.lerntia.dao.IQuestionDAO;
import at.ac.tuwien.lerntia.lerntia.dto.Question;
import at.ac.tuwien.lerntia.lerntia.service.IQuestionService;
import at.ac.tuwien.lerntia.util.ConfigReader;
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
public class QuestionService implements IQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final IQuestionDAO questionDAO;
    private int maxLengthQuestion = 200;
    private int maxLengthAnswer = 200;
    private int minHeightPicture = 200;
    private int minWidthPicture = 200;
    private ConfigReader configReaderQuestions = null;

    @Autowired
    public QuestionService(IQuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    private static boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public void create(Question question) throws ServiceException {
        try {
            validate(question);
            questionDAO.create(question);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public void update(Question question) throws ServiceException {
        try {
            validate(question);
            questionDAO.update(question);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public List<Question> search(List<Question> questionList) throws ServiceException {
        try {
            return questionDAO.search(questionList);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public void delete(Question question) throws ServiceException {
        try {
            questionDAO.delete(question);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public Question get(long id) throws ServiceException {
        try {
            return questionDAO.get(id);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }

    @Override
    public ArrayList<String> getAllAnswers(Question question) {

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
        if (configReaderQuestions == null) {
            try {
                configReaderQuestions = new ConfigReader("questions");
            } catch (ConfigReaderException e) {
                throw new ServiceException(e.getCustomMessage());
            }
        }
        maxLengthQuestion = configReaderQuestions.getValueInt("maxLengthQuestion") != 0 ? configReaderQuestions.getValueInt("maxLengthQuestion") : maxLengthQuestion;
        maxLengthAnswer = configReaderQuestions.getValueInt("maxLengthAnswer") != 0 ? configReaderQuestions.getValueInt("maxLengthAnswer") : maxLengthAnswer;
        minHeightPicture = configReaderQuestions.getValueInt("minHeightPicture") != 0 ? configReaderQuestions.getValueInt("minHeightPicture") : minHeightPicture;
        minWidthPicture = configReaderQuestions.getValueInt("minWidthPicture") != 0 ? configReaderQuestions.getValueInt("minWidthPicture") : minWidthPicture;

        try {
            configReaderQuestions.close();
        } catch (ConfigReaderException e) {
            throw new ServiceException(e.getCustomMessage());
        }

        ArrayList<String> allAnswers = getAllAnswers(question);

        // -------------------------------------------------------------------------------------------------------------
        // validate question
        // -------------------------------------------------------------------------------------------------------------

        boolean error = false;
        StringBuilder message = new StringBuilder();

        if (question.getQuestionText().trim().equals("")) {
            error = true;
            message.append("In der ").append(question.getIndex()).append(".Frage von oben wurde kein Fragetext angegeben!\n");
        }

        if (question.getQuestionText().length() > maxLengthQuestion) {
            error = true;
            message.append("Die ").append(question.getIndex()).append(".Fragestellung von oben ist zu lang und kann daher nicht angezeigt werden!\n");
        }

        // -------------------------------------------------------------------------------------------------------------
        // validate answers
        // -------------------------------------------------------------------------------------------------------------

        // at least 2 answers present

        int numberOfAnswersPresent = 0;

        // check for every possible answer check if text is present
        for (String allAnswer : allAnswers) {
            // count the number of answers that are present
            if (allAnswer != null && !allAnswer.trim().equals("")) {
                numberOfAnswersPresent++;
            }
        }

        if (numberOfAnswersPresent < 2) {
            error = true;
            message.append("In der ").append(question.getIndex()).append(".Frage von oben wurden weniger als 2 Antwortmöglichkeiten angegeben. Es müssen mindestens 2 Antwortmöglickeiten verfügbar sein!\n");
        }

        // answers not too long
        for (int i = 0; i < allAnswers.size(); i++) {

            // answer is not "" and longer than the defined limit
            if ((allAnswers.get(i) != null && !allAnswers.get(i).equals("")) && (allAnswers.get(i).length() > maxLengthAnswer)) {
                error = true;
                message.append("In der ").append(question.getIndex()).append(".Frage von oben ist die ").append(i + 1).append(".Antwort zu lang und kann daher nicht angezeigt werden!\n");
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        // validate correct answers
        // -------------------------------------------------------------------------------------------------------------

        String correctAnswers = question.getCorrectAnswers();
        int currentCorrectAnswerIndex;

        //check if the correct answers can be parsed to an integer
        if (!isInteger(correctAnswers)) {
            error = true;
            message.append("In der ").append(question.getIndex()).append(".Frage von oben enthält die Eingabe der korrekten Antwortnummern ungültige Zeichen!\n");
            throw new ServiceException(message.toString());
        }

        // go through the correct answers string one char at a time and check if the value is valid

        for (int i = 0; i < correctAnswers.length(); i++) {

            currentCorrectAnswerIndex = Character.getNumericValue(correctAnswers.charAt(i));

            // index is 0
            if (currentCorrectAnswerIndex == 0) {
                error = true;
                message.append("In der ").append(question.getIndex()).append(".Frage von oben gibt es Antwortnummern, die die Zahl 0 beinhalten. Die korrekten Antwortnummern beginnen mit 1, nicht mit 0!\n");
                throw new ServiceException(message.toString());
            }

            // index is to high
            if (currentCorrectAnswerIndex > 5) {
                error = true;
                message.append("In der ").append(question.getIndex()).append(".Frage von oben wurde eine zu große Antwortnummer angegeben. Es kann nur bis zu 5 korrekte Antworten geben. Eine Antwortnummer größer als 5 ist daher nicht zulässig!\n");
                throw new ServiceException(message.toString());
            }

            // check if the corresponding answer does in fact exist

            // the ArrayList index starts at 0. the answers however start with 1.
            // so we have to decrement the index that we get from the correct answer string so we can
            // access the ArrayList.
            if (currentCorrectAnswerIndex != -1 && allAnswers.get(currentCorrectAnswerIndex - 1) == null || currentCorrectAnswerIndex != -1 && allAnswers.get(currentCorrectAnswerIndex - 1).equals("")) {
                error = true;
                message.append("In der ").append(question.getIndex()).append(".Frage von oben wurde eine Antwort als korrekt angegeben, die nicht existiert!\n");
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        // validate image
        // -------------------------------------------------------------------------------------------------------------

        FileInputStream input;
        try {
            if (question.getPicture() != null) {
                input = new FileInputStream(question.getPicture());
                Image image = new Image(input);

                if (image.getHeight() < minHeightPicture) {
                    error = true;
                    LOG.error("Image has too small height");
                    message.append("Das Bild muss mindestens 200x200 Pixel haben!\n");
                }
                if (image.getWidth() < minWidthPicture) {
                    error = true;
                    LOG.error("Image has too small width");
                    message.append("Das Bild muss mindestens 200x200 Pixel haben!\n");
                }
            }
        } catch (FileNotFoundException e) {
            LOG.info("No image available");
        }
        if (error) {
            throw new ServiceException(message.toString());
        }
    }

    @Override
    public List<Question> searchForQuestions(Question questionInput) throws ServiceException {
        try {
            return questionDAO.searchForQuestions(questionInput);
        } catch (PersistenceException e) {
            LOG.warn("Persistence exception caught");
            throw new ServiceException(e.getCustomMessage());
        }
    }


}
