package com.github.signed.maven.model;

import com.google.common.base.Optional;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;

public class MavenProjectBuilder {
    public static MavenProjectBuilder hire() {
        return new MavenProjectBuilder();
    }

    private MavenProject mavenProject = new MavenProject();
    private Optional<BuildBuilder> buildBuilder = Optional.absent();

    public BuildBuilder buildSection() {
        BuildBuilder buildBuilder = BuildBuilder.hire();
        this.buildBuilder = Optional.of(buildBuilder);
        return buildBuilder;
    }

    public MavenProject build() {
        if (buildBuilder.isPresent()) {
            mavenProject.setBuild(buildBuilder.get().build());
        }
        return mavenProject;
    }

    public MavenProjectBuilder packaging(String packaging) {
        mavenProject.setPackaging(packaging);
        return this;
    }

    public MavenProjectBuilder pomAt(Path pomLocation) {
        mavenProject.setFile(pomLocation.toFile());
        return this;
    }
}