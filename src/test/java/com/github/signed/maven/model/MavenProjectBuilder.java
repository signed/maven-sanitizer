package com.github.signed.maven.model;

import java.io.File;
import java.nio.file.Path;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.project.MavenProject;

import com.github.signed.maven.sanitizer.pom.dependencies.DependencyBuilder;
import com.google.common.base.Optional;

public class MavenProjectBuilder {
    public static MavenProjectBuilder hire() {
        return new MavenProjectBuilder();
    }

    public static MavenProjectBuilder reactor() {
        MavenProjectBuilder mavenProjectBuilder = new MavenProjectBuilder();
        mavenProjectBuilder.withGroupId("org.example");
        mavenProjectBuilder.withArtifactId("reactor");
        mavenProjectBuilder.packaging("pom");
        mavenProjectBuilder.withVersion("1.2.3");
        return mavenProjectBuilder;
    }

    private MavenProjectBuilder withVersion(String version) {
        mavenProject.setVersion(version);
        return this;
    }

    public static MavenProjectBuilder childOf(MavenProject parent) {
        MavenProjectBuilder builder = new MavenProjectBuilder();
        builder.withParent(parent);
        return builder;
    }

    private MavenProject mavenProject = new MavenProject();
    private Optional<BuildBuilder> buildBuilder = Optional.absent();

    public MavenProjectBuilder() {
        mavenProject.setGroupId(null);
        mavenProject.setArtifactId(null);
        mavenProject.setVersion(null);
    }

    public BuildBuilder buildSection() {
        BuildBuilder buildBuilder = BuildBuilder.hire();
        this.buildBuilder = Optional.of(buildBuilder);
        return buildBuilder;
    }

    public MavenProjectBuilder withParent(MavenProject parentProject) {
        mavenProject.setParent(parentProject);
        Parent parent = new Parent();
        parent.setGroupId(parentProject.getGroupId());
        parent.setArtifactId(parentProject.getArtifactId());
        parent.setVersion(parentProject.getVersion());
        fullyPopulatedModel().setParent(parent);
        return this;
    }

    public MavenProjectBuilder packaging(String packaging) {
        mavenProject.setPackaging(packaging);
        return this;
    }

    public MavenProjectBuilder pomAt(Path pomLocation) {
        return pomAt(pomLocation.toFile());
    }

    public MavenProjectBuilder pomAt(File pomLocation) {
        mavenProject.setFile(pomLocation);
        return this;
    }

    public MavenProjectBuilder withGroupId(String groupId) {
        mavenProject.setGroupId(groupId);
        return this;
    }

    public MavenProjectBuilder withModule(String moduleName) {
        mavenProject.getModules().add(moduleName);
        return this;
    }

    public MavenProjectBuilder withArtifactId(String artifact) {
        mavenProject.setArtifactId(artifact);
        return this;
    }

    public MavenProjectBuilder addDependencyWithManagedVersion(Dependency dependency) {
        fullyPopulatedModel().addDependency(dependency);
        originalModel().addDependency(DependencyBuilder.like(dependency).but().withVersion(null).build());
        return this;
    }

    private Model originalModel() {
        Model originalModel = mavenProject.getOriginalModel();
        if(null == originalModel) {
            originalModel = new Model();
            mavenProject.setOriginalModel(originalModel);
        }
        return originalModel;
    }

    public MavenProjectBuilder managedDependency(Dependency dependency) {
        ensureDependencyManagement().addDependency(dependency);
        return this;
    }

    public MavenProject build() {
        if (buildBuilder.isPresent()) {
            mavenProject.setBuild(buildBuilder.get().build());
        }
        if(null == mavenProject.getOriginalModel()){
            mavenProject.setOriginalModel(mavenProject.getModel().clone());
        }
        return mavenProject;
    }

    private DependencyManagement ensureDependencyManagement() {
        DependencyManagement dependencyManagement = mavenProject.getDependencyManagement();
        if(null == dependencyManagement){
            dependencyManagement = new DependencyManagement();
            fullyPopulatedModel().setDependencyManagement(dependencyManagement);
        }
        Model originalModel = originalModel();

        originalModel.setDependencyManagement(dependencyManagement);
        return dependencyManagement;
    }

    private Model fullyPopulatedModel() {
        return mavenProject.getModel();
    }
}
