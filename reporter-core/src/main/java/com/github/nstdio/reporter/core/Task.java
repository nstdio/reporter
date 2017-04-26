package com.github.nstdio.reporter.core;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.StringUtils;
import org.joda.time.Duration;
import org.joda.time.Period;

import java.util.Optional;

public class Task {
    private final Period period;
    private final RevCommit commit;

    private Task(RevCommit current, RevCommit prev) {
        commit = current;
        period = Optional.ofNullable(prev)
                .map(prevCommit -> new Duration(TimeUtil.commitMillis(prevCommit), TimeUtil.commitMillis(current)).toPeriod())
                .orElse(new Duration(TimeUtil.workDayStart.getMillis(), TimeUtil.commitMillis(current)).toPeriod());
    }

    public static Task from(RevCommit current, RevCommit prev) {
        return new Task(current, prev);
    }

    public static Task from(RevCommit current) {
        return new Task(current, null);
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

    public String periodFormatted() {
        return TimeUtil.formatter.print(period);
    }
}
