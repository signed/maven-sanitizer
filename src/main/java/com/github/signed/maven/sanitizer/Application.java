package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import org.apache.maven.cli.MavenFacade;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.github.signed.maven.sanitizer.path.BasePath.baseDirectoryOf;

public class Application {

    private final CopyPom copyPom;
    private final CleanRoom cleanRoom;
    private final Configuration configuration;

    public static void main(String[] args) {
        Path source = Paths.get("source");
        Path destination = Paths.get("destination");
        Application application = new Application(source, destination, new NullConfiguration());
        application.configure();
        application.sanitize();
    }

    private final Path source;
    private final CopyProjectFiles copyProjectFiles;

    public Application(Path source, Path destination, Configuration configuration) {
        this.source = source;
        final SourceToDestinationTreeMapper mapper = new SourceToDestinationTreeMapper(source, destination);
        cleanRoom = new CleanRoom(new FileSystem(), mapper);
        copyPom = new CopyPom(cleanRoom);
        copyProjectFiles = new CopyProjectFiles(cleanRoom);
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
            copyProjectFiles.copy(mavenProject);
        }
    }

}