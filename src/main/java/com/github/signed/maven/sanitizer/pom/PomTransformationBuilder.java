package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.MavenMatchers;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.hamcrest.Matcher;

public class PomTransformationBuilder {

    private Selector<Plugin> selector;
    private Extractor<Plugin> extractor;
    private Action<Plugin> action;
    private Matcher<Model> modelMatcher = MavenMatchers.anything();

    public PomTransformationBuilder onlyTargetModulesMatching(Matcher<Model> modelMatcher) {
        this.modelMatcher = modelMatcher;
        return this;
    }

    public PomTransformationBuilder extract(Extractor<Plugin> extractor) {
        this.extractor = extractor;
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

    public ModelTransformer create() {
        return new DefaultModelTransformer<>(selector, extractor, action, modelMatcher);
    }
}
