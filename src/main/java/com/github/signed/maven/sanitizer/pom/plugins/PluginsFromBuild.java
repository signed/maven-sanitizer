package com.github.signed.maven.sanitizer.pom.plugins;

import com.github.signed.maven.sanitizer.pom.Extractor;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;

import java.util.Collections;
import java.util.List;

public class PluginsFromBuild implements Extractor<Plugin> {

    @Override
    public Iterable<Plugin> elements(Model model) {
        Build build = model.getBuild();
        if( null == build){
            return Collections.emptyList();
        }
        List<Plugin> plugins = build.getPlugins();
        if(null == plugins) {
            return Collections.emptyList();
        }
        return plugins;
    }
}
