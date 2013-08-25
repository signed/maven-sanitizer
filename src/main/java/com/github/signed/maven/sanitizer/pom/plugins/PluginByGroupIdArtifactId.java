package com.github.signed.maven.sanitizer.pom.plugins;

import com.github.signed.maven.sanitizer.pom.Critic;
import com.github.signed.maven.sanitizer.pom.Transformation;
import org.apache.maven.model.Plugin;

public class PluginByGroupIdArtifactId implements Critic<Plugin> {

    private final String groupId;
    private final String artifactId;

    public PluginByGroupIdArtifactId(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    @Override
    public void criticise(Plugin plugin, Transformation<Plugin> transformations) {
        if(groupId.equals(plugin.getGroupId()) && artifactId.equals(plugin.getArtifactId()) ){
            transformations.execute(plugin);
        }
    }
}
