package com.github.nstdio.reporter.gui.service;

import com.github.nstdio.reporter.core.OutputFormat;
import com.github.nstdio.reporter.core.ProjectReport;
import com.github.nstdio.reporter.core.ReportFormatter;
import com.github.nstdio.reporter.core.sender.Credentials;
import com.github.nstdio.reporter.core.sender.EmailSender;
import com.github.nstdio.reporter.gui.App;
import com.github.nstdio.reporter.gui.event.EventBusUtils;
import com.github.nstdio.reporter.gui.event.ReportGeneratedEvent;
import com.github.nstdio.reporter.gui.event.ReportGenerationStartEvent;
import com.github.nstdio.reporter.gui.exception.RequiredConfigMissing;
import com.github.nstdio.reporter.gui.prefs.Key;
import com.github.nstdio.reporter.gui.prefs.Prefs;
import com.google.common.eventbus.EventBus;
import javaslang.control.Try;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportService {
    private final ExecutorService executor;
    private final Prefs prefs = Prefs.defaultPrefs();
    private final EventBus eventBus = App.eventBus();
    private final EmailSender emailSender = new EmailSender();

    public ReportService() {
        this(Executors.newSingleThreadExecutor());
    }

    @SuppressWarnings("WeakerAccess")
    public ReportService(ExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Asynchronously loads today's commits from repositories. Emmit's two type of events on {@link App#eventBus()}
     * event bus. All event will be posted on current running thread, so probably event handler should change context
     * before interacting with UI.
     * <p>
     * 1) {@link ReportGenerationStartEvent} when execution starts.
     * <p>
     * 2) {@link ReportGeneratedEvent} when execution ends.
     */
    public void load() {
        executor.execute(() -> {
            EventBusUtils.reportGenerationStartEvent();

            final List<String> repos = Arrays.asList(prefs.getArray(Key.PROJECT_PATH));
            final String commiter = Try.of(() -> prefs.get(Key.COMMITER).get())
                    .getOrElseThrow(throwable -> {
                        EventBusUtils.reportGeneratedEvent(null);
                        return new RequiredConfigMissing();
                    });

            List<ProjectReport> reports = new ArrayList<>();
            repos.forEach(repo -> reports.add(new ProjectReport(commiter, repo)));

            ReportFormatter formatter = new ReportFormatter(OutputFormat.HTML);

            String report = formatter.today(reports);
            reports.forEach(ProjectReport::dispose);

            EventBusUtils.reportGeneratedEvent(report);
        });
    }

    public void send(final char[] password, String html) {
        executor.execute(() ->
                Try.run(EventBusUtils::sendStartEvent)
                        .andThenTry(() -> sendImpl(password, html))
                        .andThen(EventBusUtils::sentEvent)
                        .onFailure(EventBusUtils::sendFailEvent)
        );
    }

    private void sendImpl(final char[] password, String html) throws Exception {
        //noinspection ConstantConditions
        final String userName = prefs.get(Key.EMAIL_USERNAME).get();
        //noinspection ConstantConditions
        final String from = prefs.get(Key.EMAIL_FROM).get();
        final String[] receipts = prefs.getArray(Key.EMAIL_RECEIPT);

        Credentials credentials = new Credentials(userName, password);

        emailSender.send(credentials, html, from, receipts, OutputFormat.HTML);
    }
}
