package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;

@Controller
public class AlertController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static String LERNTIA = "[Lerntia] ";
    private static String SPACE = "   ";
    private Image INFO = new Image(getClass().getResourceAsStream("/icons/info.png"));
    private Image ERROR = new Image(getClass().getResourceAsStream("/icons/error.png"));
    private Image WARNING = new Image(getClass().getResourceAsStream("/icons/warning.png"));
    private Image CONFIRMATION = new Image(getClass().getResourceAsStream("/icons/confirmation.png"));

    public void showBigAlert(Alert.AlertType alertType, String title, String header, String content) {
        var alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        var dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add( "dialogue-content");

        var grid = new GridPane();
        var graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        var textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        ImageView imageView;
        if(alertType == Alert.AlertType.ERROR) {
            imageView = new ImageView(ERROR);
        } else if (alertType == Alert.AlertType.WARNING) {
            imageView = new ImageView(WARNING);
        } else if(alertType == Alert.AlertType.CONFIRMATION) {
            imageView = new ImageView(CONFIRMATION);
        } else {
            imageView = new ImageView(INFO);
        }
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);

        var stackPane = new StackPane(imageView);
        stackPane.setAlignment(Pos.CENTER);
        grid.add(stackPane, 0, 0);

        var headerLabel = new Label(header + SPACE);
        headerLabel.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        headerLabel.getStyleClass().add("dialogue-header");
        headerLabel.setWrapText(true);
        headerLabel.setAlignment(Pos.CENTER_RIGHT);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setMaxHeight(Double.MAX_VALUE);
        grid.add(headerLabel, 1, 0);

        dialogPane.setHeader(grid);
        if(alertType == Alert.AlertType.CONFIRMATION) {
            dialogPane.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        } else {
            dialogPane.getButtonTypes().setAll(ButtonType.OK);
        }
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


}
