package com.github.nstdio.reporter.core;

import javaslang.control.Try;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings({"unused", "WeakerAccess", "SameParameterValue"})
public class GitFacade {

    private final Git git;
    private final Repository repository;

    public GitFacade(final String repoPath) {
        repository = Try.of(() -> new FileRepositoryBuilder()
                .setGitDir(new File(repoPath))
                .build())
                .getOrElseThrow(() -> new IllegalArgumentException("Cannot open repository at path: " + repoPath));

        git = Try.of(() -> new Git(repository))
                .getOrElse(() -> null);
    }

    public List<Ref> branches() {
        return Try.of(() -> git.branchList().call())
                .getOrElseThrow(() -> new IllegalStateException("Cannot get branch list."));
    }

    public Stream<Ref> branchesAsStream() {
        return Try.of(() -> git.branchList().call().stream())
                .getOrElse(Stream::empty);
    }

    public List<RevCommit> commits(String author) {
        return commitsAsStream()
                .filter(commit -> Predicates.authorIndentName(commit, author))
                .collect(Collectors.toList());
    }

    public List<RevCommit> commits() {
        return commitsAsStream()
                .sorted(Comparator.comparingInt(RevCommit::getCommitTime))
                .collect(Collectors.toList());
    }

    public List<RevCommit> commits(Date since) {
        return commitsAsStream()
                .filter(revCommit -> Predicates.since(revCommit, since))
                .collect(Collectors.toList());
    }

    public List<RevCommit> commitsWorkweek() {
        return commitWorkweekAsStream()
                .collect(Collectors.toList());
    }

    public List<RevCommit> commitsWorkweek(String author) {
        return commitWorkweekAsStream()
                .filter(commit -> Predicates.authorIndentName(commit, author))
                .collect(Collectors.toList());
    }

    public List<RevCommit> todayCommits(String author) {
        return todayCommitsAsStream(author)
                .sorted(Comparator.comparingInt(RevCommit::getCommitTime))
                .collect(Collectors.toList());
    }

    public Stream<RevCommit> todayCommitsAsStream(String author) {
        return commitsAsStream()
                .filter(commit -> Predicates.today(commit, author));
    }

    private Stream<RevCommit> commitWorkweekAsStream() {
        return commitsAsStream()
                .filter(Predicates::workweek);
    }

    private Stream<RevCommit> commitsAsStream(ObjectId objectId) {
        return Try.of(() -> {
            final Iterable<RevCommit> call = git.log().add(objectId).call();
            return StreamSupport.stream(call.spliterator(), false);
        }).getOrElseThrow((Supplier<IllegalArgumentException>) IllegalArgumentException::new);
    }

    private Stream<RevCommit> commitsAsStream() {
        return branchesAsStream()
                .map(ref -> resolve(ref.getName()))
                .flatMap(this::commitsAsStream);
    }

    private ObjectId resolve(String branchName) {
        return Try.of(() -> repository.resolve(branchName))
                .getOrElseThrow((Supplier<IllegalStateException>) IllegalStateException::new);
    }

    public void close() {
        git.close();
    }
}
