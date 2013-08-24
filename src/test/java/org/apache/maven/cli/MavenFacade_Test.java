package org.apache.maven.cli;

import com.github.signed.maven.sanitizer.Fixture;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MavenFacade_Test {

    private final Fixture fixture = new Fixture();
    private final MavenFacade mavenFacade = new MavenFacade();

    @Test
    public void bringPlexusOnline() throws Exception {
        String pathToDirectoryWithPom = fixture.multiModule.getParent();
        List<MavenProject> projects = mavenFacade.getMavenProjects(pathToDirectoryWithPom);

        MavenProject project = getProjectWith(projects, "artifact");
        Dependency dependency = getDependencyWith(project, "junit");
        assertThat(dependency.getScope(), is("test"));
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