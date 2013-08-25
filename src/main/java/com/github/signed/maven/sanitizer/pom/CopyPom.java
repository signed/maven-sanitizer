package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.ModelSerializer;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManaegment;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependenciesInTestScope;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
import com.github.signed.maven.sanitizer.pom.dependencies.DropPlugin;
import com.github.signed.maven.sanitizer.pom.plugins.DropPluginByGroupIdArtifactId;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromBuild;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromPluginManagement;
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
        Critic<Dependency> dependencyCritic = new DropDependenciesInTestScope();

        final DependenciesFromDependencies dependenciesFromDependencies = new DependenciesFromDependencies();
        criticiseDependencies(model, dependenciesFromDependencies, dependencyCritic, new DropDependency(dependenciesFromDependencies.elements(targetModelToWrite)));
        final DependenciesFromDependencyManaegment dependenciesFromDependencyManaegment = new DependenciesFromDependencyManaegment();
        criticiseDependencies(model, dependenciesFromDependencyManaegment, dependencyCritic, new DropDependency(dependenciesFromDependencyManaegment.elements(targetModelToWrite)));
    }

    private void criticiseDependencies(Model model, Extractor<Dependency> extractor, Critic<Dependency> dropDependenciesInTestScope, Transformation<Dependency> transformation) {
        for (Dependency dependency : extractor.elements(model)) {
            dropDependenciesInTestScope.criticise(dependency, transformation);
        }
    }

    private void criticisePlugins(Model model, Model targetModelToWrite) {
        Critic<Plugin> pluginCritic = new DropPluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin");

        final PluginsFromBuild pluginsFromBuild = new PluginsFromBuild();
        criticisePlugins(model, pluginsFromBuild, pluginCritic, new DropPlugin(pluginsFromBuild.elements(targetModelToWrite)));
        final PluginsFromPluginManagement pluginsFromPluginManagement = new PluginsFromPluginManagement();
        criticisePlugins(model, pluginsFromPluginManagement, pluginCritic, new DropPlugin(pluginsFromPluginManagement.elements(targetModelToWrite)));
    }

    private void criticisePlugins(Model model, Extractor<Plugin> pluginsFromBuild, Critic<Plugin> pluginCritic, Transformation<Plugin> transformation) {
        for (Plugin plugin : pluginsFromBuild.elements(model)) {
            pluginCritic.criticise(plugin, transformation);
        }
    }
}
