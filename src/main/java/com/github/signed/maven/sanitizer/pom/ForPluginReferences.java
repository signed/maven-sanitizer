package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.dependencies.DropPlugin;
import com.github.signed.maven.sanitizer.pom.plugins.PluginByGroupIdArtifactId;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromBuild;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromPluginManagement;
import com.google.common.collect.Lists;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.hamcrest.Matcher;

import java.util.List;

import static com.github.signed.maven.sanitizer.pom.NoAction.noAction;
import static com.github.signed.maven.sanitizer.pom.NoSelection.nothing;

public class ForPluginReferences {

    public static ForPluginReferences inAllModules() {
        return new ForPluginReferences();
    }

    private Selector<Plugin> selector = nothing();
    private Action<Plugin> action = noAction();
    private Matcher<Model> modelMatcher = MavenMatchers.anything();
    private List<Extractor<Plugin>> extractors = Lists.newArrayList();

    public ForPluginReferences onlyTargetModulesMatching(Matcher<Model> modelMatcher) {
        this.modelMatcher = modelMatcher;
        return this;
    }

    public ForPluginReferences referencesTo(Selector<Plugin> selector) {
        this.selector = selector;
        return this;
    }

    public ForPluginReferences focusOnPluginsInBuildAndPluginManagementSection() {
        this.extractors.add(new PluginsFromBuild());
        this.extractors.add(new PluginsFromPluginManagement());
        return this;
    }

    public ForPluginReferences referencesTo(String groupId, String artifactId) {
        return referencesTo(new PluginByGroupIdArtifactId(groupId, artifactId));
    }

    public ForPluginReferences drop() {
        this.action = new DropPlugin();
        return this;
    }

    public ModelTransformer create() {
        return new DefaultModelTransformer<>(selector, action, modelMatcher, extractors);
    }
}
