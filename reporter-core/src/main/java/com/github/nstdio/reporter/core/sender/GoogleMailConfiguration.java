package com.github.nstdio.reporter.core.sender;

import org.apache.commons.mail.DefaultAuthenticator;

import javax.mail.Authenticator;

class GoogleMailConfiguration implements ConfigurableMail {

    @Override
    public String hostName() {
        return "smtp.gmail.com";
    }

    @Override
    public Authenticator authenticator(Credentials credentials) {
        return new DefaultAuthenticator(credentials.getUserName(), new String(credentials.getPassword()));
    }

    @Override
    public String sslSmtpPort() {
        return "465";
    }
}
