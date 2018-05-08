package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class AudioController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LerntiaService lerntiaService;

    @Autowired
    public AudioController(LerntiaService lerntiaService) {
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void onAudioButtonClicked() {
        LOG.debug("Audio button clicked");

    }


}
