package at.ac.tuwien.lerntia.lerntia.ui;

import at.ac.tuwien.lerntia.util.ButtonText;
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
    private static final String LERNTIA = "[Lerntia] ";
    private static final String SPACE = "   ";
    private static final int MINWIDTH = 50;
    private final Image INFO = new Image(getClass().getResourceAsStream("/icons/info.png"));
    private final Image ERROR = new Image(getClass().getResourceAsStream("/icons/error.png"));
    private final Image WARNING = new Image(getClass().getResourceAsStream("/icons/warning.png"));
    private final Image CONFIRMATION = new Image(getClass().getResourceAsStream("/icons/confirmation.png"));
    private final Image CORRECT = new Image(getClass().getResourceAsStream("/icons/correct.png"));
    private final Image WRONG = new Image(getClass().getResourceAsStream("/icons/incorrect.png"));
    private final Image UNDEFINED = new Image(getClass().getResourceAsStream("/icons/feedback.png"));
    private boolean wrongAnswer = false;
    private boolean undefinedAnswer = false;
    private boolean onlyWrongQuestions = false;
    private ImageView imageView;

    public void showBigAlert(Alert.AlertType alertType, String title, String header, String content) {
        LOG.debug("Create big alert.");
        title = (title == null) ? "" : title;
        header = (header == null) ? "" : header;
        content = (content == null) ? "" : content;

        StringBuilder headerBuilder = new StringBuilder(header);
        while (headerBuilder.length() < MINWIDTH) {
            headerBuilder.append(" ");
        }
        header = headerBuilder.toString();
        Alert alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue-content");

        GridPane grid = new GridPane();
        ColumnConstraints graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        ColumnConstraints textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        getAlertImage(alertType);
        imageView.setFitWidth(84);
        imageView.setFitHeight(84);

        StackPane stackPane = new StackPane(imageView);
        stackPane.setAlignment(Pos.CENTER);
        grid.add(stackPane, 0, 0);

        Label headerLabel = new Label(header + SPACE);
        headerLabel.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        headerLabel.getStyleClass().add("dialogue-header");
        headerLabel.setWrapText(true);
        headerLabel.setAlignment(Pos.CENTER_RIGHT);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setMaxHeight(Double.MAX_VALUE);
        grid.add(headerLabel, 1, 0);

        //used only for repeating questions
        ButtonType btnAll = new ButtonType(ButtonText.WRONGQUESTIONS.toString(), ButtonBar.ButtonData.YES);
        ButtonType btnFalse = new ButtonType(ButtonText.ALLQUESTIONS.toString(), ButtonBar.ButtonData.NO);

        dialogPane.setHeader(grid);
        if (alertType == Alert.AlertType.CONFIRMATION) {
            dialogPane.getButtonTypes().setAll(btnAll, btnFalse);
        } else {
            dialogPane.getButtonTypes().setAll(ButtonType.OK);
        }
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));

        LOG.debug("Showing a big alert with title: " + title);
        checkConfirmation(alertType, alert, btnAll, btnFalse, stage);
    }

    private void checkConfirmation(Alert.AlertType alertType, Alert alert, ButtonType btnAll, ButtonType btnFalse, Stage stage) {
        if (alertType == Alert.AlertType.CONFIRMATION) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == btnAll) {
                onlyWrongQuestions = true;
            } else if (result.isPresent() && result.get() == btnFalse) {
                onlyWrongQuestions = false;
            }
        } else {
            stage.showAndWait();
        }
    }

    public void showBigAlertWithDiagram(Alert.AlertType alertType, String title, String header, String content, ImageView imageView) {
        this.imageView = imageView;
        StringBuilder headerBuilder = new StringBuilder(header);
        while (headerBuilder.length() < MINWIDTH) {
            headerBuilder.append(" ");
        }
        header = headerBuilder.toString();
        Alert alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue-content");

        GridPane grid = new GridPane();
        ColumnConstraints graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        ColumnConstraints textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        getAlertImage(alertType);

        StackPane stackPane = new StackPane(imageView);
        stackPane.setAlignment(Pos.CENTER);
        grid.add(stackPane, 0, 0);

        Label headerLabel = new Label(header + SPACE);
        headerLabel.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        headerLabel.getStyleClass().add("dialogue-header");
        headerLabel.setWrapText(true);
        headerLabel.setAlignment(Pos.CENTER_RIGHT);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setMaxHeight(Double.MAX_VALUE);
        grid.add(headerLabel, 1, 0);

        //used only for repeating questions
        ButtonType btnAll = new ButtonType(ButtonText.WRONGQUESTIONS.toString(), ButtonBar.ButtonData.YES);
        ButtonType btnFalse = new ButtonType(ButtonText.ALLQUESTIONS.toString(), ButtonBar.ButtonData.NO);

        dialogPane.setHeader(grid);
        if (alertType == Alert.AlertType.CONFIRMATION) {
            dialogPane.getButtonTypes().setAll(btnAll, btnFalse);
        } else {
            dialogPane.getButtonTypes().setAll(ButtonType.OK);
        }
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));
        alert.setGraphic(imageView);

        LOG.debug("Showing a big diagram alert with title: " + title);
        checkConfirmation(alertType, alert, btnAll, btnFalse, stage);
    }


    public boolean showWrongAnswerAlert(String title, String header, String content) {
        wrongAnswer = true;
        return showCorrectAnswerAlert(title, header, content);
    }

    public boolean showUndefinedAnswerAlert(String title, String header, String content) {
        undefinedAnswer = true;
        return showCorrectAnswerAlert(title, header, content);
    }

    public boolean showCorrectAnswerAlert(String title, String header, String content) {
        LOG.debug("Create answer alert.");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(ButtonText.CONTINUE.toString());

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue-content");

        GridPane grid = new GridPane();
        ColumnConstraints graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        ColumnConstraints textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        ImageView imageView;
        if (wrongAnswer) {
            imageView = new ImageView(WRONG);
            wrongAnswer = false;
        } else if(undefinedAnswer) {
            imageView = new ImageView(UNDEFINED);
            undefinedAnswer = false;

        } else {
            imageView = new ImageView(CORRECT);
        }
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        StackPane stackPane = new StackPane(imageView);
        stackPane.setAlignment(Pos.CENTER);
        grid.add(stackPane, 0, 0);

        Label headerLabel = new Label(header + SPACE);
        headerLabel.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        headerLabel.getStyleClass().add("dialogue-header");
        headerLabel.setWrapText(true);
        headerLabel.setAlignment(Pos.CENTER_RIGHT);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setMaxHeight(Double.MAX_VALUE);
        grid.add(headerLabel, 1, 0);

        dialogPane.setHeader(grid);
        dialogPane.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText(ButtonText.SHOW.toString());
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText(ButtonText.CONTINUE.toString());
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));
        stage.setMaximized(true);

        ObjectProperty<ButtonType> result = new SimpleObjectProperty<>();
        for (ButtonType type : dialogPane.getButtonTypes()) {
            ((Button) dialogPane.lookupButton(type)).setOnAction(e -> result.set(type));
        }
        LOG.debug("Showing an answer alert with title: " + title);
        stage.showAndWait();
        return result.get() == ButtonType.YES;
    }

    public void showStandardAlert(Alert.AlertType alertType, String title, String header, String content) {
        LOG.info("Create standard alert");
        title = (title == null) ? "" : title;
        header = (header == null) ? "" : header;
        content = (content == null) ? "" : content;

        StringBuilder headerBuilder = new StringBuilder(header);
        while (headerBuilder.length() < MINWIDTH) {
            headerBuilder.append(" ");
        }
        header = headerBuilder.toString();

        Alert alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().clear();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog-standard.css").toExternalForm());
        dialogPane.getStyleClass().add("standard-dialogue-content");

        GridPane grid = new GridPane();
        ColumnConstraints graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        ColumnConstraints textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        getAlertImage(alertType);
        imageView.setFitWidth(54);
        imageView.setFitHeight(54);

        StackPane stackPane = new StackPane(imageView);
        stackPane.setAlignment(Pos.CENTER);
        grid.add(stackPane, 0, 0);

        Label headerLabel = new Label(header + SPACE);
        headerLabel.getStylesheets().add(getClass().getResource("/css/dialog-standard.css").toExternalForm());
        headerLabel.getStyleClass().add("standard-dialogue-header");
        headerLabel.setWrapText(true);
        headerLabel.setAlignment(Pos.CENTER_RIGHT);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setMaxHeight(Double.MAX_VALUE);
        grid.add(headerLabel, 1, 0);

        dialogPane.setHeader(grid);
        dialogPane.getButtonTypes().setAll(ButtonType.OK);
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));

        LOG.debug("Showing a standard alert with title: " + title);
        stage.showAndWait();
    }


    private void getAlertImage(Alert.AlertType alertType) {
        if (alertType == Alert.AlertType.ERROR) {
            imageView = new ImageView(ERROR);
        } else if (alertType == Alert.AlertType.WARNING) {
            imageView = new ImageView(WARNING);
        } else {
            imageView = new ImageView(INFO);
        }
    }


    public boolean showStandardConfirmationAlert(String title, String header, String content) {
        LOG.debug("Create standard confirmation alert.");
        title = (title == null) ? "" : title;
        header = (header == null) ? "" : header;
        content = (content == null) ? "" : content;

        StringBuilder headerBuilder = new StringBuilder(header);
        while (headerBuilder.length() < MINWIDTH) {
            headerBuilder.append(" ");
        }
        header = headerBuilder.toString();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().clear();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog-standard.css").toExternalForm());
        dialogPane.getStyleClass().add("standard-dialogue-content");

        GridPane grid = new GridPane();
        ColumnConstraints graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        ColumnConstraints textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        imageView = new ImageView(CONFIRMATION);
        imageView.setFitWidth(54);
        imageView.setFitHeight(54);

        StackPane stackPane = new StackPane(imageView);
        stackPane.setAlignment(Pos.CENTER);
        grid.add(stackPane, 0, 0);

        Label headerLabel = new Label(header + SPACE);
        headerLabel.getStylesheets().add(getClass().getResource("/css/dialog-standard.css").toExternalForm());
        headerLabel.getStyleClass().add("standard-dialogue-header");
        headerLabel.setWrapText(true);
        headerLabel.setAlignment(Pos.CENTER_RIGHT);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setMaxHeight(Double.MAX_VALUE);
        grid.add(headerLabel, 1, 0);

        dialogPane.setHeader(grid);
        dialogPane.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText(ButtonText.YES.toString());
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText(ButtonText.NO.toString());
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));

        ObjectProperty<ButtonType> result = new SimpleObjectProperty<>();
        for (ButtonType type : dialogPane.getButtonTypes()) {
            ((Button) dialogPane.lookupButton(type)).setOnAction(e -> result.set(type));
        }
        stage.showAndWait();
        LOG.trace("Showing a standard confirmation alert with title: " + title);
        return result.get() == ButtonType.YES;
    }

    public boolean showBigConfirmationAlert(String title, String header, String content) {
        LOG.debug("Create big confirmation alert.");
        title = (title == null) ? "" : title;
        header = (header == null) ? "" : header;
        content = (content == null) ? "" : content;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(content + SPACE);
        alert.setTitle(LERNTIA + title);
        alert.setResizable(true);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialogue-content");

        GridPane grid = new GridPane();
        ColumnConstraints graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);

        ColumnConstraints textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        imageView = new ImageView(CONFIRMATION);
        imageView.setFitWidth(84);
        imageView.setFitHeight(84);

        StackPane stackPane = new StackPane(imageView);
        stackPane.setAlignment(Pos.CENTER);
        grid.add(stackPane, 0, 0);

        Label headerLabel = new Label(header + SPACE);
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
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image("/icons/main.png"));

        ObjectProperty<ButtonType> result = new SimpleObjectProperty<>();
        for (ButtonType type : dialogPane.getButtonTypes()) {
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
