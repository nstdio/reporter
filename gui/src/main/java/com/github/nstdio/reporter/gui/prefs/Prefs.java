package com.github.nstdio.reporter.gui.prefs;

import com.github.nstdio.reporter.gui.Utils;
import com.github.nstdio.reporter.gui.entity.Repo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Prefs {
    private static final Logger logger = LoggerFactory.getLogger(Prefs.class);
    private static final String DELIM = File.pathSeparator;
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final Preferences preferences;

    private Prefs() {
        preferences = Preferences.userNodeForPackage(getClass());
    }

    public static Prefs defaultPrefs() {
        return Holder.INSTANCE;
    }

    private static Stream<Repo> validRepos(List<Repo> repos) {
        return repos.stream().filter(Repo::isValid);
    }

    public void put(Key key, String value) {
        if (value != null && !value.isEmpty()) {
            preferences.put(key.getKey(), value);
        }
    }

    public void put(List<Repo> repos) {
        final String projects = validRepos(repos)
                .map(Repo::getProject)
                .filter(((Predicate<String>) String::isEmpty).negate())
                .collect(Collectors.joining(DELIM));

        final String path = validRepos(repos)
                .map(Repo::getPath)
                .collect(Collectors.joining(DELIM));

        put(Key.PROJECT_NAME, projects);
        put(Key.PROJECT_PATH, path);
    }

    public Optional<String> get(Key key) {
        return Optional.ofNullable(preferences.get(key.getKey(), null));
    }

    public String[] getArray(Key key) {
        return getArray(key, DELIM);
    }

    private String[] getArray(Key key, String delim) {
        return get(key)
                .map(s -> s.split(delim))
                .orElse(EMPTY_STRING_ARRAY);
    }

    public List<Repo> getRepos() {
        final String[] names = getArray(Key.PROJECT_NAME);
        final String[] paths = getArray(Key.PROJECT_PATH);

        final int count = Math.min(names.length, paths.length);

        List<Repo> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final Optional<File> gitDir = Utils.validGitDir(paths[i]);
            if (gitDir.isPresent()) {
                list.add(new Repo(names[i], gitDir.get().getAbsolutePath(), true));
            } else {
                logger.info("Git directory not valid: {}", paths[i]);
            }
        }

        return list;
    }

    public void putStringList(Key key, List<String> items) {
        put(key, items.stream().collect(Collectors.joining(DELIM)));
    }

    private static class Holder {
        static final Prefs INSTANCE = new Prefs();
    }
}
