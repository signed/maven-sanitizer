package com.github.signed.maven.sanitizer.pom;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

public class SanitizedPom {
    public final MavenProject sourceMavenProject;
    public final Model transformedModelToWrite;

    public SanitizedPom(MavenProject sourceMavenProject, Model transformedModelToWrite) {
        this.sourceMavenProject = sourceMavenProject;
        this.transformedModelToWrite = transformedModelToWrite;
    }
}
