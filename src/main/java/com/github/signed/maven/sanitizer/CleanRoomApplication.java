package com.github.signed.maven.sanitizer;

import com.google.common.collect.Sets;

import java.nio.file.Path;
import java.util.Set;

public class CleanRoomApplication {
    private final Set<Path> pathsToCopy = Sets.newHashSet();

    public void addAll(Set<Path> from) {
        pathsToCopy.addAll(from);
    }

    public Iterable<Path> pathsToCopy() {
        return pathsToCopy;
    }
}
