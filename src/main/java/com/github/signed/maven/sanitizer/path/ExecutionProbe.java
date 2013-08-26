package com.github.signed.maven.sanitizer.path;

import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExecutionProbe {

    private final String groupId;
    private final String artifactId;
    private final List<Path> defaults;
    private final String configurationElement;

    public ExecutionProbe(String groupId, String artifactId, List<Path> defaults, String configurationElement) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.defaults = defaults;
        this.configurationElement = configurationElement;
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
        return defaults;
    }

    public String configurationElement() {
        return configurationElement;
    }
}