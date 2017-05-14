package com.github.nstdio.reporter.cli;

import com.github.nstdio.reporter.core.ProjectReport;
import com.github.nstdio.reporter.core.ReportFormatter;
import com.github.nstdio.reporter.core.sender.Credentials;
import com.github.nstdio.reporter.core.sender.EmailSender;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        final Config config = ConfigBuilder.config(args);

        final Optional<Config> configOptional = Optional.ofNullable(config);

        final ReportFormatter formatter = configOptional
                .map(input -> new ReportFormatter(input.getOutputFormat()))
                .orElse(null);


        final List<ProjectReport> projectReports = configOptional
                .map(input -> {
                    final List<String> repos = Arrays.asList(input.getRepo());
                    List<ProjectReport> reports = new ArrayList<>();
                    repos.forEach(repo -> reports.add(new ProjectReport(input.getCommiter(), repo)));

                    return reports;
                })
                .orElse(Collections.emptyList());

        configOptional.ifPresent(input -> {
            final String reportBody = formatter.today(projectReports);
            final EmailSender emailSender = new EmailSender();

            char[] password = input.getEmailPassword();

            if (input.getEmailPassword() == null) {
                logger.info("You doest not provide any password in config file.");
                logger.info("Please provide password for {}", input.getEmailUsername());
            }

            if (password == null) {
                logger.info("Please provide password in config file like email.password=<password> and rerun program.");
                logger.info("Exiting...");
                System.exit(-1);
            }

            logger.info("Printing message body.");
            logger.info(reportBody);

            String question = String.format("Send from %s to %s", input.getEmailFrom(), Arrays.toString(input.getEmailTo()));

            Prompt.prompt(question, () -> {
                logger.info("Roger that! Sending email...");

                final Credentials credentials = new Credentials(input.getEmailUsername(), password);
                try {
                    emailSender.send(credentials, reportBody, input.getEmailFrom(), input.getEmailTo(), input.getOutputFormat());
                    logger.info("Ok!");
                    logger.info("Exiting...");
                } catch (EmailException e) {
                    e.printStackTrace();
                }
            }, () -> {
                logger.info("Oh! What a naughty developer.");
                logger.info("Bye!");
            });
        });
    }
}
