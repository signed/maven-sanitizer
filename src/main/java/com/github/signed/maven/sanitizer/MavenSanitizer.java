package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import org.apache.maven.cli.MavenFacade;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.github.signed.maven.sanitizer.path.BasePath.baseDirectoryOf;

public class MavenSanitizer {

    private final CleanRoomGuard cleanRoomGuard;

    public static void main(String[] args) {
        Path source = Paths.get(args[0]).toAbsolutePath();
        Path destination = Paths.get(args[1]).toAbsolutePath();
        System.out.println("source: "+source);
        System.out.println("destination: " + destination);
        MavenSanitizer mavenSanitizer = new MavenSanitizer(source, destination, readConfiguration());
        mavenSanitizer.configure();
        mavenSanitizer.sanitize();
    }

    private static Configuration readConfiguration() {
        return new DefaultConfiguration();
    }

    private final CopyPom copyPom;
    private final CleanRoom cleanRoom;
    private final Configuration configuration;
    private final Path source;
    private final CopyProjectFiles copyProjectFiles;

    public MavenSanitizer(Path source, Path destination, Configuration configuration) {
        this.source = source;
        final SourceToDestinationTreeMapper mapper = new SourceToDestinationTreeMapper(source, destination);
        cleanRoom = new CleanRoom(new FileSystem(), mapper);
        copyPom = new CopyPom(cleanRoom);
        cleanRoomGuard = new CleanRoomGuard(cleanRoom);
        copyProjectFiles = new CopyProjectFiles();
        this.configuration = configuration;
    }

    public void configure() {
        configuration.configure(this.copyProjectFiles);
        configuration.configure(this.copyPom);
    }

    public void sanitize() {
        List<MavenProject> mavenProjects = new MavenFacade().getMavenProjects(source);
        for (MavenProject mavenProject : mavenProjects) {
            cleanRoom.createDirectoryAssociatedTo(baseDirectoryOf(mavenProject));
            copyPom.from(mavenProject);
            cleanRoomGuard.copyToCleanRoom(copyProjectFiles.allPathsCollectedFrom(mavenProject));
        }
    }

}