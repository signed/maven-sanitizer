package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManagement;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromBuild;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromPluginManagement;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.hamcrest.Matcher;

public class ConfigurationCreator {

    private final CopyPom copyPom;

    public ConfigurationCreator(CopyPom copyPom) {
        this.copyPom = copyPom;
    }

    public void addPluginTransformation(Selector<Plugin> selector, Action<Plugin> action) {
        addPluginTransformation(selector, action, MavenMatchers.<Model>anything());
    }

    public void addDependencyTransformation(Selector<Dependency> selector, Action<Dependency> action) {
        copyPom.addTransformer(new DefaultModelTransformer<>(selector, new DependenciesFromDependencies(), action, MavenMatchers.<Model>anything()));
        copyPom.addTransformer(new DefaultModelTransformer<>(selector, new DependenciesFromDependencyManagement(), action, MavenMatchers.<Model>anything()));
    }

    public void addPluginTransformation(Selector<Plugin> selector, Action<Plugin> action, Matcher<Model> matcher) {
        copyPom.addTransformer(new DefaultModelTransformer<>(selector, new PluginsFromBuild(), action, matcher));
        copyPom.addTransformer(new DefaultModelTransformer<>(selector, new PluginsFromPluginManagement(), action, matcher));
    }
}
