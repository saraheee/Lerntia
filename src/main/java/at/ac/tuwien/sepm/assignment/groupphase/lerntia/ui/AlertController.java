package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Controller
public class AlertController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static String LERNTIA = "[Lerntia] ";

    public void showBigAlert(Alert.AlertType alertType, String title, String header, String content) {
        var alert = new Alert(alertType);
        var dialogPane = new DialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue");
        dialogPane.setHeader(new Text(" " + header + " "));
        dialogPane.setContent(new Text(" " + content + " "));
        dialogPane.getButtonTypes().setAll(ButtonType.OK);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);
        alert.setDialogPane(dialogPane);
        alert.showAndWait();
    }

    public void showStandardAlert(Alert.AlertType alertType, String title, String header, String content) {
        var alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);
        alert.showAndWait();
    }

    public boolean showConfirmation( String title, String header, String content ) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();

        return (result.get() == ButtonType.OK);
    }

    public boolean showBigConfirmation( String title, String header, String content ) {

        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setResizable(true);
        alert.setTitle(LERNTIA + title);

        var dialogPane = new DialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue");

        dialogPane.setHeader(new Text(" " + header + " "));
        dialogPane.setContent(new Text(" " + content + " "));

        dialogPane.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setDialogPane(dialogPane);
        var optional = alert.showAndWait();

        if (optional.isPresent() && optional.get() == ButtonType.YES) {
            return true;
        } else {
            return false;
        }
    }
}
