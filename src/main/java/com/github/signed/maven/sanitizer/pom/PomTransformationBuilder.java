package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromBuild;
import com.github.signed.maven.sanitizer.pom.plugins.PluginsFromPluginManagement;
import com.google.common.collect.Lists;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.hamcrest.Matcher;

import java.util.List;

public class PomTransformationBuilder {

    private Selector<Plugin> selector;
    private Action<Plugin> action;
    private Matcher<Model> modelMatcher = MavenMatchers.anything();
    private List<Extractor<Plugin>> extractors = Lists.newArrayList();

    public PomTransformationBuilder onlyTargetModulesMatching(Matcher<Model> modelMatcher) {
        this.modelMatcher = modelMatcher;
        return this;
    }

    public PomTransformationBuilder extract(Extractor<Plugin> extractor) {
        this.extractors.add(extractor);
        return this;
    }

    public PomTransformationBuilder targetElementsMatching(Selector<Plugin> selector) {
        this.selector = selector;
        return this;
    }

    public PomTransformationBuilder andPerform(Action<Plugin> action) {
        this.action = action;
        return this;
    }

    public PomTransformationBuilder focusOnPluginsInBuildSection() {
        return extract(new PluginsFromBuild());
    }

    public PomTransformationBuilder focusOnPluginsInPluginManagmentSection() {
        return extract(new PluginsFromPluginManagement());
    }

    public PomTransformationBuilder focusOnPluginsInBuildAndPluginManagmentSection() {
        return focusOnPluginsInBuildSection().focusOnPluginsInPluginManagmentSection();
    }

    public ModelTransformer create() {
        return new DefaultModelTransformer<>(selector, action, modelMatcher, extractors);
    }
}
