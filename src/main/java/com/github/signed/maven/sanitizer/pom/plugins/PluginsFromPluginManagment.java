package com.github.signed.maven.sanitizer.pom.plugins;

import com.github.signed.maven.sanitizer.pom.Extractor;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;

import java.util.Collections;
import java.util.List;

public class PluginsFromPluginManagment implements Extractor<Plugin> {
    @Override
    public Iterable<Plugin> elements(Model model) {
        Build build = model.getBuild();
        if(null == build){
            return Collections.emptyList();
        }
        PluginManagement pluginManagement = build.getPluginManagement();
        if(null == pluginManagement){
            return Collections.emptyList();
        }
        List<Plugin> plugins = pluginManagement.getPlugins();
        if(null == plugins){
            return Collections.emptyList();
        }
        return plugins;
    }
}
