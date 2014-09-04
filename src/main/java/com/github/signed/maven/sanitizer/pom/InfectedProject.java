package com.github.signed.maven.sanitizer.pom;

import java.nio.file.Path;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import com.github.signed.maven.sanitizer.path.BasePath;
import com.github.signed.maven.sanitizer.pom.modules.Module;

public class InfectedProject {

    private final Model modelAsWritten;
    private final MavenProject mavenProject;
    public final Model fullyPopulatedModel;
    public final Model targetModelToWrite;

    public InfectedProject(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
        this.modelAsWritten = mavenProject.getOriginalModel();
        this.fullyPopulatedModel = mavenProject.getModel();
        this.targetModelToWrite = mavenProject.getOriginalModel().clone();
    }

    public Path baseDirectory() {
        return BasePath.baseDirectoryOf(mavenProject);
    }

    public Path resolvePathFor(Module module) {
        return baseDirectory().resolve(module.name);
    }

    public Model modelAsWritten() {
        return modelAsWritten;
    }

    public MavenProject project(){
        return mavenProject;
    }
}
