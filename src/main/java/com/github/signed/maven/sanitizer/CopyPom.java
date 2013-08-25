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
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.List;

public class CopyPom {

    private final ModelSerializer serializer = new ModelSerializer();
    private final FileSystem fileSystem;

    public CopyPom(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public void copyPom(MavenProject mavenProject, Path pom, Path targetPom) {
        Model model = mavenProject.getModel();
        Model targetModelToWrite = mavenProject.getOriginalModel().clone();
        criticiseDependencies(model, targetModelToWrite);
        criticisePlugins(model, targetModelToWrite);
        this.fileSystem.writeStringTo(targetPom, serializer.serializeModelToString(targetModelToWrite));
    }

    private void criticiseDependencies(Model model, Model targetModelToWrite) {
        DropDependenciesInTestScope dropDependenciesInTestScope = new DropDependenciesInTestScope();

        criticiseDependencies(model, targetModelToWrite, dropDependenciesInTestScope);
        criticiseDependencyManagement(model, targetModelToWrite, dropDependenciesInTestScope);
    }

    private void criticisePlugins(Model model, Model targetModelToWrite) {
        PluginCritic pluginCritic = new DropPluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin");

        criticisePlugins(model, targetModelToWrite, new PluginsFromBuild(), pluginCritic);
        criticisePlugins(model, targetModelToWrite, new PluginsFromPluginManagment(), pluginCritic);
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

    private void criticisePlugins(Model model, Model targetModelToWrite, Extractor<Plugin> pluginsFromBuild, PluginCritic pluginCritic) {
        DefaultPluginTransformations transformations = new DefaultPluginTransformations(pluginsFromBuild.elements(targetModelToWrite));
        for (Plugin plugin : pluginsFromBuild.elements(model)) {
            pluginCritic.criticise(plugin, transformations);
        }
    }
}
