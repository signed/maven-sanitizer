package com.github.signed.maven.sanitizer.path;

import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static com.github.signed.maven.sanitizer.path.BasePath.baseDirectoryOf;

public class SourceRoots implements PathsProvider {
    @Override
    public Iterable<Path> paths(MavenProject mavenProject) {
        Set<Path> sourceRootsToCopy = new HashSet<>();
        for (String compileSourceRoot : mavenProject.getCompileSourceRoots()) {
            Path sourceCompileSourceRoot = baseDirectoryOf(mavenProject).resolve(compileSourceRoot);
            sourceRootsToCopy.add(sourceCompileSourceRoot);
        }
        return sourceRootsToCopy;
    }
}
