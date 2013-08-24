package org.apache.maven.cli;

import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.List;

public class MavenFacade {

    public List<MavenProject> getMavenProjects(Path source) {
        try {
            MavenCli.CliRequest cliRequest = new MavenCli.CliRequest(new String[0], null);
            cliRequest.workingDirectory = source.toAbsolutePath().toString();
            StandInMaven standInMaven = new StandInMaven();
            new CopyOfMavenCli(standInMaven).doMain(cliRequest);
            return standInMaven.getProjects();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}