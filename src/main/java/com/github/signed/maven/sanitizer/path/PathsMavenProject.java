package com.github.signed.maven.sanitizer.path;

import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PathsMavenProject {

    private final MavenProject mavenProject;

    public PathsMavenProject(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    public List<PluginExecution> executionsFor(String groupId, String artifactId) {
        Map<String, Plugin> plugins = mavenProject.getBuild().getPluginsAsMap();
        String warPluginKey = Plugin.constructKey(groupId, artifactId);
        if (plugins.containsKey(warPluginKey)) {
            Plugin plugin = plugins.get(warPluginKey);
            return plugin.getExecutions();
        }else {
            return Collections.emptyList();
        }
    }
}