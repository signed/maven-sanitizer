package com.github.signed.maven.sanitizer.path;

import com.google.common.collect.Iterables;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WarWebAppDirectory implements PathsProvider {

    private final ExecutionsProbe probe;

    public WarWebAppDirectory(ExecutionsProbe probe) {
        this.probe = probe;
    }

    @Override
    public Iterable<Path> paths(final MavenProject mavenProject) {
        Collection<PluginExecution> executions = probe.probeIn(mavenProject);
        if (executions.isEmpty()) {
            return Collections.emptyList();
        }
        List<Path> foundPath = new ArrayList<>();
        Iterable<Path> defaultPaths = probe.defaultsForMissingConfigurationInExecution();
        for (PluginExecution execution : executions) {
            List<Path> discovered = retrieveExplicitConfiguration(execution);
            if (discovered.isEmpty()) {
                Iterables.addAll(foundPath, defaultPaths);
            } else {
                foundPath.addAll(discovered);
            }
        }
        return Iterables.transform(foundPath, new ResolveToProjectBaseDirectory(mavenProject));
    }

    private List<Path> retrieveExplicitConfiguration(PluginExecution execution) {
        Xpp3Dom configuration = (Xpp3Dom) execution.getConfiguration();
        if (null != configuration) {
            Xpp3Dom element = configuration.getChild(configurationElement());
            if (null != element) {
                if (element.getChildCount() > 0) {
                    List<Path> found = new ArrayList<>();
                    for (Xpp3Dom child : element.getChildren()) {
                        found.add(Paths.get(child.getValue()));
                    }
                    return found;
                } else {
                    String value = element.getValue();
                    return Collections.singletonList(Paths.get(value));
                }
            }
        }
        return Collections.emptyList();
    }

    private String configurationElement() {
        return "warSourceDirectory";
    }
}