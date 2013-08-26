package com.github.signed.maven.sanitizer.path;

import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class WarWebAppDirectory implements PathsProvider {
    @Override
    public Iterable<Path> paths(MavenProject mavenProject) {
        List<PluginExecution> executions = new PathsMavenProject(mavenProject).executionsFor("org.apache.maven.plugins", "maven-war-plugin");
        if (executions.isEmpty()) {
            return Collections.emptyList();
        }

        Path webAppDirectory = Paths.get("src/main/webapp");
        for (PluginExecution execution : executions) {
            Xpp3Dom configuration = (Xpp3Dom) execution.getConfiguration();
            if (null != configuration) {
                Xpp3Dom warSourceDirectory = configuration.getChild("warSourceDirectory");
                if (null != warSourceDirectory) {
                    webAppDirectory = Paths.get(warSourceDirectory.getValue());
                }
            }
        }
        webAppDirectory = mavenProject.getBasedir().toPath().resolve(webAppDirectory);
        return Collections.singletonList(webAppDirectory);
    }
}