package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManagement;
import com.google.common.collect.Lists;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.hamcrest.Matcher;

import java.util.List;

public class ForDependencyReferences {

    private final Matcher<Model> moduleMatcher = MavenMatchers.<Model>anything();
    private Selector<Dependency> selector;
    private Action<Dependency> action;

    public static ForDependencyReferences inAllModules() {
        return new ForDependencyReferences();
    }

    private final List<Extractor<Dependency>> extractors = Lists.newArrayList();

    public ModelTransformer build() {
        return new DefaultModelTransformer<>(this.selector, this.action, moduleMatcher, this.extractors);
    }

    public ForDependencyReferences perform(Action<Dependency> action) {
        this.action = action;
        return this;
    }

    public ForDependencyReferences focusOn(Selector<Dependency> selector) {
        this.selector = selector;
        return this;
    }

    public ForDependencyReferences focusOnActualDependencies() {
        this.extractors.add(new DependenciesFromDependencies());
        return this;
    }

    public void theMethod(Selector<Dependency> selector, Action<Dependency> action, CopyPom copyPom) {
        copyPom.addTransformer(focusOnActualDependencies().focusOn(selector).perform(action).build());
    }

    public void theOtherMethod(Selector<Dependency> selector, Action<Dependency> action, CopyPom copyPom) {
        copyPom.addTransformer(focusOnDependenciesInDependencyManagment().focusOn(selector).perform(action).build());
    }

    public ForDependencyReferences focusOnDependenciesInDependencyManagment() {
        this.extractors.add(new DependenciesFromDependencyManagement());
        return this;
    }
}