package com.github.nstdio.reporter.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

class OptionsChecker {

    String check(Config config, Options options) {
        StringBuilder sb = new StringBuilder();

        if (config.getCommiter() == null) {
            sb.append(requiredError(options.getOption("c")));
        }

        if (config.getRepo() == null) {
            sb.append(requiredError(options.getOption("r")));
        }

        return sb.toString();
    }

    private String requiredError(Option option) {
        return String.format("\n-%s or %s in config file muse be specified.", option.getOpt(), option.getLongOpt());
    }
}
