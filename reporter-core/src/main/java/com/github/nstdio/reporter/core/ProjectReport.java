package com.github.nstdio.reporter.core;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectReport {
    private final static Logger logger = LoggerFactory.getLogger(ProjectReport.class);

    private final GitFacade git;
    private final String author;
    private final String project;

    public ProjectReport(String author, String repoPath) {
        this(author, repoPath, null);
    }

    public ProjectReport(String author, String repoPath, String projectName) {
        git = new GitFacade(repoPath);
        this.author = author;

        if (StringUtils.isEmpty(projectName)) {
            logger.info("Inferring project name from path: {} ", repoPath);
            project = FilenameUtils.projectName(repoPath);
            logger.info("Project name is determined: {}", project);
        } else {
            project = projectName;
        }
    }

    public String getProjectName() {
        return project;
    }

    public List<RevCommit> todayCommits() {
        return git.todayCommits(author);
    }

    public List<Task> today() {
        final List<RevCommit> commits = todayCommits();

        if (commits.size() == 0) {
            logger.info("You have no commits for project {}.", project);
            return Collections.emptyList();
        }

        List<Task> tasks = new ArrayList<>();

        commits.forEach(commit -> tasks.add(Task.from(project, commit)));

        return tasks;
    }

    public void dispose() {
        git.close();
        logger.info("{} disposed", project);
    }
}
