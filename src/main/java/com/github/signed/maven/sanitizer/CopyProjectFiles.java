package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.PathsProvider;
import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CopyProjectFiles {
    private final CleanRoom cleanRoom;
    private final Collection<PathsProvider> pathsProviders = new ArrayList<>();

    public CopyProjectFiles(CleanRoom cleanRoom) {
        this.cleanRoom = cleanRoom;
    }

    public void addPathsToCopy(PathsProvider provider) {
        this.pathsProviders.add(provider);
    }

    void copy(MavenProject mavenProject) {
        copyToCleanRoom(allPathsCollectedFrom(mavenProject));
    }

    private Set<Path> allPathsCollectedFrom(MavenProject mavenProject) {
        Set<Path> pathsToCopy = new HashSet<>();
        for (PathsProvider pathsProvider : pathsProviders) {
            Iterables.addAll(pathsToCopy, pathsProvider.paths(mavenProject));
        }
        return pathsToCopy;
    }


    private void copyToCleanRoom(Iterable<Path> sourceDirectoryToCopy) {
        for (Path path : sourceDirectoryToCopy) {
            cleanRoom.copyContentBelowInAssociatedDirectory(path);
        }
    }
}