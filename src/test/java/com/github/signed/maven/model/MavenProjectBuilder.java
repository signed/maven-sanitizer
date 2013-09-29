package com.github.signed.maven.model;

import com.google.common.base.Optional;
import org.apache.maven.project.MavenProject;

import java.io.File;
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

    public MavenProjectBuilder packaging(String packaging) {
        mavenProject.setPackaging(packaging);
        return this;
    }

    public MavenProjectBuilder pomAt(Path pomLocation) {
        return pomAt(pomLocation.toFile());
    }

    public MavenProjectBuilder pomAt(File pomLocation) {
        mavenProject.setFile(pomLocation);
        return this;
    }

    public MavenProjectBuilder withGroupId(String groupId) {
        mavenProject.setGroupId(groupId);
        return this;
    }

    public MavenProjectBuilder withModule(String moduleName) {
        mavenProject.getModules().add(moduleName);
        return this;
    }

    public MavenProjectBuilder withArtifactId(String artifact) {
        mavenProject.setArtifactId(artifact);
        return this;
    }

    public MavenProject build() {
        if (buildBuilder.isPresent()) {
            mavenProject.setBuild(buildBuilder.get().build());
        }
        return mavenProject;
    }
}
