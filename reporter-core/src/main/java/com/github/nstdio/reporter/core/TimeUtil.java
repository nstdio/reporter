package com.github.nstdio.reporter.core;

import org.eclipse.jgit.revwalk.RevCommit;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import static org.joda.time.DateTimeConstants.FRIDAY;
import static org.joda.time.DateTimeConstants.MONDAY;

class TimeUtil {
    final static LocalDate now = new LocalDate();
    final static LocalDate monday = now.withDayOfWeek(MONDAY);
    final static LocalDate friday = now.withDayOfWeek(FRIDAY);
    final static DateTime mondayStartOfDay = monday.toDateTimeAtStartOfDay();
    final static DateTime fridayEndOfDay = friday.toDateTimeAtStartOfDay()
            .withHourOfDay(23)
            .withMinuteOfHour(59)
            .withSecondOfMinute(59);

    final static DateTime workDayStart = now.toDateTimeAtStartOfDay()
            .withHourOfDay(9);

    final static DateTime workDayEnd = now.toDateTimeAtStartOfDay()
            .withHourOfDay(23)
            .withMinuteOfHour(59);

    final static Interval workweekInterval = new Interval(mondayStartOfDay, fridayEndOfDay);
    final static Interval todayWorkdayInterval = new Interval(workDayStart, workDayEnd);

    final static PeriodFormatter formatter = new PeriodFormatterBuilder()
            .appendLiteral("(")
            .appendHours()
            .appendSuffix(" hour, ", " hours, ")
            .appendMinutes()
            .appendSuffix(" minute", " minutes")
            .appendLiteral(")")
            .toFormatter();

    static long commitMillis(RevCommit commit) {
        return commit.getCommitTime() * 1000L;
    }
}
