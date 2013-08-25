package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Strings;
import com.github.signed.maven.sanitizer.pom.plugins.PluginTransformations;
import org.apache.maven.model.Plugin;

import java.util.Iterator;

public class DefaultPluginTransformations implements PluginTransformations {
    private final Strings strings = new Strings();
    private final Iterable<Plugin> plugins;

    public DefaultPluginTransformations(Iterable<Plugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public void drop(Plugin toDrop) {
        Iterator<Plugin> pluginIterator = plugins.iterator();
        while (pluginIterator.hasNext()){
            Plugin plugin = pluginIterator.next();
            if(!sameGroupId(plugin, toDrop)){
                continue;
            }
            if( !sameArtifactId(plugin, toDrop)){
                continue;
            }
            pluginIterator.remove();
        }
    }

    private boolean sameGroupId(Plugin plugin, Plugin toDrop) {
        return strings.matching(plugin.getGroupId(), toDrop.getGroupId());
    }

    private boolean sameArtifactId(Plugin plugin, Plugin toDrop) {
        return strings.matching(plugin.getArtifactId(), toDrop.getArtifactId());
    }
}
