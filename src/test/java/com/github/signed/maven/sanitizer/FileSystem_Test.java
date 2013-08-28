package com.github.signed.maven.sanitizer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.github.signed.matcher.file.IsEmptyDirectory.isAnEmptyDirectory;
import static com.github.signed.matcher.file.IsFile.isAFile;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileSystem_Test {

    @Rule
    public TemporaryFolder source = new TemporaryFolder();
    @Rule
    public TemporaryFolder destination = new TemporaryFolder();

    private final FileSystem fileSystem = new FileSystem();

    @Test
    public void doNothingIfSourceDirectoryIsEmpty() throws Exception {
        copyContentInSourceToDestination();
        assertThat(destination.getRoot(), isAnEmptyDirectory());
    }

    @Test
    public void copyAFileToDestination() throws Exception {
        source.newFile("single-file");
        copyContentInSourceToDestination();
        assertThat(new File(destination.getRoot(), "single-file"), isAFile());
    }

    @Test
    public void copyFilesInSubdirectories() throws Exception {
        File subdirectory = source.newFolder("one", "two");
        new File(subdirectory, "a-file").createNewFile();

        copyContentInSourceToDestination();
        assertThat(new File(destination.getRoot(), "one/two/a-file"), isAFile());
    }

    @Test
    public void copyFile() throws Exception {
        Path theFile = source.newFile("theFile").toPath();
        Path destination = this.destination.getRoot().toPath().resolve("theFile");

        fileSystem.copyFile(theFile, destination);
        assertThat(destination.toFile(), isAFile());
    }

    @Test
    public void copyFileEvenInDeepSubdirectories() throws Exception {
        source.newFolder("one", "two", "three");
        Path theFileToCopy = this.source.newFile("one/two/three/theFile").toPath();

        Path destination = this.destination.getRoot().toPath().resolve("one/two/three/theFile");

        fileSystem.copyFile(theFileToCopy, destination);
        assertThat(destination.toFile(), isAFile());
    }

    private void copyContentInSourceToDestination() throws IOException {
        fileSystem.copyDirectoryContentInto(source.getRoot().toPath(), destination.getRoot().toPath());
    }
}