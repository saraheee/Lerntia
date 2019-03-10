package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.lerntia.dto.Speech;
import at.ac.tuwien.lerntia.lerntia.service.ITextToSpeechService;
import at.ac.tuwien.lerntia.exception.TextToSpeechServiceException;
import at.ac.tuwien.lerntia.exception.TextToSpeechServiceValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.locks.ReentrantLock;

import static org.springframework.util.Assert.notNull;

@Controller
public class AudioController implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReentrantLock lock = new ReentrantLock();
    private final ITextToSpeechService iTextToSpeechService;
    private final AlertController alertController;

    @FXML
    private Button audioButton;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;

    @Autowired
    public AudioController(ITextToSpeechService iTextToSpeechService, AlertController alertController) {
        notNull(iTextToSpeechService, "'iTextToSpeechService' should not be null");
        notNull(alertController, "'alertController' should not be null");
        this.iTextToSpeechService = iTextToSpeechService;
        this.alertController = alertController;
    }

    @FXML
    private void onAudioButtonClicked() {
        LOG.debug("Audio button clicked");

        if (audioButton.isDefaultButton()) {
            deselectAudioButton();
            //stop sound
            stopReading();
            return;
        }

        if (this.question == null || this.question.trim().length() < 1) {
            showValidationFailedDialog();
        } else {
            //play sound of the question and all answers
            Speech tts = new Speech();
            tts.setQuestion(this.question);
            tts.setAnswer1(this.answer1);
            tts.setAnswer2(this.answer2);
            tts.setAnswer3(this.answer3);
            tts.setAnswer4(this.answer4);
            tts.setAnswer5(this.answer5);
            if (iTextToSpeechService != null) {
                try {
                    stopReading();
                    iTextToSpeechService.readQuestionAndAnswers(tts);

                } catch (TextToSpeechServiceException e) {
                    LOG.error("Failed to read question and answers. " + e.getCustomMessage());
                    showAudioErrorDialog();
                } catch (TextToSpeechServiceValidationException e) {
                    LOG.error("Validation failed for the input text of the speech synthesizer. " + e.getCustomMessage());
                    showValidationFailedDialog();
                }
            } else {
                LOG.error("Failed to read question and answers: iTextToSpeechService is 'null'");
                showAudioErrorDialog();
            }
        }
        selectAudioButton();
    }

    void readSingleAnswer(String answerText) {
        deselectAudioButton();
        if (answerText == null || answerText.trim().length() < 1) {
            showValidationFailedDialog();
        } else {
            Speech tts = new Speech();
            tts.setSingleAnswer(answerText);
            if (iTextToSpeechService != null) {
                try {
                    stopReading();
                    iTextToSpeechService.readSingleAnswer(tts);

                } catch (TextToSpeechServiceException e) {
                    LOG.error("Failed to read question and answers. " + e.getCustomMessage());
                    showAudioErrorDialog();
                } catch (TextToSpeechServiceValidationException e) {
                    LOG.error("Validation failed for the input text of the speech synthesizer. " + e.getCustomMessage());
                    showValidationFailedDialog();
                }
            } else {
                LOG.error("Failed to read question and answers: iTextToSpeechService is 'null'");
                showAudioErrorDialog();
            }
        }
    }

    void readFeedbackText(String feedbackText) {
        deselectAudioButton();
        Speech tts = new Speech();
        tts.setFeedbackText(feedbackText);
        if (iTextToSpeechService != null) {
            try {
                stopReading();
                iTextToSpeechService.readFeedbackText(tts);

            } catch (TextToSpeechServiceException e) {
                LOG.error("Failed to read the feedback text. " + e.getCustomMessage());
                showAudioErrorDialog();
            } catch (TextToSpeechServiceValidationException e) {
                LOG.error("Validation failed for the input text of the speech synthesizer. " + e.getCustomMessage());
                showValidationFailedDialog();
            }
        } else {
            LOG.error("Failed to read the feedback text: iTextToSpeechService is 'null'");
            showAudioErrorDialog();
        }
    }

    void stopReading() {
        iTextToSpeechService.stopSpeaking();
    }

    void setSelected() {
        onAudioButtonClicked();
    }

    private void showAudioErrorDialog() {
        alertController.showBigAlert(Alert.AlertType.ERROR, "Sprachausgabe fehlgeschlagen",
            "Der Text konnte leider nicht vorgelesen werden.", "");
    }

    private void showValidationFailedDialog() {
        alertController.showBigAlert(Alert.AlertType.ERROR, "Kein Text gefunden",
            "Kein Text zum Lesen vorhanden.", "");
    }

    void setQuestion(String question) {
        this.question = question;
    }

    void setAnswer1(String answer) {
        this.answer1 = answer;
    }

    void setAnswer2(String answer) {
        this.answer2 = answer;
    }

    void setAnswer3(String answer) {
        this.answer3 = answer;
    }

    void setAnswer4(String answer) {
        this.answer4 = answer;
    }

    void setAnswer5(String answer) {
        this.answer5 = answer;
    }

    private void selectAudioButton() {
        audioButton.defaultButtonProperty().setValue(true);
        Thread audioThread = new Thread(this);
        audioThread.start();
    }

    void deselectAudioButton() {
        audioButton.defaultButtonProperty().setValue(false);
    }

    @Override
    public void run() {
        while (audioButton != null && audioButton.isDefaultButton()) {
            lock.lock();
            try {
                if (iTextToSpeechService.noCurrentAudio()) {
                    deselectAudioButton();
                    LOG.debug("Deselected the audio button.");
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
