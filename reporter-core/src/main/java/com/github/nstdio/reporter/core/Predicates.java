package com.github.nstdio.reporter.core;

import org.eclipse.jgit.revwalk.RevCommit;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Date;

class Predicates {

    static boolean workweek(RevCommit commit) {
        return TimeUtil.workweekInterval.contains(commit.getCommitTime() * 1000L);
    }

    static boolean interval(RevCommit commit, DateTime start, DateTime end) {
        Interval interval = new Interval(start, end);

        return interval.contains(TimeUtil.commitMillis(commit));
    }

    static boolean authorIndentName(RevCommit commit, String name) {
        return commit.getAuthorIdent().getName().equalsIgnoreCase(name);
    }

    static boolean today(RevCommit commit, String author) {
        return TimeUtil.todayWorkdayInterval.contains(TimeUtil.commitMillis(commit)) && authorIndentName(commit, author);
    }

    static boolean since(RevCommit commit, Date since) {
        return TimeUtil.commitMillis(commit) > since.getTime();
    }
}
