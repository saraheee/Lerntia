package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.LerntiaService;
import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class LerntiaController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LerntiaService lerntiaService;
    @FXML
    private HBox mainWindow;
    @FXML
    private VBox mainWindowLeft;
    @FXML
    private VBox mainWindowRight;
    @FXML
    private ImageView mainImage;
    @FXML
    private HBox firstAnswer;


    public LerntiaController(LerntiaService lerntiaService) {
        this.lerntiaService = lerntiaService;
    }

    @FXML
    private void initialize() {
        mainWindowLeft.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(25));
        //mainImage.setFitWidth(mainWindow.getWidth() / 100 * 15);
        mainWindowRight.prefWidthProperty().bind(mainWindow.widthProperty().divide(100).multiply(75));

        //Example for selecting the first answer
        var nodeOut = firstAnswer.getChildren().get(0);
        if (nodeOut instanceof HBox) {
            for (var nodeIn : ((HBox) nodeOut).getChildren()) {
                if (nodeIn instanceof CheckBox) {
                    ((CheckBox) nodeIn).setSelected(true);
                }
            }
        }
    }


    @FXML
    private void onPlaceHolderButtonClicked() {
        LOG.trace("Placeholder button clicked");
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("[Lerntia] Info");
        alert.setHeaderText("Dieser Button hat noch keine Funktionalität!");
        alert.setResizable(true);
        var dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
        /*try {
            lerntiaService.goToExamMode();
        } catch (ServiceException e) {
            LOG.error("An error occured {}", e.getMessage());
        }*/
    }
}
