package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.ModelSerializer;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManagement;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
import com.github.signed.maven.sanitizer.pom.dependencies.DropPlugin;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromBuild;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromPluginManagement;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CopyPom {

    private final ModelSerializer serializer = new ModelSerializer();
    private final CleanRoom cleanRoom;
    private final List<Critic<Dependency>> dependencyCritics = new ArrayList<>();
    private final List<Critic<Plugin>> pluginCritics = new ArrayList<>();

    public CopyPom(CleanRoom cleanRoom) {
        this.cleanRoom = cleanRoom;
    }

    public void addPluginCritic(Critic<Plugin> pluginCritic) {
        pluginCritics.add(pluginCritic);
    }

    public void addDependencyCritic(Critic<Dependency> dependencyCritic) {
        dependencyCritics.add(dependencyCritic);
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
        for (Critic<Dependency> critic : dependencyCritics) {
            DependenciesFromDependencies dependenciesFromDependencies = new DependenciesFromDependencies();
            criticise(model, critic, dependenciesFromDependencies, new DropDependency(dependenciesFromDependencies.elements(targetModelToWrite)));
            DependenciesFromDependencyManagement dependenciesFromDependencyManagement = new DependenciesFromDependencyManagement();
            criticise(model, critic, dependenciesFromDependencyManagement, new DropDependency(dependenciesFromDependencyManagement.elements(targetModelToWrite)));
        }
    }

    private void criticisePlugins(Model model, Model targetModelToWrite) {
        for (Critic<Plugin> critic : pluginCritics) {
            final PluginsFromBuild extractor = new PluginsFromBuild();
            criticises(model, critic, extractor, new DropPlugin(extractor.elements(targetModelToWrite)));
            PluginsFromPluginManagement pluginsFromPluginManagement = new PluginsFromPluginManagement();
            DropPlugin transformation = new DropPlugin(pluginsFromPluginManagement.elements(targetModelToWrite));
            criticises(model, critic, pluginsFromPluginManagement, transformation);
        }
    }

    private void criticises(Model model, Critic<Plugin> pluginCritic, Extractor<Plugin> extractor, Transformation<Plugin> transformation) {
        criticise(model, extractor, pluginCritic, transformation);
    }

    private void criticise(Model model, Critic<Dependency> dependencyCritic, Extractor<Dependency> extractor, DropDependency transformation) {
        criticise(model, extractor, dependencyCritic, transformation);
    }

    private <MavenModelElement> void criticise(Model model, Extractor<MavenModelElement> extractor, Critic<MavenModelElement> pluginCritic, Transformation<MavenModelElement> transformation) {
        for (MavenModelElement plugin : extractor.elements(model)) {
            pluginCritic.criticise(plugin, transformation);
        }
    }
}
