package com.github.signed.maven.sanitizer.path;

import com.github.signed.maven.model.MavenProjectBuilder;
import com.github.signed.maven.model.PluginExecutionBuilder;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PathsInPluginConfiguration_Test {

    private final PathsInPluginConfiguration pathsProvider = new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-war-plugin", Collections.singletonList(Paths.get("src/main/webapp")), "warSourceDirectory"));
    private final MavenProjectBuilder projectBuilder = MavenProjectBuilder.hire();
    private final Path baseDirectory = Paths.get("/tmp/").toAbsolutePath();

    @Before
    public void setBaseDirectoryOnMavenProject() throws Exception {
        projectBuilder.pomAt(baseDirectory.resolve("pom.xml"));
    }

    @Test
    public void aWarPluginExecutionWithoutConfigurationUsesDefaultWarSourceDirectory() throws Exception {
        apacheMavenWarPluginExecutionWithoutConfiguration();

        assertThat(soleReturnedPath(), is(projectBaseDirectory("src/main/webapp")));
    }

    @Test
    public void noPathsIfNoWarPluginExecutionIsConfigured() throws Exception {
        assertThat(pathsProvider.paths(projectBuilder.build()), Matchers.<Path>iterableWithSize(0));
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
