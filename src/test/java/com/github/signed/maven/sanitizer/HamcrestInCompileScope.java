package com.github.signed.maven.sanitizer;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.cli.MavenFacade;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

public class HamcrestInCompileScope {

    private final List<MavenProject> mavenProjects;

    public HamcrestInCompileScope(Path destination) {
        mavenProjects = new MavenFacade().getMavenProjects(destination);
    }

    public Model includesHamcrestInCompileScope() {
        return getModelFor("include-test-scoped-managed-dependency-in-compile-scope");
    }

    public Model managesDependencies(){
        return getModelFor("parent");
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
