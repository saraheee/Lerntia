package at.ac.tuwien.sepm.assignment.groupphase.application;

import at.ac.tuwien.sepm.assignment.groupphase.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.exception.TextToSpeechServiceException;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.ITextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.service.impl.SimpleTextToSpeechService;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.AlertController;
import at.ac.tuwien.sepm.assignment.groupphase.lerntia.ui.LerntiaMainController;
import at.ac.tuwien.sepm.assignment.groupphase.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/main.png")));
        primaryStage.setMaximized(true);
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            var alertController = new AlertController();
            if (alertController.showBigConfirmationAlert("Wirklich beenden",
                "Soll Lerntia wirklich beendet werden?", "")) {
                LOG.debug("Application shutdown initiated");
                controller.fireFeedbackAlert();
                return;
            }
            event.consume();
        });

        Font.loadFont(getClass().getResource("/fonts/Arial Black.ttf").toExternalForm(), 36);
        Font.loadFont(getClass().getResource("/fonts/Lora-Bold.ttf").toExternalForm(), 36);
        context = new AnnotationConfigApplicationContext(getClass());
        final var fxmlLoader = context.getBean(SpringFXMLLoader.class);

        var loader = new FXMLLoader(getClass().getResource("/fxml/lerntia.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();
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

    @Override
    public void stop() {
        LOG.debug("Stopping application");
        try {
            LOG.info("Stopping the learning algorithm");
            controller.stopAlgorithm();
        } catch (ServiceException e) {
            LOG.debug("Can't shutdown the learning algorithm.");
        }
        if (iTextToSpeechService != null) {
            controller.stopAudio();
        }
        try {
            LOG.debug("Starting thread interrupt");
            textToSpeechThread.interrupt();
            LOG.info("Thread is interrupted");
        } catch (IllegalThreadStateException e) {
            LOG.debug("Interrupting textToSpeech thread!");
        }
        context.close();
        Platform.exit();
    }

    @Override
    public void run() {
        iTextToSpeechService = new SimpleTextToSpeechService();
        try {
            iTextToSpeechService.playWelcomeText();
        } catch (TextToSpeechServiceException e) {
            LOG.error("Failed to play welcome text with the speech synthesizer!");
        }
    }

}
