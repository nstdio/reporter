package com.github.nstdio.reporter.gui.statusbar;

import com.github.nstdio.reporter.gui.App;
import com.github.nstdio.reporter.gui.Loader;
import com.github.nstdio.reporter.gui.event.MailEvent;
import com.github.nstdio.reporter.gui.event.ReportGeneratedEvent;
import com.github.nstdio.reporter.gui.event.ReportGenerationStartEvent;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class StatusBar extends VBox {
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label statusLbl;

    public StatusBar() {
        Loader.load(this, "statusbar.fxml");
        App.eventBus().register(this);
    }

    private void changeState(String status, boolean indicatorVisible) {
        Platform.runLater(() -> {
            progressIndicator.setVisible(indicatorVisible);
            statusLbl.setText(status);
        });
    }

    @Subscribe
    public void onReportGenerated(ReportGeneratedEvent event) {
        final String status = Optional.ofNullable(event.getReport())
                .map(s -> "Report successfully loaded.")
                .orElse("No report has been generated");

        changeState(status, false);
    }

    @Subscribe
    public void reportGenerationStart(ReportGenerationStartEvent event) {
        changeState("Generating report", true);
    }

    @Subscribe
    public void sendStartEvent(MailEvent.SendStartEvent event) {
        changeState("Sending email", true);
    }

    @Subscribe
    public void sendFailedEvent(MailEvent.SendFailedEvent event) {
        final String status = String.format("Exception while sending email: %s (%s)",
                event.getCause().getMessage(), event.getCause().getClass());

        changeState(status, false);
    }

    @Subscribe
    public void sentEvent(MailEvent.SentEvent event) {
        changeState("Email successfully sent.", false);
    }
}
