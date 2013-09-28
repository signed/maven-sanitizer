package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManagement;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
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

    public ForDependencyReferences referencesTo(Selector<Dependency> selector) {
        this.selector = selector;
        return this;
    }

    public ForDependencyReferences focusOnActualDependencies() {
        this.extractors.add(new DependenciesFromDependencies());
        return this;
    }

    public ForDependencyReferences focusOnDependenciesInDependencyManagment() {
        this.extractors.add(new DependenciesFromDependencyManagement());
        return this;
    }

    public ForDependencyReferences focusOnActualDependenciesAndDependencyManagment() {
        return focusOnDependenciesInDependencyManagment().focusOnActualDependencies();
    }

    public ForDependencyReferences drop() {
        return perform(new DropDependency());
    }
}