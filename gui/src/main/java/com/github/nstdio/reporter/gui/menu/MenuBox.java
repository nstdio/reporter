package com.github.nstdio.reporter.gui.menu;

import com.github.nstdio.reporter.gui.Loader;
import com.github.nstdio.reporter.gui.config.ConfigController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MenuBox extends VBox {

    public MenuBox() {
        Loader.load(this, "menu.fxml");
    }

    @FXML
    private void showPreference(ActionEvent event) throws IOException {
        ConfigController.present(getScene().getWindow());
    }
}
