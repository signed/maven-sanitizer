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
    private final List<ModelTransformer> modelTransformers = new ArrayList<>();

    public CopyPom(CleanRoom cleanRoom) {
        this.cleanRoom = cleanRoom;
    }

    public void addPluginCritic(Critic<Plugin> pluginCritic) {
        modelTransformers.add(new DefaultModelTransformer<>(pluginCritic, new PluginsFromBuild(), new DropPlugin()));
        modelTransformers.add(new DefaultModelTransformer<>(pluginCritic, new PluginsFromPluginManagement(), new DropPlugin()));
    }

    public void addDependencyCritic(Critic<Dependency> dependencyCritic) {
        modelTransformers.add(new DefaultModelTransformer<>(dependencyCritic, new DependenciesFromDependencies(), new DropDependency()));
        modelTransformers.add(new DefaultModelTransformer<>(dependencyCritic, new DependenciesFromDependencyManagement(), new DropDependency()));
    }

    public void from(MavenProject mavenProject) {
        Path pom = mavenProject.getFile().toPath();
        Model model = mavenProject.getModel();
        Model targetModelToWrite = mavenProject.getOriginalModel().clone();
        TheModels models = new TheModels(model, targetModelToWrite);
        transform(models);
        String content = serializer.serializeModelToString(targetModelToWrite);
        cleanRoom.writeStringToPathAssociatedWith(pom, content);
    }

    private void transform(TheModels models) {
        for (ModelTransformer modelTransformer : modelTransformers) {
            modelTransformer.transform(models);
        }
    }
}