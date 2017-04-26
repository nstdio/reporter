package com.github.nstdio.reporter.core.sender;

public class Credentials {
    private final String userName;
    private final char[] password;

    public Credentials(String userName, char[] password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public char[] getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "userName='" + userName + '\'' +
                ", password= ***}";
    }
}
