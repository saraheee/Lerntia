package at.ac.tuwien.sepm.assignment.groupphase.lerntia;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ConfigReaderException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceValidationException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Speech;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ITextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleTextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TextToSpeechServiceTest {

    private ITextToSpeechService textToSpeechService;

    @Before
    public void setUp() {
        ITextToSpeechService(new SimpleTextToSpeechService());
    }

    private void ITextToSpeechService(ITextToSpeechService simpleTextToSpeechService) {
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

    @Test
    public void playWelcomeAndQuestionAndAnswersShouldPersist() throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        textToSpeechService.playWelcomeText();
        var speech = new Speech();
        speech.setQuestion("Eine erste Frage?");
        speech.setAnswer1("Eine Antwort!");
        speech.setAnswer2("Noch eine Antwort!");
        textToSpeechService.readQuestionAndAnswers(speech);
        speech.setQuestion("Eine weitere Frage?");
        speech.setAnswer1("Eine weitere Antwort!");
        speech.setAnswer2("Noch eine weitere Antwort!");
        textToSpeechService.readQuestionAndAnswers(speech);
    }

    @Test
    public void readQuestionAndAnswersWithAllAnswersShouldPersist() throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        var speech = new Speech("frage", "a1", "a2", "a3", "a4", "a5", "voice");
        textToSpeechService.readQuestionAndAnswers(speech);
    }

    @Test(expected = TextToSpeechServiceException.class)
    public void readQuestionAndAnswersWithEmptyValuesShouldFail() throws TextToSpeechServiceValidationException, TextToSpeechServiceException {
        var speech = new Speech();
        speech.setQuestion("");
        speech.setAnswer1("");
        speech.setAnswer2("");
        textToSpeechService.readQuestionAndAnswers(speech);
    }

    @Test(expected = TextToSpeechServiceException.class)
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
    public void readFeedbackTextShouldPersist() throws TextToSpeechServiceValidationException, TextToSpeechServiceException {
        var speech = new Speech();
        speech.setFeedbackText("Feedback!");
        textToSpeechService.readFeedbackText(speech);
    }

    @Test
    public void setVoiceShouldPersist() throws TextToSpeechServiceException {
        textToSpeechService.playWelcomeText();
        var speech = new Speech();
        speech.setVoice("bits3-hsmm");
        textToSpeechService.setVoice(speech);
    }

    @Test
    public void noCurrentAudioShouldReturnTrue() {
        Assert.assertEquals(textToSpeechService.noCurrentAudio(), true);
    }

    @Test
    public void noCurrentAudioShouldReturnFalse() throws TextToSpeechServiceException, TextToSpeechServiceValidationException {
        var speech = new Speech();
        speech.setFeedbackText("Text zum Lesen!");
        textToSpeechService.readFeedbackText(speech);
        Assert.assertEquals(textToSpeechService.noCurrentAudio(), false);
    }

    @Test
    public void stopSpeakingWithAudioShouldPersist() throws TextToSpeechServiceValidationException, TextToSpeechServiceException {
        var speech = new Speech();
        speech.setFeedbackText("Feedback text!");
        textToSpeechService.readFeedbackText(speech);
        textToSpeechService.stopSpeaking();
    }

    @Test
    public void stopSpeakingWithoutAudioShouldPersist() {
        textToSpeechService.stopSpeaking();
    }

    @Test
    public void filterTextInParenthesisShouldPersist() {
        var service = new SimpleTextToSpeechService();
        Assert.assertEquals(service.filterTextInParenthesis("Hallo (a)b"), "Hallo b");
    }

    @Test
    public void filterTextInNestedParenthesisShouldPersist() {
        var service = new SimpleTextToSpeechService();
        Assert.assertEquals(service.filterTextInParenthesis("((Hallo (a)b) c)i"), "i");
    }

    @Test
    public void replaceUmlautsInTextWithUmlautsShouldPersist() {
        var service = new SimpleTextToSpeechService();
        Assert.assertEquals(service.replaceUmlauts("Möhren-Gemuese Kuchen"), "M\u00f6hren-Gem\u00fcse Kuchen");
    }

    @Test
    public void replaceUmlautsInTextWithoutUmlautsShouldPersist() {
        var service = new SimpleTextToSpeechService();
        Assert.assertEquals(service.replaceUmlauts("Bei mir gibt es keine Umlaute."), "Bei mir gibt es keine Umlaute.");
    }

    @Test
    public void getTextShouldPersist() throws TextToSpeechServiceValidationException {
        try {
            var configReaderSpeech = new ConfigReader("speech");

            final var BREAK = configReaderSpeech.getValue("break");
            final var ANSWER = configReaderSpeech.getValue("answerPrefix");
            configReaderSpeech.close();
            var service = new SimpleTextToSpeechService();
            var speech = new Speech();

            var question = "Wann ist das Semester vorbei?";
            var answer1 = "Bald.";
            var answer2 = "Sehr bald.";
            var answer3 = "Kurz bevor die Sommerferien anfangen.";
            var answer4 = "Am 29.Juni 2018 um 23:59.";
            var answer5 = "Keine der genannten Antworten.";

            speech.setQuestion(question);
            speech.setAnswer1(answer1);
            speech.setAnswer2(answer2);
            speech.setAnswer3(answer3);
            speech.setAnswer4(answer4);
            speech.setAnswer5(answer5);

            Assert.assertEquals(service.getQuestionAndAnswerText(speech), question
                + BREAK + ANSWER + SimpleTextToSpeechService.answerNumber.eins + BREAK + answer1 + '\n'
                + BREAK + ANSWER + SimpleTextToSpeechService.answerNumber.zwei + BREAK + answer2 + '\n'
                + BREAK + ANSWER + SimpleTextToSpeechService.answerNumber.drei + BREAK + answer3 + '\n'
                + BREAK + ANSWER + SimpleTextToSpeechService.answerNumber.vier + BREAK + answer4 + '\n'
                + BREAK + answer5 + '\n');
        } catch (ConfigReaderException e) {
            throw new TextToSpeechServiceValidationException(e.getCustomMessage());
        }
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

}
