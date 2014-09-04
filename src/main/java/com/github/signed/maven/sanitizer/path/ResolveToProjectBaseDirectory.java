package com.github.signed.maven.sanitizer.path;

import java.nio.file.Path;

import org.apache.maven.project.MavenProject;
import com.google.common.base.Function;

public class ResolveToProjectBaseDirectory implements Function<Path, Path> {
    private final MavenProject mavenProject;

    public ResolveToProjectBaseDirectory(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    @Override
    public Path apply(Path input) {
        return mavenProject.getBasedir().toPath().resolve(input);
    }
}
