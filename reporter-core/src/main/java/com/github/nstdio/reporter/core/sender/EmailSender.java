package com.github.nstdio.reporter.core.sender;

import com.github.nstdio.reporter.core.OutputFormat;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

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
            email.setSubject("TestMail");
            email.setMsg(message);

            email.addTo(to);

            email.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
