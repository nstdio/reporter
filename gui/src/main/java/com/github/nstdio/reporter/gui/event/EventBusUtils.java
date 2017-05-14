package com.github.nstdio.reporter.gui.event;

import com.github.nstdio.reporter.gui.App;
import com.google.common.eventbus.EventBus;

public class EventBusUtils {
    private static final EventBus BUS = App.eventBus();

    private EventBusUtils() {
    }

    public static void sendStartEvent() {
        BUS.post(new MailEvent.SendStartEvent());
    }

    public static void sentEvent() {
        BUS.post(new MailEvent.SentEvent());
    }

    public static void sendFailEvent(Throwable throwable) {
        BUS.post(MailEvent.SendFailedEvent.of(throwable));
    }

    public static void reportGeneratedEvent(String report) {
        BUS.post(ReportGeneratedEvent.of(report));
    }

    public static void reportGenerationStartEvent() {
        BUS.post(new ReportGenerationStartEvent());
    }
}
