package com.github.signed.maven.sanitizer;

import com.github.signed.matcher.file.IsFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;

public class CopyRecursively_Test {

    @Rule
    public TemporaryFolder source = new TemporaryFolder();

    @Rule
    public TemporaryFolder destination = new TemporaryFolder();

    @Test
    public void ensureParentDirectoryExistsBeforeCopying() throws Exception {
        Path sourceDirectory = source.getRoot().toPath();
        Path theFile = sourceDirectory.resolve("deep/folder/structure/file.txt");
        Files.createDirectories(theFile.getParent());
        Path toCopy = Files.createFile(theFile);
        Path destinationDirectory = destination.getRoot().toPath();

        new CopyRecursively(sourceDirectory, destinationDirectory).visitFile(toCopy, null);
        assertThat(destinationDirectory.resolve("deep/folder/structure/file.txt").toFile(), IsFile.isAFile());
    }
}
