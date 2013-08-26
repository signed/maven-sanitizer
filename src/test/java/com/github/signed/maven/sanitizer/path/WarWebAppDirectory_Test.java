package com.github.signed.maven.sanitizer.path;

import com.github.signed.maven.model.MavenProjectBuilder;
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
    private final Path baseDirectory = Paths.get("/tmp/").toAbsolutePath();

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
        Path absolutePath = projectBaseDirectory("src/main/webContent");
        setWarSourceDirectoryTo(absolutePath);

        assertThat(soleReturnedPath(), is(projectBaseDirectory("src/main/webContent")));
    }

    @Test
    public void resolveRelativePathToAbsolutePath() throws Exception {
        Path relativePath = Paths.get("theWebApplication");
        setWarSourceDirectoryTo(relativePath);

        assertThat(soleReturnedPath(), is(projectBaseDirectory("theWebApplication")));
    }

    @Test
    public void aWarPluginExecutionWithoutConfigurationUsesDefaultWarSourceDirectory() throws Exception {
        apacheMavenWarPluginExecutionWithoutConfiguration();

        assertThat(soleReturnedPath(), is(projectBaseDirectory("src/main/webapp")));
    }

    private void setWarSourceDirectoryTo(Path absolutePath) {
        apacheMavenWarPluginExecutionWithoutConfiguration().withConfiguration().addElement("warSourceDirectory", absolutePath);
    }

    private PluginExecutionBuilder apacheMavenWarPluginExecutionWithoutConfiguration() {
        return projectBuilder.buildSection().addPlugin("org.apache.maven.plugins", "maven-war-plugin").withExecution();
    }

    private Path projectBaseDirectory(String relative) {
        return baseDirectory.resolve(relative);
    }

    private Path soleReturnedPath() {
        return pathsProvider.paths(projectBuilder.build()).iterator().next();
    }
}
