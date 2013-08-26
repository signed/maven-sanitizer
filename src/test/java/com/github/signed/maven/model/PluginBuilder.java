package com.github.signed.maven.model;

import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;

import java.util.ArrayList;
import java.util.List;

public class PluginBuilder {
    public static PluginBuilder hire() {
        return new PluginBuilder();
    }

    private Plugin plugin = new Plugin();
    private List<PluginExecutionBuilder> executionBuilders = new ArrayList<>();

    public PluginBuilder groupId(String groupId){
        plugin.setGroupId(groupId);
        return this;
    }

    public PluginBuilder artifactId(String artifactId){
        plugin.setArtifactId(artifactId);
        return this;
    }

    public PluginExecutionBuilder withExecution(){
        PluginExecutionBuilder pluginExecutionBuilder = PluginExecutionBuilder.hire();
        executionBuilders.add(pluginExecutionBuilder);
        return pluginExecutionBuilder;
    }


    public Plugin build() {
        List<PluginExecution> executions = plugin.getExecutions();
        for (PluginExecutionBuilder executionBuilder : executionBuilders) {
            executions.add(executionBuilder.build());
        }
        return plugin;
    }
}
