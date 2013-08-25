package com.github.signed.maven.sanitizer.pom.plugins;

import org.apache.maven.model.Plugin;

public interface PluginTransformations {
    void drop(Plugin plugin);
}
