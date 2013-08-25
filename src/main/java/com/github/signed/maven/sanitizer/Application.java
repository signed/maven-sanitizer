package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import org.apache.maven.cli.MavenFacade;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Application {

    public static void main(String [] args){
        Path source = Paths.get("source");
        Path destination = Paths.get("destionation");
        new Application(source, destination).sanitize();
    }

    private final Path source;
    private final CopyProject copyProject;

    public Application(Path source, Path destination) {
        this.source = source;
        final SourceToDestinationTreeMapper mapper = new SourceToDestinationTreeMapper(source, destination);
        final CleanRoom cleanRoom = new CleanRoom(new FileSystem(), mapper);
        copyProject = new CopyProject(cleanRoom, new CopyPom(cleanRoom));
    }

    public void sanitize() {
        List<MavenProject> mavenProjects = new MavenFacade().getMavenProjects(source);
        for (MavenProject mavenProject : mavenProjects) {
            copyProject.copy(mavenProject);
        }
    }
}