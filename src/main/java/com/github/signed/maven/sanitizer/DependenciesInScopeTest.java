package com.github.signed.maven.sanitizer;

import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

public class DependenciesInScopeTest {
    private final List<MavenProject> mavenProjects;

    public DependenciesInScopeTest(List<MavenProject> mavenProjects) {
        this.mavenProjects = mavenProjects;
    }

    public Model getChildModule() {
        return getModelFor("child");
    }

    private Model getModelFor(String moduleName) {
        for (MavenProject mavenProject : this.mavenProjects) {
            if (moduleName.equals(mavenProject.getName())) {
                return mavenProject.getOriginalModel();
            }
        }
        throw new RuntimeException("there was no module with the expected name " + moduleName);
    }
}
