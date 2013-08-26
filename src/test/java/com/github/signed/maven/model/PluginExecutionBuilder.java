package com.github.signed.maven.model;

import com.google.common.base.Optional;
import org.apache.maven.model.PluginExecution;

public class PluginExecutionBuilder {

    public static PluginExecutionBuilder hire() {
        return new PluginExecutionBuilder();
    }

    private Optional<ConfigurationBuilder> configurationBuilder = Optional.absent();
    private PluginExecution pluginExecution = new PluginExecution();

    public ConfigurationBuilder withConfiguration() {
        ConfigurationBuilder configuration = new ConfigurationBuilder();
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
