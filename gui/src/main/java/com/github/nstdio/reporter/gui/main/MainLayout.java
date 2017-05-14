package com.github.nstdio.reporter.gui.main;

import com.github.nstdio.reporter.gui.Loader;
import com.github.nstdio.reporter.gui.editor.Editor;
import com.github.nstdio.reporter.gui.menu.MenuBox;
import com.github.nstdio.reporter.gui.statusbar.StatusBar;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainLayout extends BorderPane {

    @FXML
    private MenuBox menuBox;
    @FXML
    private StatusBar statusBar;
    @FXML
    private Editor editorBox;

    public MainLayout() {
        Loader.load(this, "main.fxml");
    }

    public void loadAsync() {
        editorBox.loadAsync();
    }
}
