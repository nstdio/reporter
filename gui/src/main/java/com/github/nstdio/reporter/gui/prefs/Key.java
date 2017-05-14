package com.github.nstdio.reporter.gui.prefs;

public enum Key {
    COMMITER("commiter"),
    PROJECT_NAME("project.name"),
    PROJECT_PATH("project.path"),
    EMAIL_USERNAME("email.username"),
    EMAIL_FROM("email.from"),
    EMAIL_RECEIPT("email.receipt");

    private final String key;

    Key(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
