package com.github.nstdio.reporter.core;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.StringUtils;
import org.joda.time.Duration;
import org.joda.time.Period;

import java.util.Optional;

import static com.github.nstdio.reporter.core.TimeUtil.commitMillis;

public class Task {
    private final RevCommit commit;
    private final String project;
    private Period period;

    private Task(String project, RevCommit current) {
        this.project = project;
        commit = current;
    }

    public static Task from(String project, RevCommit current) {
        return new Task(project, current);
    }

    public RevCommit commit() {
        return commit;
    }

    public String fullMessage() {
        return StringUtils.replaceLineBreaksWithSpace(commit.getFullMessage());
    }

    public Period period() {
        return period;
    }

    public void setPeriod(RevCommit prev) {
        period = Optional.ofNullable(prev)
                .map(prevCommit -> new Duration(commitMillis(prevCommit), commitMillis(commit)).toPeriod())
                .orElse(new Duration(TimeUtil.workDayStart.getMillis(), commitMillis(commit)).toPeriod());
    }

    public String periodFormatted() {
        return TimeUtil.formatter.print(period);
    }

    public String getProject() {
        return project;
    }
}
