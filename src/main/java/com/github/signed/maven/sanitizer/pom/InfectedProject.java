package com.github.signed.maven.sanitizer.pom;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import com.github.signed.maven.sanitizer.path.BasePath;
import com.github.signed.maven.sanitizer.pom.modules.Module;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

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

    public Optional<Dependency> getEntryInDependencyManagementFor(final Dependency element) {
        MavenProject project = project();
        Dependency managedDependency = null;
        while(managedDependency == null&& null != project.getParent()){
            project = project.getParent();
            DependencyManagement dependencyManagement = project.getOriginalModel().getDependencyManagement();
            if( null == dependencyManagement) {
                return Optional.absent();
            }

            final List<Dependency> dependencies = dependencyManagement.getDependencies();
            Iterable<Dependency> matches = Iterables.filter(dependencies, new Predicate<Dependency>() {
                @Override
                public boolean apply(Dependency dependency) {
                    return element.getGroupId().equals(dependency.getGroupId())
                            && element.getArtifactId().equals(dependency.getArtifactId())
                            && ((null == element.getType()) ? "jar" : element.getType()).equals(dependency.getType());
                }
            });

            if(!Iterables.isEmpty(matches)) {
                managedDependency = Iterables.get(matches, 0);
            }
        }

        return Optional.fromNullable(managedDependency);
    }
}
