package com.github.signed.maven.sanitizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystem {

    public void createDirectory(Path target) {
        try {
            Files.createDirectories(target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyDirectoryContentInto(Path sourceDirectory, Path destinationDirectory) {
        try {
            Files.walkFileTree(sourceDirectory, new CopyRecursively(sourceDirectory, destinationDirectory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}