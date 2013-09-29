package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.github.signed.maven.sanitizer.pom.PomTransformer;
import org.apache.maven.cli.MavenFacade;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    private final PomTransformer pomTransformer;
    private final Configuration configuration;
    private final Path source;
    private final CollectPathsToCopy collectPathsToCopy;

    public MavenSanitizer(Path source, Path destination, Configuration configuration) {
        this.source = source;
        final SourceToDestinationTreeMapper mapper = new SourceToDestinationTreeMapper(source, destination);
        pomTransformer = new PomTransformer();
        CleanRoom cleanRoom = new CleanRoom(new FileSystem(), mapper);
        cleanRoomGuard = new CleanRoomGuard(cleanRoom);
        collectPathsToCopy = new CollectPathsToCopy();
        this.configuration = configuration;
    }

    public void configure() {
        configuration.configure(this.collectPathsToCopy);
        configuration.configure(this.pomTransformer);
    }

    public void sanitize() {
        List<MavenProject> mavenProjects = new MavenFacade().getMavenProjects(source);
        CleanRoomApplication cleanRoomApplication = writeCleanRoomApplicationFor(mavenProjects);
        cleanRoomGuard.process(cleanRoomApplication);
    }

    private CleanRoomApplication writeCleanRoomApplicationFor(List<MavenProject> mavenProjects) {
        CleanRoomApplication cleanRoomApplication = new CleanRoomApplication();
        for (MavenProject mavenProject : mavenProjects) {
            cleanRoomApplication.add(pomTransformer.transformPomIn(mavenProject));
            cleanRoomApplication.addAll(collectPathsToCopy.from(mavenProject));
        }
        return cleanRoomApplication;
    }
}