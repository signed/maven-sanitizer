package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WarWebAppDirectory_Test {

    private final WarWebAppDirectory pathsProvider = new WarWebAppDirectory();
    private final MavenProject project = new MavenProject();
    private final Path baseDirectory = Paths.get("").toAbsolutePath();

    @Before
    public void setBaseDirectoryOnMavenProject() throws Exception {
        project.setPackaging("war");
        project.setFile(baseDirectory.resolve("pom.xml").toFile());
    }

    @Test
    public void noPathsIfPackagingTypeIsNotWar() throws Exception {
        project.setPackaging("jar");
        assertThat(pathsProvider.paths(project), Matchers.<Path>iterableWithSize(0));
    }

    @Test
    public void defaultPathIfThereIsNoExplicitConfiguration() throws Exception {
        assertThat(soleReturnedPath(), is(projectBaseDirectory("src/main/webapp")));
    }

    @Test
    public void explicitlyConfiguredPathIfPresent() throws Exception {
        Build build = project.getBuild();
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setArtifactId("maven-war-plugin");
        build.addPlugin(plugin);

        Xpp3Dom warSourceDirectory = new ConfigurationBuilder().add("warSourceDirectory", projectBaseDirectory("src/main/webContent").toString()).toConfiguration();
        PluginExecution execution = new PluginExecution();
        execution.setConfiguration(warSourceDirectory);
        List<PluginExecution> executions = plugin.getExecutions();
        executions.add(execution);

        assertThat(soleReturnedPath(), is(projectBaseDirectory("src/main/webContent")));
    }

    private Path projectBaseDirectory(String relative) {
        return baseDirectory.resolve(relative);
    }

    private Path soleReturnedPath() {
        return pathsProvider.paths(project).iterator().next();
    }
}
