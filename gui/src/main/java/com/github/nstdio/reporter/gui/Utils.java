package com.github.nstdio.reporter.gui;

import com.github.nstdio.reporter.core.FilenameUtils;

import java.io.File;
import java.util.Optional;

public final class Utils {

    private static final String GIT_DIR_NAME = ".git";

    public static Optional<File> validGitDir(File file) {
        if (file == null) {
            return Optional.empty();
        }

        if (check(file)) {
            return Optional.of(file);
        }

        return Optional.ofNullable(file.listFiles(Utils::check))
                .filter(files -> files.length > 0)
                .map(files -> files[0]);
    }

    public static Optional<File> validGitDir(String pathname) {
        return validGitDir(new File(pathname));
    }

    private static boolean check(File file) {
        return file.isDirectory() && file.getName().equals(GIT_DIR_NAME);
    }

    public static String projectName(File file) {
        return FilenameUtils.projectName(file.getAbsolutePath());
    }
}
