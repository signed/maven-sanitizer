package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.PathsProvider;
import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectPathsToCopy {
    private final Collection<PathsProvider> pathsProviders = new ArrayList<>();

    public void addPathsToCopy(PathsProvider provider) {
        this.pathsProviders.add(provider);
    }

    public Set<Path> from(MavenProject mavenProject) {
        Set<Path> pathsToCopy = new HashSet<>();
        for (PathsProvider pathsProvider : pathsProviders) {
            Iterables.addAll(pathsToCopy, pathsProvider.paths(mavenProject));
        }
        return pathsToCopy;
    }
}