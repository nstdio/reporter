package com.github.nstdio.reporter.cli;

import com.github.nstdio.reporter.core.OutputFormat;

class Config {

    private String commiter;
    private String email;
    private String[] repo;
    private OutputFormat outputFormat;
    private EmailConfig emailConfig = new EmailConfig();

    public String getCommiter() {
        return commiter;
    }

    public void setCommiter(String commiter) {
        this.commiter = commiter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getRepo() {
        return repo;
    }

    public void setRepo(String[] repo) {
        this.repo = repo;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getEmailFrom() {
        return emailConfig.from;
    }

    public void setEmailFrom(String from) {
        emailConfig.from = from;
    }

    public String[] getEmailTo() {
        return emailConfig.to;
    }

    public void setEmailTo(String[] to) {
        emailConfig.to = to;
    }

    public String getEmailUsername() {
        return emailConfig.username;
    }

    public void setEmailUsername(String username) {
        emailConfig.username = username;
    }

    public char[] getEmailPassword() {
        return emailConfig.password;
    }

    public void setEmailPassword(char[] password) {
        emailConfig.password = password;
    }

    private static class EmailConfig {
        private String username;
        private String from;
        private char[] password;
        private String[] to;
    }
}
