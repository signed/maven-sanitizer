package com.github.signed.maven.model;

import com.google.common.base.Optional;
import org.apache.maven.model.PluginExecution;

public class PluginExecutionBuilder {

    public static PluginExecutionBuilder hire() {
        return new PluginExecutionBuilder();
    }

    private Optional<PluginConfigurationBuilder> configurationBuilder = Optional.absent();
    private PluginExecution pluginExecution = new PluginExecution();

    public PluginConfigurationBuilder withConfiguration() {
        PluginConfigurationBuilder configuration = new PluginConfigurationBuilder();
        configurationBuilder = Optional.of(configuration);
        return configuration;
    }

    public PluginExecution build() {
        if(configurationBuilder.isPresent()){
            pluginExecution.setConfiguration(configurationBuilder.get().toConfiguration());
        }
        return pluginExecution;
    }
}
