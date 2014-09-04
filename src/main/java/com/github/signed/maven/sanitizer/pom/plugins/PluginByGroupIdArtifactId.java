package com.github.signed.maven.sanitizer.pom.plugins;

import org.apache.maven.model.Plugin;
import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.Selector;

public class PluginByGroupIdArtifactId implements Selector<Plugin> {

    private final String groupId;
    private final String artifactId;

    public PluginByGroupIdArtifactId(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    @Override
    public boolean executeActionOnMatch(Patient<Plugin> patient, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        return groupId.equals(patient.fullyPopulated().getGroupId()) && artifactId.equals(patient.fullyPopulated().getArtifactId());
    }
}
