package com.github.signed.maven.sanitizer.pom.plugins;

import org.apache.maven.model.Plugin;

public class DropPluginByGroupIdArtifactId implements PluginCritic {

    private final String groupId;
    private final String artifactId;

    public DropPluginByGroupIdArtifactId(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    @Override
    public void criticise(Plugin plugin, PluginTransformations transformations) {
        if(groupId.equals(plugin.getGroupId()) && artifactId.equals(plugin.getArtifactId()) ){
            transformations.drop(plugin);
        }
    }
}
