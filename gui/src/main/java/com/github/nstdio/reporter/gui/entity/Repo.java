package com.github.nstdio.reporter.gui.entity;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Repo {
    private final SimpleStringProperty project;
    private final SimpleStringProperty path;
    private final SimpleBooleanProperty valid;

    public Repo(String project, String path, boolean valid) {
        this.project = new SimpleStringProperty(project);
        this.path = new SimpleStringProperty(path);
        this.valid = new SimpleBooleanProperty(valid);
    }

    public String getProject() {
        return project.get();
    }

    public void setProject(String projectName) {
        project.set(projectName);
    }

    public String getPath() {
        return path.get();
    }

    public void setPath(String p) {
        path.set(p);
    }

    public boolean isValid() {
        return valid.get();
    }

    public void setValid(boolean valid) {
        this.valid.set(valid);
    }
}
