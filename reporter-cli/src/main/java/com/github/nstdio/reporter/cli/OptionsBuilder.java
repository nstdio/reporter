package com.github.nstdio.reporter.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.File;
import java.io.FileInputStream;

import static org.apache.commons.cli.Option.UNLIMITED_VALUES;

class OptionsBuilder {
    static Options options() {
        Option config = Option.builder("C")
                .hasArg()
                .argName("path to config")
                .longOpt("config")
                .desc("the config .properties file. If not specified -c, -r arguments required.")
                .numberOfArgs(1)
                .type(FileInputStream.class)
                .build();

        Option commiter = Option.builder("c")
                .hasArg()
                .longOpt("commiter")
                .desc("the name of the commiter")
                .build();

        Option email = Option.builder("e")
                .hasArg()
                .longOpt("commiter-email")
                .desc("the email of the commiter")
                .build();

        Option repo = Option.builder("r")
                .hasArg()
                .longOpt("repo")
                .argName("path")
                .desc("the local repository from witch report will be generated.")
                .valueSeparator(File.pathSeparator.charAt(0))
                .optionalArg(false)
                .numberOfArgs(UNLIMITED_VALUES)
                .build();

        Option outputFormat = Option.builder("f")
                .hasArg()
                .longOpt("output-format")
                .desc("the output format. Possible values [text, html]")
                .required(false)
                .numberOfArgs(1)
                .build();

        Option help = Option.builder("h")
                .desc("print this message and exit")
                .longOpt("help")
                .build();

        Option version = Option.builder("v")
                .desc("print the version information and exit")
                .longOpt("version")
                .build();

        Option quiet = new Option("quiet", "be extra quiet");
        Option verbose = new Option("verbose", "be extra verbose");
        Option debug = new Option("debug", "print debugging information");

        Options options = new Options();

        options
                .addOption(config)
                .addOption(commiter)
                .addOption(email)
                .addOption(repo)
                .addOption(outputFormat)
                .addOption(help)
                .addOption(version)
                .addOption(quiet)
                .addOption(verbose)
                .addOption(debug);

        return options;
    }
}
