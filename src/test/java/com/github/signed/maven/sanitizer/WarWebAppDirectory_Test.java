package com.github.signed.maven.sanitizer;

import com.github.signed.maven.model.BuildBuilder;
import com.github.signed.maven.model.MavenProjectBuilder;
import com.github.signed.maven.model.PluginBuilder;
import com.github.signed.maven.model.PluginExecutionBuilder;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WarWebAppDirectory_Test {

    private final WarWebAppDirectory pathsProvider = new WarWebAppDirectory();
    private final MavenProjectBuilder projectBuilder = MavenProjectBuilder.hire();
    private final Path baseDirectory = Paths.get("").toAbsolutePath();

    @Before
    public void setBaseDirectoryOnMavenProject() throws Exception {
        projectBuilder.packaging("war").pomAt(baseDirectory.resolve("pom.xml"));
    }

    @Test
    public void noPathsIfPackagingTypeIsNotWar() throws Exception {
        projectBuilder.packaging("jar");
        assertThat(pathsProvider.paths(projectBuilder.build()), Matchers.<Path>iterableWithSize(0));
    }

    @Test
    public void defaultPathIfThereIsNoExplicitConfiguration() throws Exception {
        assertThat(soleReturnedPath(), is(projectBaseDirectory("src/main/webapp")));
    }

    @Test
    public void explicitlyConfiguredPathIfPresent() throws Exception {
        BuildBuilder buildBuilder = projectBuilder.withBuildSection();
        PluginBuilder pluginBuilder = buildBuilder.addPlugin("org.apache.maven.plugins", "maven-war-plugin");
        PluginExecutionBuilder pluginExecutionBuilder = pluginBuilder.withExecution();
        pluginExecutionBuilder.withConfiguration().addElement("warSourceDirectory", projectBaseDirectory("src/main/webContent").toString());

        assertThat(soleReturnedPath(), is(projectBaseDirectory("src/main/webContent")));
    }

    private Path projectBaseDirectory(String relative) {
        return baseDirectory.resolve(relative);
    }

    private Path soleReturnedPath() {
        return pathsProvider.paths(projectBuilder.build()).iterator().next();
    }
}
