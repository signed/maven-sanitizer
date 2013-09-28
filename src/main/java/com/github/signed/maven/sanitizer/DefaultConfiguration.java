package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.ExecutionProbe;
import com.github.signed.maven.sanitizer.path.PathsInPluginConfiguration;
import com.github.signed.maven.sanitizer.path.ProjectSubdirectory;
import com.github.signed.maven.sanitizer.path.ResourceRoots;
import com.github.signed.maven.sanitizer.path.SourceRoots;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import com.github.signed.maven.sanitizer.pom.PomTransformationBuilder;
import com.github.signed.maven.sanitizer.pom.PomTransformerCreator;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesInScope;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyMatching;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static com.github.signed.maven.sanitizer.pom.PomTransformationBuilder.forAllModules;
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
    public void configure(CopyPom copyingThePom) {
        during(copyingThePom, forAllModules().focusOnPluginsInBuildAndPluginManagmentSection().drop().pluginReferencesTo("org.apache.maven.plugins", "maven-antrun-plugin"));
        during(copyingThePom, forAllModules().focusOnPluginsInBuildAndPluginManagmentSection().drop().pluginReferencesTo("com.code54.mojo", "buildversion-plugin"));
        during(copyingThePom, forAllModules().focusOnPluginsInBuildAndPluginManagmentSection().drop().pluginReferencesTo("org.codehaus.mojo", "properties-maven-plugin"));

        PomTransformerCreator pomTransformerCreator = new PomTransformerCreator(copyingThePom);
        pomTransformerCreator.addDependencyTransformation(DependenciesInScope.Test(), new DropDependency());
        pomTransformerCreator.addDependencyTransformation(new DependencyMatching("org.example", "artifact", "zip"), new DropDependency());
    }

    private void during(CopyPom copyPom, PomTransformationBuilder pomTransformationBuilder) {
        copyPom.addTransformer(pomTransformationBuilder.create());
    }
}