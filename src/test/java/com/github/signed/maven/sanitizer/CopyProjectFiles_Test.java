package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.ResourceRoots;
import com.github.signed.maven.sanitizer.pom.CleanRoom;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import static com.github.signed.matcher.file.IsFile.isAFile;
import static org.hamcrest.MatcherAssert.assertThat;

public class CopyProjectFiles_Test {
    private final Fixture fixture = new Fixture();

    @Rule
    public final TemporaryFolder source = new TemporaryFolder();
    @Rule
    public final TemporaryFolder destination = new TemporaryFolder();
    private final MavenProject mavenProject = new MavenProject();


    @Before
    public void copySampleProjectToTemporaryFolder() throws Exception {
        FileSystem fileSystem = new FileSystem();
        fileSystem.copyDirectoryContentInto(fixture.multiModule.containingDirectory, source.getRoot().toPath());
    }

    @Before
    public void setPomFileOnMavenProject() throws Exception {
        mavenProject.setFile(new File(source.getRoot(), "artifact/pom.xml"));
    }

    @Test
    public void copyEntireResourceDirectoryToTarget() throws Exception {
        List<Resource> resources = mavenProject.getBuild().getResources();
        Resource resource = new Resource();
        resource.setDirectory("src/main/resources");
        resources.add(resource);

        copyProject().copy(mavenProject);

        assertThat(new File(destination.getRoot(), "artifact/src/main/resources/aResource.txt"), isAFile());
    }

    private CopyProjectFiles copyProject() {
        SourceToDestinationTreeMapper mapper = new SourceToDestinationTreeMapper(source.getRoot().toPath(), destination.getRoot().toPath());
        final CleanRoom cleanRoom = new CleanRoom(new FileSystem(), mapper);
        CopyProjectFiles copyProjectFiles = new CopyProjectFiles(cleanRoom);
        copyProjectFiles.addPathsToCopy(new ResourceRoots());
        return copyProjectFiles;
    }
}
