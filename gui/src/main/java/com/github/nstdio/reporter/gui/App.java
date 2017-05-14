package com.github.nstdio.reporter.gui;

import com.github.nstdio.reporter.gui.exception.RequiredConfigMissing;
import com.github.nstdio.reporter.gui.main.MainLayout;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.github.nstdio.reporter.gui.dialog.DialogFactory.exceptionDialog;
import static com.github.nstdio.reporter.gui.dialog.DialogFactory.requiredConfigMissing;

public class App extends Application {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final EventBus eventBus = new EventBus();

    public static void main(String[] args) {
        launch(args);
    }

    public static EventBus eventBus() {
        return eventBus;
    }

    private static void threadAwareExceptionHandler(Thread thread, Throwable throwable) {
        if (Platform.isFxApplicationThread()) {
            handleException(throwable);
        } else {
            Platform.runLater(() -> handleException(throwable));
        }
    }

    private static void handleException(Throwable throwable) {
        if (throwable instanceof RequiredConfigMissing) {
            requiredConfigMissing().show();
        } else {
            exceptionDialog(throwable);
        }
    }

    private static void exit(WindowEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(App::threadAwareExceptionHandler);

        final MainLayout root = new MainLayout();
        Scene scene = new Scene(root, 600, 575);

        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, window -> root.loadAsync());
        stage.setOnCloseRequest(App::exit);

        stage.setTitle("Reporter GUI");
        stage.setScene(scene);
        stage.show();
    }
}
