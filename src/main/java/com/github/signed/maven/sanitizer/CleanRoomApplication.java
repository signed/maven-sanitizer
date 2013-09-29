package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.SanitizedPom;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class CleanRoomApplication {
    private final Set<Path> pathsToCopy = Sets.newHashSet();
    private final List<SanitizedPom> sanitizedPoms = Lists.newArrayList();

    public void addAll(Set<Path> from) {
        pathsToCopy.addAll(from);
    }

    public Iterable<Path> pathsToCopy() {
        return pathsToCopy;
    }

    public void add(SanitizedPom sanitizedPom) {
        sanitizedPoms.add(sanitizedPom);
    }

    public Iterable<SanitizedPom> sanitizedPoms(){
        return sanitizedPoms;
    }
}
