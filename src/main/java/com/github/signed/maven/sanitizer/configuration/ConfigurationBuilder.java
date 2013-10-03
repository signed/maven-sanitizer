package com.github.signed.maven.sanitizer.configuration;

import com.github.signed.maven.sanitizer.CollectPathsToCopy;
import com.github.signed.maven.sanitizer.path.ExecutionProbe;
import com.github.signed.maven.sanitizer.path.PathsInPluginConfiguration;
import com.github.signed.maven.sanitizer.path.ResourceRoots;
import com.github.signed.maven.sanitizer.path.SourceRoots;
import com.github.signed.maven.sanitizer.pom.PomTransformer;
import com.google.common.collect.Lists;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.singletonList;

public class ConfigurationBuilder {
    private List<Configuration> configurations = Lists.newArrayList();

    public ConfigurationBuilder() {
        configurations.add(new Configuration() {
            @Override
            public void configure(CollectPathsToCopy projectFiles) {
                projectFiles.addPathsToCopy(new SourceRoots());
                projectFiles.addPathsToCopy(new ResourceRoots());
                projectFiles.addPathsToCopy(new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-war-plugin", singletonList(Paths.get("src/main/webapp")), "warSourceDirectory")));
                //projectFiles.addPathsToCopy(new PathsInPluginConfiguration(new ExecutionProbe("org.apache.maven.plugins", "maven-assembly-plugin", Collections.<Path>emptyList(), "descriptors")));
                //projectFiles.addPathsToCopy(new ProjectSubdirectory("org.example", "parent", "important"));
            }

            @Override
            public void configure(PomTransformer pomTransformation) {
                //nothing right now
            }
        });
    }

    public ConfigurationBuilder dropModule(String moduleName, String groupId, String artifactId) {
        configurations.add(new DropModule(moduleName, groupId, artifactId));
        return this;
    }

    public ConfigurationBuilder duringPomTransformation(ForDependencyReferences forDependencyReferences) {
        configurations.add(new SimplePomTransformation(forDependencyReferences.build()));
        return this;
    }

    public Configuration build() {
        return new SplitConfigurationAggregator(configurations);
    }

}
