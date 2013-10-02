package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.ExecutionProbe;
import com.github.signed.maven.sanitizer.path.PathsInPluginConfiguration;
import com.github.signed.maven.sanitizer.path.ProjectSubdirectory;
import com.github.signed.maven.sanitizer.path.ResourceRoots;
import com.github.signed.maven.sanitizer.path.SourceRoots;
import com.github.signed.maven.sanitizer.pom.PomTransformer;
import com.github.signed.maven.sanitizer.pom.ForDependencyReferences;
import com.github.signed.maven.sanitizer.pom.ForPluginReferences;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesInScope;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyMatching;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static java.util.Collections.singletonList;

class DefaultConfiguration implements Configuration {

    @Override
    public void configure(CollectPathsToCopy collectPathsToCopy) {
        collectPathsToCopy.addPathsToCopy(new SourceRoots());
        collectPathsToCopy.addPathsToCopy(new ResourceRoots());
        collectPathsToCopy.addPathsToCopy(new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-war-plugin", singletonList(Paths.get("src/main/webapp")), "warSourceDirectory")));
        collectPathsToCopy.addPathsToCopy(new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-assembly-plugin", Collections.<Path>emptyList(), "descriptors")));
        collectPathsToCopy.addPathsToCopy(new ProjectSubdirectory("org.example", "parent", "important"));
    }

    @Override
    public void configure(PomTransformer pomTransformation) {
        during(pomTransformation, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagementSection().drop().referencesTo("org.apache.maven.plugins", "maven-antrun-plugin"));
        during(pomTransformation, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagementSection().drop().referencesTo("com.code54.mojo", "buildversion-plugin"));
        during(pomTransformation, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagementSection().drop().referencesTo("org.codehaus.mojo", "properties-maven-plugin"));

        during(pomTransformation, ForDependencyReferences.inAllModules().focusOnActualDependenciesAndDependencyManagement().drop().referencesTo(DependenciesInScope.Test()));
        during(pomTransformation, ForDependencyReferences.inAllModules().focusOnActualDependenciesAndDependencyManagement().drop().referencesTo(DependencyMatching.dependencyWith("org.example", "artifact", "zip")));
    }

    private void during(PomTransformer copyingThePom, ForDependencyReferences perform) {
        copyingThePom.addTransformer(perform.build());
    }

    private void during(PomTransformer pomTransformer, ForPluginReferences forPluginReferences) {
        pomTransformer.addTransformer(forPluginReferences.create());
    }
}