package com.github.signed.maven.sanitizer;

import org.apache.maven.cli.MavenFacade;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.List;

public class SanitizedBuild {

    private final List<MavenProject> mavenProjects;

    public SanitizedBuild(Path destination) {
        mavenProjects = new MavenFacade().getMavenProjects(destination);
    }

    public List<String> getModulesDefinedIn(String moduleName) {
        return getModelFor(moduleName).getModules();
    }

    public Model warModule() {
        return getModelFor("war");
    }

    Model getModelFor(String moduleName) {
        for (MavenProject mavenProject : this.mavenProjects) {
            if (moduleName.equals(mavenProject.getName())) {
                return mavenProject.getOriginalModel();
            }
        }
        throw new RuntimeException("there was no module with the expected name " + moduleName);
    }

    public Model reactor() {
        return getModelFor("multimodule");
    }
}