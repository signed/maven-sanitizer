package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.ExecutionProbe;
import com.github.signed.maven.sanitizer.path.PathsInPluginConfiguration;
import com.github.signed.maven.sanitizer.path.ProjectSubdirectory;
import com.github.signed.maven.sanitizer.path.ResourceRoots;
import com.github.signed.maven.sanitizer.path.SourceRoots;
import com.github.signed.maven.sanitizer.pom.PomTransformationBuilder;
import com.github.signed.maven.sanitizer.pom.PomTransformerCreator;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesInScope;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyMatching;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
import com.github.signed.maven.sanitizer.pom.dependencies.DropPlugin;
import com.github.signed.maven.sanitizer.pom.plugins.PluginByGroupIdArtifactId;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromBuild;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromPluginManagement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static java.util.Collections.singletonList;

class DefaultConfiguration implements Configuration {

    @Override
    public void configure(CopyProjectFiles copyProjectFiles) {
        copyProjectFiles.addPathsToCopy(new SourceRoots());
        copyProjectFiles.addPathsToCopy(new ResourceRoots());
        copyProjectFiles.addPathsToCopy(new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-war-plugin", singletonList(Paths.get("src/main/webapp")), "warSourceDirectory")));
        copyProjectFiles.addPathsToCopy(new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-assembly-plugin", Collections.<Path>emptyList(), "descriptors")));
        copyProjectFiles.addPathsToCopy(new ProjectSubdirectory("org.example", "parent", "important"));
    }

    @Override
    public void configure(CopyPom copyPom) {
        PomTransformationBuilder builder2 = new PomTransformationBuilder().targetElementsMatching(new PluginByGroupIdArtifactId("org.apache.maven.plugins", "maven-antrun-plugin")).andPerform(new DropPlugin());
        copyPom.addTransformer(builder2.extract(new PluginsFromBuild()).extract(new PluginsFromPluginManagement()).create());

        PomTransformationBuilder builder1 = new PomTransformationBuilder().targetElementsMatching(new PluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin")).andPerform(new DropPlugin());
        copyPom.addTransformer(builder1.extract(new PluginsFromBuild()).extract(new PluginsFromPluginManagement()).create());

        PomTransformationBuilder builder = new PomTransformationBuilder().targetElementsMatching(new PluginByGroupIdArtifactId("org.codehaus.mojo", "properties-maven-plugin")).andPerform(new DropPlugin());
        copyPom.addTransformer(builder.extract(new PluginsFromBuild()).extract(new PluginsFromPluginManagement()).create());

        PomTransformerCreator pomTransformerCreator = new PomTransformerCreator(copyPom);
        pomTransformerCreator.addDependencyTransformation(DependenciesInScope.Test(), new DropDependency());
        pomTransformerCreator.addDependencyTransformation(new DependencyMatching("org.example", "artifact", "zip"), new DropDependency());
    }
}