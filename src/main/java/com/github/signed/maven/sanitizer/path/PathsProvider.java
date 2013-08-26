package com.github.signed.maven.sanitizer.path;

import org.apache.maven.project.MavenProject;

import java.nio.file.Path;

public interface PathsProvider {
    Iterable<Path> paths(MavenProject mavenProject);
}
