package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.ExecutionProbe;
import com.github.signed.maven.sanitizer.path.PathsInPluginConfiguration;
import com.github.signed.maven.sanitizer.path.ProjectSubdirectory;
import com.github.signed.maven.sanitizer.path.ResourceRoots;
import com.github.signed.maven.sanitizer.path.SourceRoots;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import com.github.signed.maven.sanitizer.pom.ForDependencyReferences;
import com.github.signed.maven.sanitizer.pom.ForPluginReferences;
import com.github.signed.maven.sanitizer.pom.Selector;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesInScope;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyMatching;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
import org.apache.maven.model.Dependency;

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
        during(copyingThePom, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagmentSection().drop().pluginReferencesTo("org.apache.maven.plugins", "maven-antrun-plugin"));
        during(copyingThePom, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagmentSection().drop().pluginReferencesTo("com.code54.mojo", "buildversion-plugin"));
        during(copyingThePom, ForPluginReferences.inAllModules().focusOnPluginsInBuildAndPluginManagmentSection().drop().pluginReferencesTo("org.codehaus.mojo", "properties-maven-plugin"));


        Selector<Dependency> scopeTest = DependenciesInScope.Test();
        Action<Dependency> action1 = new DropDependency();
        ForDependencyReferences.inAllModules().theMethod(scopeTest, action1, copyingThePom);
        ForDependencyReferences.inAllModules().theOtherMethod(scopeTest, action1, copyingThePom);

        Selector<Dependency> gatCoordinates = new DependencyMatching("org.example", "artifact", "zip");
        Action<Dependency> action = new DropDependency();
        ForDependencyReferences.inAllModules().theMethod(gatCoordinates, action, copyingThePom);
        ForDependencyReferences.inAllModules().theOtherMethod(gatCoordinates, action, copyingThePom);
    }

    private void during(CopyPom copyPom, ForPluginReferences forPluginReferences) {
        copyPom.addTransformer(forPluginReferences.create());
    }
}