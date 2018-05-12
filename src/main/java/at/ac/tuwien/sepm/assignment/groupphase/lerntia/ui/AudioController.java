package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.dto.Speech;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.IMainLerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ITextToSpeechService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

import static org.springframework.util.Assert.notNull;

@Controller
public class AudioController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ITextToSpeechService iTextToSpeechService;
    private final LerntiaMainController lerntiaMainController;

    @FXML
    private Button audioButton;

    @Autowired
    public AudioController(ITextToSpeechService iTextToSpeechService, IMainLerntiaService lerntiaService, LerntiaMainController lerntiaMainController) {
        notNull(iTextToSpeechService, "'iTextToSpeechService' should not be null");
        notNull(lerntiaService, "'lerntiaService' should not be null");
        notNull(lerntiaMainController, "'lerntiaMainController' should not be null");
        this.iTextToSpeechService = iTextToSpeechService;
        this.lerntiaMainController = lerntiaMainController;
    }

    @FXML
    private void onAudioButtonClicked() {
        LOG.debug("Audio button clicked");
        //play sound
        var tts = new Speech();
        tts.setQuestion(lerntiaMainController.getQuestion());
        tts.setAnswer1(lerntiaMainController.getAnswer1());
        tts.setAnswer2(lerntiaMainController.getAnswer2());
        tts.setAnswer3(lerntiaMainController.getAnswer3());
        tts.setAnswer4(lerntiaMainController.getAnswer4());
        tts.setAnswer5(lerntiaMainController.getAnswer5());

        if (iTextToSpeechService != null) {
            try {
                iTextToSpeechService.stopSpeaking();
                iTextToSpeechService.speak(tts);
            } catch (ServiceException e) {
                LOG.error("Failed to read question and answers.");
                //TODO: show alert
            }
        } else {
            LOG.error("Failed to read question and answers: iTextToSpeechService is 'null'");
        }
    }


    void setSelected() {
        if (audioButton.isDefaultButton()) {
            audioButton.defaultButtonProperty().setValue(false);
            //stop sound
            try {
                iTextToSpeechService.stopSpeaking();
            } catch (ServiceException e) {
                LOG.error("Failed to stop speech synthesizer.");
            }
        } else {
            audioButton.defaultButtonProperty().setValue(true);
            onAudioButtonClicked();
        }

    }

}
