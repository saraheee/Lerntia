package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceValidationException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Speech;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ITextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleTextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


public class TextToSpeechServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ITextToSpeechService textToSpeechService;

    @Before
    public void setUp() {
        ITextToSpeechService(new SimpleTextToSpeechService());
    }

    private void ITextToSpeechService(SimpleTextToSpeechService simpleTextToSpeechService) {
        this.textToSpeechService = simpleTextToSpeechService;
    }


    @Test
    public void readCorrectQuestionAndAnswersShouldPersist() throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        var speech = new Speech();
        speech.setQuestion("Bin ich eine Frage?");
        speech.setAnswer1("Ja, bist du!");
        speech.setAnswer2("Ich bin eine Antwort!");
        textToSpeechService.readQuestionAndAnswers(speech);
    }


    @Test(expected = TextToSpeechServiceValidationException.class)
    public void readQuestionAndAnswersWithEmptyValuesShouldFail() throws TextToSpeechServiceValidationException, TextToSpeechServiceException {
        var speech = new Speech();
        speech.setQuestion("");
        speech.setAnswer1("");
        speech.setAnswer2("");
        textToSpeechService.readQuestionAndAnswers(speech);
    }

    @Test(expected = TextToSpeechServiceValidationException.class)
    public void readQuestionAndAnswersWithNullValuesShouldFail() throws TextToSpeechServiceValidationException, TextToSpeechServiceException {
        var speech = new Speech();
        speech.setQuestion(null);
        speech.setAnswer1(null);
        speech.setAnswer2("");
        textToSpeechService.readQuestionAndAnswers(speech);
    }

    @Test
    public void readSingleAnswerShouldPersist() throws TextToSpeechServiceValidationException, TextToSpeechServiceException {
        var speech = new Speech();
        speech.setSingleAnswer("Schön");
        textToSpeechService.readSingleAnswer(speech);
    }

    @Test
    public void filterTextInParenthesisShouldPersist() throws TextToSpeechServiceException {
        var service = new SimpleTextToSpeechService();
        Assert.assertEquals(service.filterTextInParenthesis("Hallo (a)b"), "Hallo b");
    }

    @Test
    public void filterTextInNestedParenthesisShouldPersist() throws TextToSpeechServiceException {
        var service = new SimpleTextToSpeechService();
        Assert.assertEquals(service.filterTextInParenthesis("((Hallo (a)b) c)i"), "i");
    }

    @Test
    public void getTextShouldPersist() throws TextToSpeechServiceValidationException {
        var configReaderSpeech = new ConfigReader("speech");
        final var BREAK = configReaderSpeech.getValue("break");
        final var ANSWER = configReaderSpeech.getValue("answerPrefix");
        var service = new SimpleTextToSpeechService();
        var speech = new Speech();

        var question = "Wann ist das Semester vorbei?";
        var antwort1 = "Bald.";
        var antwort2 = "Sehr bald.";
        var antwort3 = "Kurz bevor die Sommerferien anfangen.";
        var antwort4 = "Am 29.Juni 2018 um 23:59.";
        var antwort5 = "Keine der genannten Antworten.";

        speech.setQuestion(question);
        speech.setAnswer1(antwort1);
        speech.setAnswer2(antwort2);
        speech.setAnswer3(antwort3);
        speech.setAnswer4(antwort4);
        speech.setAnswer5(antwort5);

        Assert.assertEquals(service.getText(speech), question
            + BREAK + ANSWER + SimpleTextToSpeechService.answerNumber.eins + BREAK + antwort1 + '\n'
            + BREAK + ANSWER + SimpleTextToSpeechService.answerNumber.zwei + BREAK + antwort2 + '\n'
            + BREAK + ANSWER + SimpleTextToSpeechService.answerNumber.drei + BREAK + antwort3 + '\n'
            + BREAK + ANSWER + SimpleTextToSpeechService.answerNumber.vier + BREAK + antwort4 + '\n'
            + BREAK + antwort5 + '\n');
    }

    @Test
    public void notEmptyQuestionAndAnswerShouldReturnFalse() {
        var service = new SimpleTextToSpeechService();
        var speech = new Speech();
        speech.setQuestion("Ich bin nicht leer.");
        speech.setAnswer1("Ich auch nicht.");
        Assert.assertEquals(service.emptyQuestionAndAnswer(speech), false);
    }

    @Test
    public void emptyQuestionAndAnswerShouldReturnTrue() {
        var service = new SimpleTextToSpeechService();
        var speech = new Speech();
        speech.setQuestion("");
        speech.setAnswer1("");
        Assert.assertEquals(service.emptyQuestionAndAnswer(speech), true);
    }

    @Test
    public void validTextShouldReturnTrue() {
        var service = new SimpleTextToSpeechService();
        Assert.assertEquals(service.isValidText("Ein gültiger Text"), true);
    }

    @Test
    public void emptyTextShouldReturnFalse() {
        var service = new SimpleTextToSpeechService();
        Assert.assertEquals(service.isValidText("  "), false);
    }


    @After
    public void wrapUp() {

    }
}
