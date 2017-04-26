package com.github.nstdio.reporter.core;

import java.util.List;

public interface Formatter {
    String header();

    String footer();

    String format(List<Task> tasks, String projectName);

    void append(Task task);
}
