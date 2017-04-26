package com.github.nstdio.reporter.core.sender;

import javax.mail.Authenticator;

public interface ConfigurableMail {
    String hostName();

    Authenticator authenticator(Credentials credentials);

    String sslSmtpPort();
}
