package com.github.nstdio.reporter.core;

public enum OutputFormat {
    TEXT,
    HTML;

    public static OutputFormat from(String format) {
        switch (format) {
            case "text":
                return TEXT;
            case "html":
                return HTML;
            default:
                return null;
        }
    }
}
