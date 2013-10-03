package com.github.signed.maven.sanitizer.configuration;

import com.google.common.collect.Lists;

import java.util.List;

public class ConfigurationBuilder {
    private List<Configuration> configurations = Lists.newArrayList();

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
