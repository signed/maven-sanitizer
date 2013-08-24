package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.List;

public class CopyProject {
    private final FileSystem fileSystem = new FileSystem();
    private final SourceToDestinationTreeMapper application;

    public CopyProject(SourceToDestinationTreeMapper application) {
        this.application = application;
    }

    void copy(MavenProject mavenProject) {
        Path baseDir = mavenProject.getBasedir().toPath();
        Path targetBaseDir = application.map(baseDir);
        fileSystem.createDirectory(targetBaseDir);

        Path pom = mavenProject.getFile().toPath();
        Path targetPom = application.map(pom);
        fileSystem.copyDirectoryContentInto(pom, targetPom);

        List<String> compileSourceRoots = mavenProject.getCompileSourceRoots();
        for (String compileSourceRoot : compileSourceRoots) {
            Path sourceCompileSourceRoot = baseDir.resolve(compileSourceRoot);
            Path targetCompileSourceRoot = application.map(sourceCompileSourceRoot);
            fileSystem.copyDirectoryContentInto(sourceCompileSourceRoot, targetCompileSourceRoot);
        }

        List<Resource> resources = mavenProject.getResources();
        for (Resource resource : resources) {
            Path sourceResource = baseDir.resolve(resource.getDirectory());
            Path targetResource = application.map(sourceResource);
            fileSystem.copyDirectoryContentInto(sourceResource, targetResource);
        }
    }
}