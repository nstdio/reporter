package com.github.nstdio.reporter.core;

import java.util.*;
import java.util.stream.Collectors;

public class ReportFormatter {
    private final OutputFormat format;
    private final Formatter formatter;

    public ReportFormatter(OutputFormat format, Formatter formatter) {
        this.format = format;
        this.formatter = formatter;
    }

    public ReportFormatter(Formatter formatter) {
        this(null, formatter);
    }

    public ReportFormatter(OutputFormat format) {
        this(format, null);
    }

    public ReportFormatter() {
        this(OutputFormat.TEXT, null);
    }

    public String today(ProjectReport... projectReports) {
        return today(Arrays.asList(projectReports));
    }

    public String today(List<ProjectReport> projectReports) {
        Formatter formatter = getFormatter();

        StringBuilder sb = new StringBuilder();
        sb.append(formatter.header());

        List<Task> sortedTasks = new ArrayList<>();
        projectReports.forEach(projectReport -> sortedTasks.addAll(projectReport.today()));

        sortedTasks.sort(Comparator.comparingInt(o -> o.commit().getCommitTime()));

        sortedTasks.get(0).setPeriod(null);

        for (int i = 1, n = sortedTasks.size(); i < n; i++) {
            sortedTasks.get(i).setPeriod(sortedTasks.get(i - 1).commit());
        }

        final Map<String, List<Task>> collect = sortedTasks
                .stream()
                .collect(Collectors.groupingBy(Task::getProject));

        for (Map.Entry<String, List<Task>> entry : collect.entrySet()) {
            if (entry.getValue().size() > 0) {
                sb.append(formatter.format(entry.getValue(), entry.getKey()));
            }
        }

        return sb.append(formatter.footer())
                .toString();
    }

    private Formatter getFormatter() {
        switch (format) {
            case TEXT:
                return TextFormatterHolder.INSTANCE;
            case HTML:
                return HtmlFormatterHolder.INSTANCE;
            default:
                if (formatter == null) {
                    throw new IllegalStateException("No formatter available.");
                }
                return formatter;
        }
    }

    private static class TextFormatterHolder {
        private static final Formatter INSTANCE = new TextFormatter();
    }

    private static class HtmlFormatterHolder {
        private static final Formatter INSTANCE = new HtmlFormatter();
    }

    private static class TextFormatter implements Formatter {
        private final StringBuilder stringBuilder = new StringBuilder();

        @Override
        public String header() {
            return "\nHi All,\n\nToday I've worked on following tasks:\n";
        }

        @Override
        public String footer() {
            return "\nThanks!\n";
        }

        @Override
        public String format(List<Task> tasks, String projectName) {
            stringBuilder
                    .append('\n')
                    .append(projectName)
                    .append('.')
                    .append('\n');

            tasks.forEach(this::append);

            final String formatted = stringBuilder.toString();
            stringBuilder.setLength(0);

            return formatted;
        }

        @Override
        public void append(Task task) {
            stringBuilder.append('\t')
                    .append(task.fullMessage())
                    .append(task.periodFormatted())
                    .append('\n');
        }
    }

    private static class HtmlFormatter implements Formatter {
        private final StringBuilder stringBuilder = new StringBuilder();

        @Override
        public String header() {
            return "<p>Hi All,</p><br><p>Today I've worked on following tasks:</p>";
        }

        @Override
        public String footer() {
            return "<p>Thanks!</p>";
        }

        @Override
        public String format(List<Task> tasks, String projectName) {
            stringBuilder
                    .append("<h3>")
                    .append(projectName).append('.')
                    .append("</h3>");

            stringBuilder.append("<ul>");

            tasks.forEach(this::append);

            stringBuilder.append("</ul>");

            final String html = stringBuilder.toString();
            stringBuilder.setLength(0);

            return html;
        }

        @Override
        public void append(Task task) {
            stringBuilder
                    .append("<li>")
                    .append(task.fullMessage())
                    .append(task.periodFormatted())
                    .append("</li>");
        }
    }
}
