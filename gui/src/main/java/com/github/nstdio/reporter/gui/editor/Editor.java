package com.github.nstdio.reporter.gui.editor;

import com.github.nstdio.reporter.gui.App;
import com.github.nstdio.reporter.gui.Loader;
import com.github.nstdio.reporter.gui.dialog.AlertBuilder;
import com.github.nstdio.reporter.gui.event.MailEvent;
import com.github.nstdio.reporter.gui.event.ReportGeneratedEvent;
import com.github.nstdio.reporter.gui.event.ReportGenerationStartEvent;
import com.github.nstdio.reporter.gui.prefs.Key;
import com.github.nstdio.reporter.gui.prefs.Prefs;
import com.github.nstdio.reporter.gui.service.ReportService;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import java.util.Optional;

import static com.github.nstdio.reporter.gui.dialog.DialogFactory.exceptionDialog;
import static com.github.nstdio.reporter.gui.dialog.DialogFactory.passwordDialog;
import static javafx.application.Platform.runLater;

public class Editor extends VBox {
    private final Prefs prefs = Prefs.defaultPrefs();
    private final ReportService reportService;

    @FXML
    private HTMLEditor editor;
    @FXML
    private Button sendBtn;
    @FXML
    private Button refreshBtn;

    public Editor() {
        Loader.load(this, "editor.fxml");

        sendBtn.setDisable(true);

        reportService = new ReportService();

        App.eventBus().register(this);
    }

    public void loadAsync() {
        reportService.load();
    }

    @Subscribe
    public void reportGenerated(ReportGeneratedEvent event) {
        runLater(() ->
                Optional.ofNullable(event.getReport())
                        .ifPresent(s -> {
                            editor.setHtmlText(s);
                            sendBtn.setDisable(false);
                        })
        );
    }

    @Subscribe
    public void reportGenerationStart(ReportGenerationStartEvent event) {
        sendBtn.setDisable(true);
    }

    @FXML
    private void onSend() {
        sendBtn.setDisable(true);
        if (prefs.get(Key.EMAIL_USERNAME).isPresent()) {
            passwordDialog()
                    .showAndWait()
                    .map(String::toCharArray)
                    .ifPresent(password -> reportService.send(password, editor.getHtmlText()));
        } else {
            showAlert();
        }
    }

    @Subscribe
    public void sendFailedEvent(MailEvent.SendFailedEvent event) {
        runLater(() -> exceptionDialog(event.getCause()).show());
    }

    private void showAlert() {
        AlertBuilder.builder(AlertType.ERROR)
                .title("Missing configuration")
                .header("Username not provided.")
                .content("In order to send a report, you must enter your username in Preference.")
                .build()
                .showAndWait();
    }

    @FXML
    private void onRefresh() {
        reportService.load();
    }
}
