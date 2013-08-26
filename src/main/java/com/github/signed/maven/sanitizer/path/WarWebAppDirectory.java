package com.github.signed.maven.sanitizer.path;

import com.github.signed.maven.sanitizer.path.PathsProvider;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class WarWebAppDirectory implements PathsProvider {
    @Override
    public Iterable<Path> paths(MavenProject mavenProject) {
        if ("war".equals(mavenProject.getPackaging())) {
            Path webAppDirectory = Paths.get("src/main/webapp");
            Map<String, Plugin> plugins = mavenProject.getBuild().getPluginsAsMap();
            String warPluginKey = Plugin.constructKey("org.apache.maven.plugins", "maven-war-plugin");
            if (plugins.containsKey(warPluginKey)) {
                Plugin plugin = plugins.get(warPluginKey);
                for (PluginExecution execution : plugin.getExecutions()) {
                    Xpp3Dom configuration = (Xpp3Dom) execution.getConfiguration();
                    if (null != configuration) {
                        Xpp3Dom warSourceDirectory = configuration.getChild("warSourceDirectory");
                        if (null != warSourceDirectory) {
                            webAppDirectory = Paths.get(warSourceDirectory.getValue());
                        }
                    }
                }
            }

            webAppDirectory = mavenProject.getBasedir().toPath().resolve(webAppDirectory);
            return Collections.singletonList(webAppDirectory);
        } else {
            return Collections.emptyList();
        }
    }
}
