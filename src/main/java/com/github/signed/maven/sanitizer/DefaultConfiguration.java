package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.ExecutionProbe;
import com.github.signed.maven.sanitizer.path.PathsInPluginConfiguration;
import com.github.signed.maven.sanitizer.path.ProjectSubdirectory;
import com.github.signed.maven.sanitizer.path.ResourceRoots;
import com.github.signed.maven.sanitizer.path.SourceRoots;
import com.github.signed.maven.sanitizer.pom.CopyPom;
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
    public void configure(CopyProjectFiles copyProjectFiles) {
        copyProjectFiles.addPathsToCopy(new SourceRoots());
        copyProjectFiles.addPathsToCopy(new ResourceRoots());
        copyProjectFiles.addPathsToCopy(new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-war-plugin", singletonList(Paths.get("src/main/webapp")), "warSourceDirectory")));
        copyProjectFiles.addPathsToCopy(new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-assembly-plugin", Collections.<Path>emptyList(), "descriptors")));
        copyProjectFiles.addPathsToCopy(new ProjectSubdirectory("org.example", "parent", "important"));
    }

    @Override
    public void configure(CopyPom copyingThePom) {
        during(copyingThePom, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagementSection().drop().referencesTo("org.apache.maven.plugins", "maven-antrun-plugin"));
        during(copyingThePom, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagementSection().drop().referencesTo("com.code54.mojo", "buildversion-plugin"));
        during(copyingThePom, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagementSection().drop().referencesTo("org.codehaus.mojo", "properties-maven-plugin"));

        during(copyingThePom, ForDependencyReferences.inAllModules().focusOnActualDependenciesAndDependencyManagement().drop().referencesTo(DependenciesInScope.Test()));
        during(copyingThePom, ForDependencyReferences.inAllModules().focusOnActualDependenciesAndDependencyManagement().drop().referencesTo(new DependencyMatching("org.example", "artifact", "zip")));
    }

    private void during(CopyPom copyingThePom, ForDependencyReferences perform) {
        copyingThePom.addTransformer(perform.build());
    }

    private void during(CopyPom copyPom, ForPluginReferences forPluginReferences) {
        copyPom.addTransformer(forPluginReferences.create());
    }
}