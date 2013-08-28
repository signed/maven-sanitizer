package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.FileSystem;
import com.github.signed.maven.sanitizer.SourceToDestinationTreeMapper;

import java.nio.file.Files;
import java.nio.file.Path;

public class CleanRoom {

    private final FileSystem fileSystem;
    private final SourceToDestinationTreeMapper mapper;

    public CleanRoom(FileSystem fileSystem, SourceToDestinationTreeMapper mapper) {
        this.fileSystem = fileSystem;
        this.mapper = mapper;
    }

    public void writeStringToPathAssociatedWith(Path pom, String content) {
        fileSystem.writeStringTo(mapper.map(pom), content);
    }

    public void createDirectoryAssociatedTo(Path baseDir) {
        Path targetBaseDir = mapper.map(baseDir);
        fileSystem.createDirectory(targetBaseDir);
    }

    public void copyContentBelowInAssociatedDirectory(Path source) {
        Path destination = mapper.map(source);
        if (Files.isRegularFile(source)) {
            fileSystem.copyFile(source, destination);
        } else {
            fileSystem.copyDirectoryContentInto(source, destination);
        }
    }
}
