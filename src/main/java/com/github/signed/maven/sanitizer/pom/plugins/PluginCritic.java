package com.github.signed.maven.sanitizer.pom.plugins;

import com.github.signed.maven.sanitizer.pom.Transformation;
import org.apache.maven.model.Plugin;

public interface PluginCritic {
    void criticise(Plugin plugin, Transformation<Plugin> transformations);
}
