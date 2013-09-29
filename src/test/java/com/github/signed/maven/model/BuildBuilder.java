package com.github.signed.maven.model;

import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;

import java.util.ArrayList;
import java.util.List;

public class BuildBuilder {
    public static BuildBuilder hire() {
        return new BuildBuilder();
    }

    private final List<PluginBuilder> pluginBuilders = new ArrayList<>();

    private Build build = new Build();

    public PluginBuilder addPlugin(String groupId, String artifactId){
        PluginBuilder pluginBuilder = PluginBuilder.hire().groupId(groupId).artifactId(artifactId);
        pluginBuilders.add(pluginBuilder);
        return pluginBuilder;
    }

    public BuildBuilder addResource(String relativeResourcePath) {
        List<Resource> resources = build.getResources();
        Resource resource = new Resource();
        resource.setDirectory("src/main/resources");
        resources.add(resource);
        return this;
    }

    public Build build() {
        List<Plugin> plugins = build.getPlugins();
        for (PluginBuilder pluginBuilder : pluginBuilders) {
            plugins.add(pluginBuilder.build());
        }
        return build;
    }
}
