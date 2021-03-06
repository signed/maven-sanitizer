package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Strings;
import org.apache.maven.model.Plugin;

import java.util.Iterator;

public class DropPlugin implements Action<Plugin> {
    private final Strings strings = new Strings();
    private Iterable<Plugin> plugins;

    @Override
    public void performOn(Iterable<Plugin> elements) {
        plugins = elements;
    }

    @Override
    public void perform(Plugin toDrop, InfectedProject infectedProject) {
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
