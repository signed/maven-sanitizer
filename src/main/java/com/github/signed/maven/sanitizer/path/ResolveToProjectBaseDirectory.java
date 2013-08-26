package com.github.signed.maven.sanitizer.path;

import com.google.common.base.Function;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nullable;
import java.nio.file.Path;

public class ResolveToProjectBaseDirectory implements Function<Path, Path> {
    private final MavenProject mavenProject;

    public ResolveToProjectBaseDirectory(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    @Override
    public Path apply(@Nullable Path input) {
        return mavenProject.getBasedir().toPath().resolve(input);
    }
}
