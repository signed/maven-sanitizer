package org.apache.maven.cli;

import com.github.signed.maven.sanitizer.Fixture;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BringPlexusOnline_Test {

    private final Fixture fixture = new Fixture();

    @Test
    public void bringPlexusOnline() throws Exception {
        String pathToDirectoryWithPom = fixture.multiModule.getParent();
        List<MavenProject> projects = getMavenProjects(pathToDirectoryWithPom);

        MavenProject project = getProjectWith(projects, "artifact");
        Dependency dependency = getDependencyWith(project, "junit");
        assertThat(dependency.getScope(), is("test"));
    }

    private List<MavenProject> getMavenProjects(String pathToDirectoryWithPom) throws Exception {
        MavenCli.CliRequest cliRequest = new MavenCli.CliRequest(new String[0], null);
        cliRequest.workingDirectory = pathToDirectoryWithPom;

        StandInMaven standInMaven = new StandInMaven();
        new CopyOfMavenCli(standInMaven).doMain(cliRequest);
        return standInMaven.getProjects();
    }

    private MavenProject getProjectWith(List<MavenProject> projects, String artifactId) {
        for (MavenProject project : projects) {
            if (artifactId.equals(project.getArtifactId())) {
                return project;
            }
        }
        return null;
    }

    private Dependency getDependencyWith(MavenProject project, String artifactId) {
        List<Dependency> dependencies = project.getDependencies();
        for (Dependency dependency : dependencies) {
            if(artifactId.equals(dependency.getArtifactId())){
                return dependency;
            }
        }
        return null;
    }
}