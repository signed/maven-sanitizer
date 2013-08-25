package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Resource;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.List;

public class CopyProject {
    private final FileSystem fileSystem = new FileSystem();
    private final SourceToDestinationTreeMapper application;

    public CopyProject(SourceToDestinationTreeMapper application) {
        this.application = application;
    }

    void copy(MavenProject mavenProject) {
        Path baseDir = mavenProject.getBasedir().toPath();
        ensureTargetBaseDirectoryExists(baseDir);

        copyPom(mavenProject);
        copySourceRoots(mavenProject, baseDir);
        copyResources(mavenProject, baseDir);
    }

    private void ensureTargetBaseDirectoryExists(Path baseDir) {
        Path targetBaseDir = application.map(baseDir);
        fileSystem.createDirectory(targetBaseDir);
    }

    private void copyPom(MavenProject mavenProject) {
        Path pom = mavenProject.getFile().toPath();
        Path targetPom = application.map(pom);
        Model model = mavenProject.getModel();
        Model targetModelToWrite = mavenProject.getOriginalModel().clone();
        DropDependenciesInTestScope dropDependenciesInTestScope = new DropDependenciesInTestScope();

        criticiseDependencies(model, targetModelToWrite, dropDependenciesInTestScope);
        criticiseDependencyManagement(model, targetModelToWrite, dropDependenciesInTestScope);

        fileSystem.writeStringTo(targetPom, serializeModelToString(targetModelToWrite));
    }

    private void criticiseDependencies(Model model, Model targetModelToWrite, DependencyCritic critic) {
        criticiseAListOfDependencies(model, critic, targetModelToWrite.getDependencies());
    }

    private void criticiseDependencyManagement(Model model, Model targetModelToWrite, DependencyCritic critic) {
        DependencyManagement dependencyManagement = targetModelToWrite.getDependencyManagement();
        if(dependencyManagement == null){
            return;
        }
        List<Dependency> dependencies = dependencyManagement.getDependencies();
        if(dependencies == null){
            return;
        }
        criticiseAListOfDependencies(model, critic, dependencies);
    }

    private void criticiseAListOfDependencies(Model model, DependencyCritic critic, List<Dependency> dependencies) {
        DependencyTransformations transformations = new DefaultDependencyTransformations(dependencies);
        for(Dependency dependency : model.getDependencyManagement().getDependencies()){
            critic.criticise(dependency, transformations);
        }
    }

    private static String serializeModelToString(Model model) {
        try {
            DefaultModelWriter modelWriter = new DefaultModelWriter();
            StringWriter writer = new StringWriter();
            modelWriter.write(writer, null, model);
            return writer.toString();
        } catch (IOException e) {
            //should never occur because we are writing into a String writer
            throw new RuntimeException(e);
        }
    }

    public interface DependencyCritic{
        void criticise(Dependency dependency, DependencyTransformations transformations);
    }

    public interface DependencyTransformations{
        void drop(Dependency dependency);
    }

    private void copySourceRoots(MavenProject mavenProject, Path baseDir) {
        List<String> compileSourceRoots = mavenProject.getCompileSourceRoots();
        for (String compileSourceRoot : compileSourceRoots) {
            Path sourceCompileSourceRoot = baseDir.resolve(compileSourceRoot);
            Path targetCompileSourceRoot = application.map(sourceCompileSourceRoot);
            fileSystem.copyDirectoryContentInto(sourceCompileSourceRoot, targetCompileSourceRoot);
        }
    }

    private void copyResources(MavenProject mavenProject, Path baseDir) {
        List<Resource> resources = mavenProject.getResources();
        for (Resource resource : resources) {
            Path sourceResource = baseDir.resolve(resource.getDirectory());
            Path targetResource = application.map(sourceResource);
            fileSystem.copyDirectoryContentInto(sourceResource, targetResource);
        }
    }
}