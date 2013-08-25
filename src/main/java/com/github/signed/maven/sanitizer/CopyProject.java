package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.Extractor;
import com.github.signed.maven.sanitizer.pom.dependencies.DefaultDependencyTransformations;
import com.github.signed.maven.sanitizer.pom.dependencies.DefaultPluginTransformations;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyCritic;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyTransformations;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependenciesInTestScope;
import com.github.signed.maven.sanitizer.pom.plugins.DropPluginByGroupIdArtifactId;
import com.github.signed.maven.sanitizer.pom.plugins.PluginCritic;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromBuild;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromPluginManagment;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
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

        criticiseDependencies(model, targetModelToWrite);
        criticisePlugins(model, targetModelToWrite);


        fileSystem.writeStringTo(targetPom, serializeModelToString(targetModelToWrite));
    }

    private void criticisePlugins(Model model, Model targetModelToWrite) {
        PluginCritic pluginCritic = new DropPluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin");

        criticisePlugins(model, targetModelToWrite, new PluginsFromBuild(), pluginCritic);
        criticisePlugins(model, targetModelToWrite, new PluginsFromPluginManagment(), pluginCritic);
    }

    private void criticisePlugins(Model model, Model targetModelToWrite, Extractor<Plugin> pluginsFromBuild, PluginCritic pluginCritic) {
        DefaultPluginTransformations transformations = new DefaultPluginTransformations(pluginsFromBuild.elements(targetModelToWrite));
        for (Plugin plugin : pluginsFromBuild.elements(model)) {
            pluginCritic.criticise(plugin, transformations);
        }
    }


    private void criticiseDependencies(Model model, Model targetModelToWrite) {
        DropDependenciesInTestScope dropDependenciesInTestScope = new DropDependenciesInTestScope();
        criticiseDependencies(model, targetModelToWrite, dropDependenciesInTestScope);
        criticiseDependencyManagement(model, targetModelToWrite, dropDependenciesInTestScope);
    }

    private void criticiseDependencies(Model model, Model targetModelToWrite, DependencyCritic critic) {
        criticiseAListOfDependencies(model, critic, targetModelToWrite.getDependencies());
    }

    private void criticiseDependencyManagement(Model model, Model targetModelToWrite, DependencyCritic critic) {
        DependencyManagement dependencyManagement = targetModelToWrite.getDependencyManagement();
        if (dependencyManagement == null) {
            return;
        }
        List<Dependency> dependencies = dependencyManagement.getDependencies();
        if (dependencies == null) {
            return;
        }
        criticiseAListOfDependencies(model, critic, dependencies);
    }

    private void criticiseAListOfDependencies(Model model, DependencyCritic critic, List<Dependency> dependencies) {
        DependencyTransformations transformations = new DefaultDependencyTransformations(dependencies);
        for (Dependency dependency : model.getDependencyManagement().getDependencies()) {
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