package com.github.signed.maven.sanitizer.pom;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

public class infectedProject {

    public final Model fullyPopulatedModel;
    public final Model targetModelToWrite;
    public final MavenProject mavenProject;

    public infectedProject(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
        this.fullyPopulatedModel = mavenProject.getModel();
        this.targetModelToWrite = mavenProject.getOriginalModel().clone();
    }
}
