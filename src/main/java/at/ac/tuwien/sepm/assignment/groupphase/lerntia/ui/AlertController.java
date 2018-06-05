package at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui;

import at.ac.tuwien.sepm.assignment.groupphase.util.ButtonText;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Controller
public class AlertController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static String LERNTIA = "[Lerntia] ";
    private static String SPACE = "   ";
    private Image INFO = new Image(getClass().getResourceAsStream("/icons/info.png"));
    private Image ERROR = new Image(getClass().getResourceAsStream("/icons/error.png"));
    private Image WARNING = new Image(getClass().getResourceAsStream("/icons/warning.png"));
    private Image CONFIRMATION = new Image(getClass().getResourceAsStream("/icons/confirmation.png"));
    private Image CORRECT = new Image(getClass().getResourceAsStream("/icons/correct.png"));
    private Image WRONG = new Image(getClass().getResourceAsStream("/icons/incorrect.png"));
    private boolean wrongAnswer = false;
    private boolean onlyWrongQuestions = false;
    private ImageView imageView;

    public void showBigAlert(Alert.AlertType alertType, String title, String header, String content) {
        var alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        var dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue-content");

        var grid = new GridPane();
        var graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        var textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        if (alertType == Alert.AlertType.ERROR) {
            imageView = new ImageView(ERROR);
        } else if (alertType == Alert.AlertType.WARNING) {
            imageView = new ImageView(WARNING);
        } else {
            imageView = new ImageView(INFO);
        }
        imageView.setFitWidth(84);
        imageView.setFitHeight(84);

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

        //used only for repeating questions
        var btnAll = new ButtonType(ButtonText.WRONGQUESTIONS.toString(), ButtonBar.ButtonData.YES);
        var btnFalse = new ButtonType(ButtonText.ALLQUESTIONS.toString(), ButtonBar.ButtonData.NO);

        dialogPane.setHeader(grid);
        if (alertType == Alert.AlertType.CONFIRMATION) {
            dialogPane.getButtonTypes().setAll(btnAll, btnFalse);
        } else {
            dialogPane.getButtonTypes().setAll(ButtonType.OK);
        }
        var stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));

        LOG.trace("Showing a big alert with title: " + title);

        if (alertType == Alert.AlertType.CONFIRMATION) {
            var result = alert.showAndWait();
            if (result.isPresent() && result.get() == btnAll) {
                onlyWrongQuestions = true;
            } else if (result.isPresent() && result.get() == btnFalse) {
                onlyWrongQuestions = false;
            }
        } else {
            stage.showAndWait();
        }
    }


    public DialogPane showWrongAnswerAlert(String title, String header, String content) {
        wrongAnswer = true;
        return showCorrectAnswerAlert(title, header, content);
    }

    public DialogPane showCorrectAnswerAlert(String title, String header, String content) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.NONE);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(ButtonText.CONTINUE.toString());

        var dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue-content");

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
        if (wrongAnswer) {
            imageView = new ImageView(WRONG);
            wrongAnswer = false;
        } else {
            imageView = new ImageView(CORRECT);
        }
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

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
        dialogPane.getButtonTypes().setAll(ButtonType.OK);

        var stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));
        stage.setMaximized(true);
        LOG.trace("Showing an answer alert with title: " + title);
        return dialogPane;
    }

    public void showStandardAlert(Alert.AlertType alertType, String title, String header, String content) {
        var alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);
        alert.showAndWait();
    }

    public boolean showStandardConfirmationAlert(String title, String header, String content) {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        var result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public boolean showBigConfirmationAlert(String title, String header, String content) {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        var dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue-content");

        var grid = new GridPane();
        var graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        var textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        imageView = new ImageView(CONFIRMATION);
        imageView.setFitWidth(84);
        imageView.setFitHeight(84);

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
        dialogPane.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText(ButtonText.YES.toString());
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText(ButtonText.NO.toString());
        var stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));

        ObjectProperty<ButtonType> result = new SimpleObjectProperty<>();
        for (var type : dialogPane.getButtonTypes()) {
            ((Button) dialogPane.lookupButton(type)).setOnAction(e -> result.set(type));
        }
        stage.showAndWait();
        LOG.trace("Showing a big confirmation alert with title: " + title);
        return result.get() == ButtonType.YES;
    }

    public boolean isOnlyWrongQuestions() {
        return onlyWrongQuestions;
    }

    public void setOnlyWrongQuestions(boolean onlyWrongQuestions) {
        this.onlyWrongQuestions = onlyWrongQuestions;
    }
}
