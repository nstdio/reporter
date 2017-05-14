package com.github.nstdio.reporter.gui.event;

abstract public class MailEvent {
    public static class SentEvent extends MailEvent {
    }

    public static class SendStartEvent extends MailEvent {

    }

    public static class SendFailedEvent extends MailEvent {
        private final Throwable cause;

        private SendFailedEvent(Throwable cause) {
            this.cause = cause;
        }

        public static SendFailedEvent of(Throwable cause) {
            return new SendFailedEvent(cause);
        }

        public Throwable getCause() {
            return cause;
        }
    }
}
