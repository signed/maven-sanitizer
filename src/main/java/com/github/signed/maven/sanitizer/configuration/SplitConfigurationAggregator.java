package com.github.signed.maven.sanitizer.configuration;

import com.github.signed.maven.sanitizer.CollectPathsToCopy;
import com.github.signed.maven.sanitizer.pom.PomTransformer;

public class SplitConfigurationAggregator implements Configuration{

    private final Iterable<Configuration> configurations;

    public SplitConfigurationAggregator(Iterable<Configuration> configurations) {
        this.configurations = configurations;
    }

    @Override
    public void configure(CollectPathsToCopy projectFiles) {
        for (Configuration configuration : configurations) {
            configuration.configure(projectFiles);
        }
    }

    @Override
    public void configure(PomTransformer pomTransformation) {
        for (Configuration configuration : configurations) {
            configuration.configure(pomTransformation);
        }
    }
}
