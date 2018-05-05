package at.ac.tuwien.sepm.assignment.groupphase.application;

import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
@ComponentScan("at.ac.tuwien.sepm.assignment.groupphase")
public final class MainApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        LOG.debug("Application starting with arguments={}", (Object) args);
        Application.launch(MainApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // setup application
        primaryStage.setTitle("[Lerntia] Lern- und Prüfungstool");
        primaryStage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("/icons/main.png")));
        primaryStage.setMaximized(true);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(primaryStage);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setResizable(true);
            alert.setTitle("[Lerntia] Wirklich beenden?");

            var dialogPane = new DialogPane();
            final var header = new Text("\n  Soll Lerntia wirklich beendet werden?");
            dialogPane.setStyle("-fx-font-size: 34; -fx-font-family: Lora-Bold; -fx-font-weight: bold; -fx-text-fill: #333333");
            dialogPane.setHeader(header);
            dialogPane.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.setDialogPane(dialogPane);
            var optional = alert.showAndWait();

            if (optional.isPresent() && optional.get() == ButtonType.YES) {
                LOG.debug("Application shutdown initiated");
                return;
            }
            event.consume();
        });

        Font.loadFont(MainApplication.class.getResource("/fonts/Arial Black.ttf").toExternalForm(), 36);
        Font.loadFont(MainApplication.class.getResource("/fonts/Lora-Bold.ttf").toExternalForm(), 36);
        context = new AnnotationConfigApplicationContext(MainApplication.class);
        final var fxmlLoader = context.getBean(SpringFXMLLoader.class);
        var scene = new Scene((Parent) fxmlLoader.load(getClass().getResourceAsStream("/fxml/lerntia.fxml")));
        primaryStage.setScene(scene);


        // show application
        primaryStage.show();
        primaryStage.toFront();
        LOG.debug("Application startup complete");
    }

    @Override
    public void stop() {
        LOG.debug("Stopping application");
        JDBCConnectionManager.closeConnection();
        context.close();

    }
}
