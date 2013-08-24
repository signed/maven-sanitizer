package com.github.signed.maven.sanitizer;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyRecursively implements FileVisitor<Path> {
    private final SourceToDestinationTreeMapper mapper;

    public CopyRecursively(Path sourceDirectory, Path destinationDirectory) {
        mapper = new SourceToDestinationTreeMapper(sourceDirectory, destinationDirectory);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path target = mapper.map(dir);
        Files.createDirectories(target);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path targetFile = mapper.map(file);
        Files.copy(file, targetFile);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
