package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.ModelSerializer;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
import com.github.signed.maven.sanitizer.pom.dependencies.DefaultPluginTransformations;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManagment;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyCritic;
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
    private final CleanRoom cleanRoom;

    public CopyPom(CleanRoom cleanRoom) {
        this.cleanRoom = cleanRoom;
    }

    public void from(MavenProject mavenProject) {
        Path pom = mavenProject.getFile().toPath();
        Model model = mavenProject.getModel();
        Model targetModelToWrite = mavenProject.getOriginalModel().clone();
        criticiseDependencies(model, targetModelToWrite);
        criticisePlugins(model, targetModelToWrite);
        String content = serializer.serializeModelToString(targetModelToWrite);
        cleanRoom.writeStringToPathAssociatedWith(pom, content);
    }

    private void criticiseDependencies(Model model, Model targetModelToWrite) {
        DependencyCritic dependencyCritic = new DropDependenciesInTestScope();

        criticiseDependencies(model, targetModelToWrite, new DependenciesFromDependencies(), dependencyCritic);
        criticiseDependencies(model, targetModelToWrite, new DependenciesFromDependencyManagment(), dependencyCritic);
    }

    private void criticiseDependencies(Model model, Model targetModelToWrite, Extractor<Dependency> extractor, DependencyCritic dropDependenciesInTestScope) {
        Transformation<Dependency> transformations = new DropDependency(extractor.elements(targetModelToWrite));
        for (Dependency dependency : extractor.elements(model)) {
            dropDependenciesInTestScope.criticise(dependency, transformations);
        }
    }

    private void criticisePlugins(Model model, Model targetModelToWrite) {
        PluginCritic pluginCritic = new DropPluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin");

        criticisePlugins(model, targetModelToWrite, new PluginsFromBuild(), pluginCritic);
        criticisePlugins(model, targetModelToWrite, new PluginsFromPluginManagment(), pluginCritic);
    }

    private void criticisePlugins(Model model, Model targetModelToWrite, Extractor<Plugin> pluginsFromBuild, PluginCritic pluginCritic) {
        Transformation<Plugin> transformations = new DefaultPluginTransformations(pluginsFromBuild.elements(targetModelToWrite));
        for (Plugin plugin : pluginsFromBuild.elements(model)) {
            pluginCritic.criticise(plugin, transformations);
        }
    }
}
