package com.github.nstdio.reporter.core;

import org.apache.commons.lang3.text.WordUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilenameUtils {

    public static String projectName(String pathToGitRepo) {
        final Path path = Paths.get(pathToGitRepo).normalize();
        String project = path.getName(path.getNameCount() - 2).toString();

        return WordUtils.capitalizeFully(project, new char[]{'_', '-'})
                .replaceAll("[-_]", " ");
    }
}
