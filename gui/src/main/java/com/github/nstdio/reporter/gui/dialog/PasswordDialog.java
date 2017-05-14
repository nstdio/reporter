package com.github.nstdio.reporter.gui.dialog;

import com.github.nstdio.reporter.gui.Loader;
import com.github.nstdio.reporter.gui.prefs.Key;
import com.github.nstdio.reporter.gui.prefs.Prefs;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;

/**
 * @link https://gist.github.com/drguildo/ba2834bf52d624113041
 */
class PasswordDialog extends Dialog<String> {
    private PasswordField passwordField;

    PasswordDialog() {
        setTitle("Password");
        setHeaderText(headerText());
        setGraphic(Loader.icon("key"));

        ButtonType passwordButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(passwordButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Password:"), 0, 0);
        grid.add(passwordField, 1, 0);

        Node okBtn = getDialogPane().lookupButton(passwordButtonType);
        okBtn.setDisable(true);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            okBtn.setDisable(newValue.trim().isEmpty());
        });

        getDialogPane().setContent(grid);

        Platform.runLater(() -> passwordField.requestFocus());

        setResultConverter(dialogButton -> {
            if (dialogButton == passwordButtonType) {
                return passwordField.getText();
            }
            return null;
        });
    }

    private static String headerText() {
        return Prefs.defaultPrefs().get(Key.EMAIL_USERNAME)
                .map(s -> String.format("Please enter your email password. Username is %s", s))
                .orElse("Please enter your email password.");
    }
}
