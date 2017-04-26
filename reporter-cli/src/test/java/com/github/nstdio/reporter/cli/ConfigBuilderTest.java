package com.github.nstdio.reporter.cli;

import com.github.nstdio.reporter.core.OutputFormat;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;

public class ConfigBuilderTest {

    @Test
    public void noArgs() throws Exception {
        final Config config = ConfigBuilder.config(new String[0]);

        assertNull(config);
    }

    @Test
    public void invalidConfig() throws Exception {
        String[] args = {"-C", "not_found"};

        try {
            ConfigBuilder.config(args);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
            assertEquals(e.getMessage(), "Config file not found!");
        }
    }

    @Test
    public void validArgs() throws Exception {
        final String commiter = "Edgar Asatryan";
        final String[] repos = {"path_to_repo1", "path_to_repo2"};

        final String reposClArg = repos[0] + File.pathSeparator + repos[1];

        String[] args = {"-c", commiter, "-r", reposClArg};

        final Config config = ConfigBuilder.config(args);

        assertNotNull(config);
        assertEquals(commiter, config.getCommiter());
        assertArrayEquals(repos, config.getRepo());
        assertEquals(OutputFormat.TEXT, config.getOutputFormat());
        assertNull(config.getEmail());
    }

    @Test
    public void validConfig() throws Exception {
        final URL resource = resource("config.properties");

        final String[] args = {"-C", resource.getPath()};

        final Config config = ConfigBuilder.config(args);

        assertNotNull(config);
        assertEquals("Edgar Asatryan", config.getCommiter());
        assertArrayEquals(new String[]{"/path/to/repo/.git", "/path/to/repo2/.git"}, config.getRepo());
        assertEquals(OutputFormat.HTML, config.getOutputFormat());
        assertNull(config.getEmail());
    }

    @Test
    public void emptyConfig() throws Exception {
        URL configRes = resource("empty-config.properties");
        final String[] args = {"-C", configRes.getPath()};

        final Config config = ConfigBuilder.config(args);

        assertNull(config);
    }

    private URL resource(String name) {
        final URL resource = Thread.currentThread().getContextClassLoader().getResource(name);
        assumeNotNull(resource);

        return resource;
    }
}