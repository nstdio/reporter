package com.github.nstdio.reporter.core;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProjectReport {
    private final static Logger logger = LoggerFactory.getLogger(ProjectReport.class);

    private final GitFacade git;
    private final String path;
    private final String author;
    private String project;

    public ProjectReport(String author, String repoPath) {
        git = new GitFacade(repoPath);
        this.author = author;
        path = repoPath;

        determineProjectName();
    }

    private String projectName() {
        final Path path = Paths.get(this.path).normalize();
        String project = path.getName(path.getNameCount() - 2).toString();

        return WordUtils.capitalizeFully(project, new char[]{'_', '-'})
                .replaceAll("[-_]", " ");
    }

    private void determineProjectName() {
        logger.info("Inferring project name from path: {} ", path);

        project = projectName();

        logger.info("Project name is determined: {}", project);
    }

    public String getProjectName() {
        return project;
    }

    public void setProjectName(String projectName) {
        project = projectName;
    }

    public List<RevCommit> todayCommits() {
        return git.todayCommits(author);
    }

    public List<Task> today() {
        final List<RevCommit> commits = todayCommits();

        List<Task> tasks = new ArrayList<>();
        if (commits.size() == 0) {
            logger.info("You have no commits for project {}.", project);
            return tasks;
        }

        tasks.add(Task.from(commits.get(0)));

        for (int i = 1, n = commits.size(); i < n; i++) {
            final Task from = Task.from(commits.get(i), commits.get(i - 1));
            tasks.add(from);

        }

        return tasks;
    }

    public void dispose() {
        git.close();
    }
}
