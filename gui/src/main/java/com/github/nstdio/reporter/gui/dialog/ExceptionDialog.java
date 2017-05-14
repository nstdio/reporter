package com.github.nstdio.reporter.gui.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javaslang.control.Try;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import static java.awt.Desktop.getDesktop;

class ExceptionDialog extends Alert {
    private static final String ISSUES = "https://github.com/nstdio/reporter/issues";

    ExceptionDialog(Throwable t) {
        super(AlertType.ERROR);

        setTitle("Error during process execution");
        setHeaderText("A runtime error has occurred");
        setContentText(String.format("Caused by: %s", ExceptionUtils.getRootCauseMessage(t)));

        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));

        FlowPane flowPane = new FlowPane();
        Hyperlink link = new Hyperlink(ISSUES);
        flowPane.getChildren().addAll(
                new Label("Please submit this error on "), link
        );

        link.setOnAction((event) -> Try.run(() -> getDesktop().browse(new URI(ISSUES))));

        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(flowPane, 0, 0);
        expContent.add(new Label("The exception stacktrace was:"), 0, 1);
        expContent.add(textArea, 0, 2);

        getDialogPane().setExpandableContent(expContent);
    }
}
