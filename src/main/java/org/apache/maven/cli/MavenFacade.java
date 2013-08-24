package org.apache.maven.cli;

import org.apache.maven.project.MavenProject;

import java.util.List;

public class MavenFacade {
    public MavenFacade() {
    }

    public List<MavenProject> getMavenProjects(String pathToDirectoryWithPom) throws Exception {
        MavenCli.CliRequest cliRequest = new MavenCli.CliRequest(new String[0], null);
        cliRequest.workingDirectory = pathToDirectoryWithPom;

        StandInMaven standInMaven = new StandInMaven();
        new CopyOfMavenCli(standInMaven).doMain(cliRequest);
        return standInMaven.getProjects();
    }
}