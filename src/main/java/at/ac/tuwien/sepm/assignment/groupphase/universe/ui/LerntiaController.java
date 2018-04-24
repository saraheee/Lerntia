package at.ac.tuwien.sepm.assignment.groupphase.universe.ui;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.universe.service.LerntiaService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class LerntiaController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LerntiaService lerntiaService;


    public LerntiaController(LerntiaService lerntiaService) {
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void onPlaceHolderButtonClicked() {
        LOG.trace("Placeholder button clicked");
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("[Lerntia] Info");
        alert.setHeaderText("Dieser Button hat noch keine Funktionalität!");
        var dialogPane = alert.getDialogPane();
        //dialogPane.getStylesheets().add(getClass().getResource("css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
        try {
            lerntiaService.goToExamMode();
        } catch (ServiceException e) {
            LOG.error("An error occured {}", e.getMessage());
        }
    }
}
