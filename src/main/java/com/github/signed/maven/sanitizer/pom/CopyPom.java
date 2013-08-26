package com.github.signed.maven.sanitizer.pom;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        criticise(model, targetModelToWrite, dependencyCritic, new DependenciesFromDependencies());
        criticise(model, targetModelToWrite, dependencyCritic, new DependenciesFromDependencyManagement());
    }

    private void criticisePlugins(Model model, Model targetModelToWrite) {
        List<Critic<Plugin>> critics = new ArrayList<>();
        critics.add(new PluginByGroupIdArtifactId("org.apache.maven.plugins", "maven-antrun-plugin"));
        critics.add(new PluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin"));
        critics.add(new PluginByGroupIdArtifactId("org.codehaus.mojo", "properties-maven-plugin"));

        for (Critic<Plugin> critic : critics) {
            criticises(model, targetModelToWrite, critic, new PluginsFromBuild());
            criticises(model, targetModelToWrite, critic, new PluginsFromPluginManagement());
        }
    }

    private void criticises(Model model, Model targetModelToWrite, Critic<Plugin> pluginCritic, Extractor<Plugin> extractor) {
        criticise(model, extractor, pluginCritic, new DropPlugin(extractor.elements(targetModelToWrite)));
    }

    private void criticise(Model model, Model targetModelToWrite, Critic<Dependency> dependencyCritic, Extractor<Dependency> extractor) {
        criticise(model, extractor, dependencyCritic, new DropDependency(extractor.elements(targetModelToWrite)));
    }

    private <MavenModelElement> void criticise(Model model, Extractor<MavenModelElement> pluginsFromBuild, Critic<MavenModelElement> pluginCritic, Transformation<MavenModelElement> transformation) {
        for (MavenModelElement plugin : pluginsFromBuild.elements(model)) {
            pluginCritic.criticise(plugin, transformation);
        }
    }
}
