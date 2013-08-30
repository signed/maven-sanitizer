package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.path.ExecutionProbe;
import com.github.signed.maven.sanitizer.path.PathsInPluginConfiguration;
import com.github.signed.maven.sanitizer.path.ProjectSubdirectory;
import com.github.signed.maven.sanitizer.path.ResourceRoots;
import com.github.signed.maven.sanitizer.path.SourceRoots;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesInTestScope;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
import com.github.signed.maven.sanitizer.pom.dependencies.DropPlugin;
import com.github.signed.maven.sanitizer.pom.plugins.PluginByGroupIdArtifactId;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static java.util.Collections.singletonList;

class IntegrationTestConfiguration implements Configuration {

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
        copyPom.addPluginTransformation(new PluginByGroupIdArtifactId("org.apache.maven.plugins", "maven-antrun-plugin"), new DropPlugin());
        copyPom.addPluginTransformation(new PluginByGroupIdArtifactId("com.code54.mojo", "buildversion-plugin"), new DropPlugin());
        copyPom.addPluginTransformation(new PluginByGroupIdArtifactId("org.codehaus.mojo", "properties-maven-plugin"), new DropPlugin());
        copyPom.addDependencyTransformation(new DependenciesInTestScope(), new DropDependency());
    }
}
