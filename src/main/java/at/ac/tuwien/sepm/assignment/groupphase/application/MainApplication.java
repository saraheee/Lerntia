package at.ac.tuwien.sepm.assignment.groupphase.application;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ITextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleTextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.LerntiaMainController;
import at.ac.tuwien.sepm.assignment.groupphase.util.ConfigReader;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
@ComponentScan("at.ac.tuwien.sepm.assignment.groupphase")
public final class MainApplication extends Application implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Thread textToSpeechThread;
    private AnnotationConfigApplicationContext context;
    private ITextToSpeechService iTextToSpeechService;
    private LerntiaMainController controller;

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
        // LerntiaMainController
        controller = loader.getController();

        var scene = new Scene((Parent) fxmlLoader.load(getClass().getResourceAsStream("/fxml/lerntia.fxml")));
        controller.update(scene);
        primaryStage.setScene(scene);

        // show application
        primaryStage.show();
        primaryStage.toFront();
        iTextToSpeechService = new SimpleTextToSpeechService();

        textToSpeechThread = new Thread(new MainApplication());
        textToSpeechThread.start();
        LOG.debug("Application startup complete");
    }

    @Autowired
    public void getiTextToSpeechService(ITextToSpeechService iTextToSpeechService) {
        this.iTextToSpeechService = iTextToSpeechService;
    }

    @Override
    public void stop() {
        LOG.debug("Stopping application");

        if (iTextToSpeechService != null) {
            controller.stopAudio();
        } else {
            LOG.debug("iTextToSpeechService is already null.");
        }

        try {
            LOG.info("Starting Thread interrupt");
            textToSpeechThread.interrupt();
            LOG.info("Thread is Interrupted");
        } catch (IllegalThreadStateException e) {
            LOG.debug("Interrupting textToSpeech thread: " + e.getMessage());
        }

    // todo check if it really is ok just delete this   JDBCConnectionManager.closeConnection();
        context.close();
    }

    @Override
    public void run() {
        iTextToSpeechService = new SimpleTextToSpeechService();
        try {
            iTextToSpeechService.playWelcomeText();
        } catch (ServiceException e) {
            LOG.error("Failed to play welcome text with the speech synthesizer: " + e.getMessage());
        }
    }

}
