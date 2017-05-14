package com.github.nstdio.reporter.gui.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertBuilder {
    private AlertType alertType;
    private String content;
    private String title;
    private String header;

    private AlertBuilder(AlertType alertType) {
        this.alertType = alertType;
    }

    public static AlertBuilder builder(AlertType alertType) {
        return new AlertBuilder(alertType);
    }

    public static AlertBuilder builder() {
        return builder(AlertType.INFORMATION);
    }

    public Alert build() {
        final Alert alert = new Alert(alertType, content);
        alert.setHeaderText(header);
        return alert;
    }

    public AlertBuilder content(String content) {
        this.content = content;
        return this;
    }

    public AlertBuilder title(String title) {
        this.title = title;
        return this;
    }

    public AlertBuilder type(AlertType alertType) {
        this.alertType = alertType;
        return this;
    }

    public AlertBuilder header(String header) {
        this.header = header;
        return this;
    }
}
