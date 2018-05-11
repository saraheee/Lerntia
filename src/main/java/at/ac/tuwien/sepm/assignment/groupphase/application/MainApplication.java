package at.ac.tuwien.sepm.assignment.groupphase.application;

import at.ac.tuwien.sepm.assignment.groupphase.lerntia.talk.TextToSpeech;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.LerntiaMainController;
import at.ac.tuwien.sepm.assignment.groupphase.util.JDBCConnectionManager;
import at.ac.tuwien.sepm.assignment.groupphase.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
            dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
            dialogPane.getStyleClass().add("dialogue");
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

        var loader = new FXMLLoader(getClass().getResource("/fxml/lerntia.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();
        LerntiaMainController controller = loader.getController();

        var scene = new Scene((Parent) fxmlLoader.load(getClass().getResourceAsStream("/fxml/lerntia.fxml")));
        controller.update(scene);
        primaryStage.setScene(scene);


        // show application
        primaryStage.show();
        primaryStage.toFront();
        try {
            var tts = new TextToSpeech();
            tts.setVoice("bits3-hsmm");
            tts.speak("Hallo und willkommen bei Lerntia. Schön dass du hier bist!", 1.0f, false, false);
        } catch (Exception e) {
            LOG.error("Failed to start MaryTTS.");
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("[Lerntia] MaryTTS konnte nicht gestartet werden");
            alert.setHeaderText("MaryTTS konnte nicht gestartet werden.\nDaher wird die Sprachausgabe nicht funktionieren!");
            alert.setContentText("Bitte Java 10.0.1 herunterladen!\nDer Fehler tretet in den Versionen Java 9 und Java 10 auf.");
            alert.setResizable(true);
            alert.showAndWait();
        }
        LOG.debug("Application startup complete");
    }

    @Override
    public void stop() {
        LOG.debug("Stopping application");
        JDBCConnectionManager.closeConnection();
        context.close();

    }
}
