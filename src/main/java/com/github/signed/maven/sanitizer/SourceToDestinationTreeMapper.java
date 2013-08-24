package com.github.signed.maven.sanitizer;

import java.nio.file.Path;

public class SourceToDestinationTreeMapper {
    private final Path sourceTree;
    private final Path destinationTree;

    public SourceToDestinationTreeMapper(Path sourceTree, Path destinationTree) {
        this.sourceTree = sourceTree;
        this.destinationTree = destinationTree;
    }

    public Path map(Path belowSourceTree) {
        Path relativize = sourceTree.relativize(belowSourceTree);
        return destinationTree.resolve(relativize);
    }
}