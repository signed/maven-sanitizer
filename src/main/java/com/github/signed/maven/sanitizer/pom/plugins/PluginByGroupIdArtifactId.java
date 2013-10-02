package com.github.signed.maven.sanitizer.pom.plugins;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Selector;
import com.github.signed.maven.sanitizer.pom.Action;
import org.apache.maven.model.Plugin;

public class PluginByGroupIdArtifactId implements Selector<Plugin> {

    private final String groupId;
    private final String artifactId;

    public PluginByGroupIdArtifactId(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    @Override
    public void executeActionOnMatch(Plugin candidate, Action<Plugin> action, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        if(groupId.equals(candidate.getGroupId()) && artifactId.equals(candidate.getArtifactId()) ){
            action.perform(candidate);
        }
    }
}
