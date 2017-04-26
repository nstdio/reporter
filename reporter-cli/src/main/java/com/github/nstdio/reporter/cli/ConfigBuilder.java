package com.github.nstdio.reporter.cli;

import com.github.nstdio.reporter.core.OutputFormat;
import javaslang.control.Try;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;
import java.util.Properties;

class ConfigBuilder {
    private static final HelpFormatter FORMATTER = new HelpFormatter();

    static Config config(final String[] cliArgs) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        final Options options = OptionsBuilder.options();

        CommandLine commandLine = parser.parse(options, cliArgs);

        if (commandLine.hasOption('h')) {
            help(options);
            System.exit(0);
        }

        if (commandLine.hasOption('v')) {
            printPackageInfo();
            System.exit(0);
        }

        final Config config = Optional.ofNullable(commandLine.getParsedOptionValue("C"))
                .filter(o -> (o instanceof File))
                .map(o -> (File) o)
                .map(ConfigBuilder::fromFile)
                .orElse(fromCommandLine(commandLine));

        if (!checkErrors(config, options)) {
            return null;
        }

        return config;
    }

    private static void printPackageInfo() {
        Manifests manifests = new Manifests();

        System.out.printf("%s v%s using reporter-core v0.0.1\nCreated-By: %s\nBuild-Jdk: %s",
                manifests.getAttr("Implementation-Title"), manifests.getAttr("Implementation-Version"),
                manifests.getAttr("Created-By"), manifests.getAttr("Build-Jdk")
        );
    }

    private static boolean checkErrors(Config config, Options options) {
        OptionsChecker checker = new OptionsChecker();
        final String result = checker.check(config, options);

        if (!result.isEmpty()) {
            help(options, result);
            return false;
        }

        return true;
    }

    private static void help(Options options) {
        FORMATTER.printHelp("reporter-cli", options, true);
    }

    private static void help(Options options, String message) {
        FORMATTER.printHelp("reporter-cli", String.format("\nError: %s\n\n", message), options, "", true);
    }

    private static Config fromFile(File configFile) {
        Properties properties = new Properties();

        Try.of(() -> new FileInputStream(configFile))
                .andThenTry(properties::load)
                .getOrElseThrow(() -> new IllegalArgumentException("Config file not found!"));

        final Config config = new Config();

        config.setCommiter(properties.getProperty("commiter"));
        config.setEmail(properties.getProperty("email"));
        config.setRepo(values(properties.getProperty("repo")));
        config.setOutputFormat(from(properties.getProperty("output-format")));
        config.setEmailUsername(properties.getProperty("email.username"));
        config.setEmailFrom(properties.getProperty("email.from"));

        final char[] password = Optional.ofNullable(properties.getProperty("email.password"))
                .map(String::toCharArray)
                .orElse(null);

        config.setEmailPassword(password);

        String[] receipts = values(properties.getProperty("email.to"));

        config.setEmailTo(receipts);

        return config;
    }

    private static String[] values(String values) {
        return Optional.ofNullable(values)
                .map(s -> s.split(File.pathSeparator))
                .orElse(null);
    }

    private static OutputFormat from(String format) {
        if (format == null) {
            return OutputFormat.TEXT;
        }

        OutputFormat outputFormat = OutputFormat.from(format);

        if (outputFormat == null) {
            throw new IllegalArgumentException("Unknown output format: " + format);
        }

        return outputFormat;
    }

    private static Config fromCommandLine(CommandLine commandLine) {
        String commiter = commandLine.getOptionValue('c');
        String[] repos = commandLine.getOptionValues('r');
        String email = commandLine.getOptionValue('e');
        String outputFormat = commandLine.getOptionValue('f');

        final Config config = new Config();

        config.setCommiter(commiter);
        config.setRepo(repos);
        config.setEmail(email);
        config.setOutputFormat(from(outputFormat));

        return config;
    }
}
