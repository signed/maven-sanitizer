package com.github.signed.maven.sanitizer.path;

import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExecutionsProbe {

    private final String groupId;
    private final String artifactId;

    public ExecutionsProbe(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public Collection<PluginExecution> probeIn(MavenProject mavenProject) {
        Map<String, Plugin> plugins = mavenProject.getBuild().getPluginsAsMap();
        String warPluginKey = Plugin.constructKey(groupId, artifactId);
        if (plugins.containsKey(warPluginKey)) {
            Plugin plugin = plugins.get(warPluginKey);
            return plugin.getExecutions();
        }else {
            return Collections.emptyList();
        }
    }

    public List<Path> defaultsForMissingConfigurationInExecution() {
        return Collections.singletonList(Paths.get("src/main/webapp"));
    }
}