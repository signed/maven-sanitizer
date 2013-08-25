package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.List;

public class CopyProject {
    private final FileSystem fileSystem = new FileSystem();
    private final SourceToDestinationTreeMapper mapper;
    private final CopyPom copyPom;

    public CopyProject(SourceToDestinationTreeMapper mapper) {
        this.mapper = mapper;
        copyPom = new CopyPom(fileSystem);
    }

    void copy(MavenProject mavenProject) {
        Path baseDir = mavenProject.getBasedir().toPath();
        ensureTargetBaseDirectoryExists(baseDir);

        copyPom(mavenProject);
        copySourceRoots(mavenProject, baseDir);
        copyResources(mavenProject, baseDir);
    }

    private void ensureTargetBaseDirectoryExists(Path baseDir) {
        Path targetBaseDir = mapper.map(baseDir);
        fileSystem.createDirectory(targetBaseDir);
    }

    private void copyPom(MavenProject mavenProject) {
        Path pom = mavenProject.getFile().toPath();
        Path targetPom = mapper.map(pom);
        copyPom.copyPom(mavenProject, pom, targetPom);
    }

    private void copySourceRoots(MavenProject mavenProject, Path baseDir) {
        List<String> compileSourceRoots = mavenProject.getCompileSourceRoots();
        for (String compileSourceRoot : compileSourceRoots) {
            Path sourceCompileSourceRoot = baseDir.resolve(compileSourceRoot);
            Path targetCompileSourceRoot = mapper.map(sourceCompileSourceRoot);
            fileSystem.copyDirectoryContentInto(sourceCompileSourceRoot, targetCompileSourceRoot);
        }
    }

    private void copyResources(MavenProject mavenProject, Path baseDir) {
        List<Resource> resources = mavenProject.getResources();
        for (Resource resource : resources) {
            Path sourceResource = baseDir.resolve(resource.getDirectory());
            Path targetResource = mapper.map(sourceResource);
            fileSystem.copyDirectoryContentInto(sourceResource, targetResource);
        }
    }
}