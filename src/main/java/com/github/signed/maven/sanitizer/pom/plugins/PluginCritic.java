package com.github.signed.maven.sanitizer.pom.plugins;

import org.apache.maven.model.Plugin;

public interface PluginCritic {
    void criticise(Plugin plugin, PluginTransformations transformations);
}
