package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.ModelSerializer;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManagement;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesInTestScope;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
import com.github.signed.maven.sanitizer.pom.dependencies.DropPlugin;
import com.github.signed.maven.sanitizer.pom.plugins.PluginByGroupIdArtifactId;
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
        Critic<Dependency> dependencyCritic = new DependenciesInTestScope();
        DependenciesFromDependencies dependenciesFromDependencies = new DependenciesFromDependencies();
        criticise(model, dependencyCritic, dependenciesFromDependencies, new DropDependency(dependenciesFromDependencies.elements(targetModelToWrite)));
        DependenciesFromDependencyManagement dependenciesFromDependencyManagement = new DependenciesFromDependencyManagement();
        criticise(model, dependencyCritic, dependenciesFromDependencyManagement, new DropDependency(dependenciesFromDependencyManagement.elements(targetModelToWrite)));
    }

    private void criticisePlugins(Model model, Model targetModelToWrite) {
        List<Critic<Plugin>> critics = new ArrayList<>();
        critics.add(new PluginByGroupIdArtifactId("org.apache.maven.plugins", "maven-antrun-plugin"));
        critics.add(new PluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin"));
        critics.add(new PluginByGroupIdArtifactId("org.codehaus.mojo", "properties-maven-plugin"));

        for (Critic<Plugin> critic : critics) {
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
