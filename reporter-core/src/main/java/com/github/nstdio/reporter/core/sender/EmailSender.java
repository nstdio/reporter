package com.github.nstdio.reporter.core.sender;

import com.github.nstdio.reporter.core.OutputFormat;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailSender {

    public void send(Credentials credential, String message, String from, String[] to, OutputFormat format) {
        try {

            Email email = email(format);
            ConfigurableMail configurableMail = configurableMail(from);

            email.setHostName(configurableMail.hostName());
            email.setSslSmtpPort(configurableMail.sslSmtpPort());
            email.setAuthenticator(configurableMail.authenticator(credential));

            email.setSSLOnConnect(true);
            email.setStartTLSEnabled(true);
            email.setFrom(from);
            email.setSubject(subject());
            email.setCharset("UTF-8");
            email.addTo(to);

            if (email instanceof HtmlEmail) {
                ((HtmlEmail) email).setHtmlMsg(message);
            } else {
                email.setMsg(message);
            }

            email.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String subject() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

        return String.format("Report - %s", dateFormatter.format(new Date()));
    }

    private Email email(OutputFormat format) {
        switch (format) {
            case HTML:
                return new HtmlEmail();
            case TEXT:
                return new SimpleEmail();
            default:
                return new SimpleEmail();
        }
    }

    private ConfigurableMail configurableMail(String email) {
        if (email.endsWith("@gmail.com")) {
            return new GoogleMailConfiguration();
        }

        return new GoogleMailConfiguration();
    }
}
