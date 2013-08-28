package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.FileSystem;
import com.github.signed.maven.sanitizer.SourceToDestinationTreeMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CleanRoom_Test {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void useTheCopyFileMethodToCopyFiles() throws Exception {
        Path file = folder.newFile("to copy").toPath();
        FileSystem mock = mock(FileSystem.class);
        SourceToDestinationTreeMapper mapper = mock(SourceToDestinationTreeMapper.class);

        CleanRoom cleanRoom = new CleanRoom(mock, mapper);
        Path mapperResult = Paths.get("any");
        when(mapper.map(file)).thenReturn(mapperResult);
        cleanRoom.copyContentBelowInAssociatedDirectory(file);

        verify(mock).copyFile(file, mapperResult);
    }
}
