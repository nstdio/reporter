package com.github.nstdio.reporter.gui.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogFactory {

    public static Dialog<String> passwordDialog() {
        return new PasswordDialog();
    }

    public static Dialog<ButtonType> exceptionDialog(Throwable t) {
        return new ExceptionDialog(t);
    }

    public static Dialog<ButtonType> requiredConfigMissing() {
        return AlertBuilder.builder(Alert.AlertType.ERROR)
                .title("Missing config.")
                .header("Required configuration property is missing.")
                .content("Please fill all fields in preference window.")
                .build();
    }
}
