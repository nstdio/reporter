package com.github.nstdio.reporter.gui.event;

public class ReportGeneratedEvent {
    private final String report;

    private ReportGeneratedEvent(String report) {
        this.report = report;
    }

    public static ReportGeneratedEvent of(String report) {
        return new ReportGeneratedEvent(report);
    }

    public String getReport() {
        return report;
    }
}
