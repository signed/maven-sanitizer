package com.github.signed.maven.sanitizer.path;

import com.github.signed.maven.model.MavenProjectBuilder;
import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProjectSubdirectory_Test {


    private MavenProjectBuilder mavenProjectBuilder = new MavenProjectBuilder().pomAt(Paths.get("/tmp/pom.xml"));

    @Before
    public void setUp() throws Exception {
        mavenProjectBuilder.withGroupId("org.example").withArtifactId("artifactid");
    }

    @Test
    public void returnEmptyIterableIfNoSubdirectoriesArePassed() throws Exception {
        ProjectSubdirectory projectSubdirectory = new ProjectSubdirectory("org.example", "artifactid");
        Iterable<Path> paths = derivedPaths(projectSubdirectory);
        assertThat(Iterables.size(paths), is(0));
    }

    private Iterable<Path> derivedPaths(ProjectSubdirectory projectSubdirectory) {
        MavenProject mavenProject = mavenProjectBuilder.build();
        return projectSubdirectory.paths(mavenProject);
    }

    @Test
    public void returnOnPathPerSubdirectory() throws Exception {
        ProjectSubdirectory projectSubdirectory = new ProjectSubdirectory("org.example", "artifactid", "subdirectory");
        assertThat(Iterables.size(derivedPaths(projectSubdirectory)), is(1));
    }

    @Test
    public void returnSubdirectoryBelowBaseDirectory() throws Exception {
        ProjectSubdirectory projectSubdirectory = new ProjectSubdirectory("org.example", "artifactid", "subdirectory");
        assertThat(derivedPaths(projectSubdirectory).iterator().next(), is(Paths.get("/tmp/subdirectory")));
    }

    @Test
    public void returnEmptyIterableIfGroupIdOrArtifactIdOfThePassedProjectDoNotMatch() throws Exception {
        ProjectSubdirectory projectSubdirectory = new ProjectSubdirectory("org.example.unexpected", "artifactid", "subdirectory", "another");
        assertThat(Iterables.size(derivedPaths(projectSubdirectory)), is(0));
    }
}
