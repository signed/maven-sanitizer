package com.github.signed.maven.sanitizer.path;

import org.apache.maven.project.MavenProject;

import java.nio.file.Path;

public class BasePath {

    public static Path baseDirectoryOf(MavenProject mavenProject) {
        return mavenProject.getBasedir().toPath();
    }
}
