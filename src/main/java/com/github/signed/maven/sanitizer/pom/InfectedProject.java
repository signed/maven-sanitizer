package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.path.BasePath;
import com.github.signed.maven.sanitizer.pom.modules.Module;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;

public class InfectedProject {

    public final Model fullyPopulatedModel;
    public final Model targetModelToWrite;
    private final MavenProject mavenProject;

    public InfectedProject(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
        this.fullyPopulatedModel = mavenProject.getModel();
        this.targetModelToWrite = mavenProject.getOriginalModel().clone();
    }

    public Path baseDirectory() {
        return BasePath.baseDirectoryOf(mavenProject);
    }

    public Path resolvePathFor(Module module) {
        return baseDirectory().resolve(module.name);
    }
}
