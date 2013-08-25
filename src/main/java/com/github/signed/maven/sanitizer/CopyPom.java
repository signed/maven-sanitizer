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
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;

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

        criticiseAListOfDependencies(model, dropDependenciesInTestScope, new DependenciesFromDependencies().elements(targetModelToWrite));
        criticiseAListOfDependencies(model, dropDependenciesInTestScope, new DependenciesFromDependencyManagment().elements(targetModelToWrite));
    }

    private void criticisePlugins(Model model, Model targetModelToWrite) {
        PluginCritic pluginCritic = new DropPluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin");

        criticisePlugins(model, targetModelToWrite, new PluginsFromBuild(), pluginCritic);
        criticisePlugins(model, targetModelToWrite, new PluginsFromPluginManagment(), pluginCritic);
    }

    private void criticiseAListOfDependencies(Model model, DependencyCritic critic, Iterable<Dependency> dependencies) {
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
