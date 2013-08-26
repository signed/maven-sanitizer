package com.github.signed.maven.model;

import com.github.signed.maven.sanitizer.path.PathsProvider;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.Collections;

public class AssemblyDescriptors implements PathsProvider {
    @Override
    public Iterable<Path> paths(MavenProject mavenProject) {
        return Collections.emptyList();
    }
}
