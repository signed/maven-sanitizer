package com.github.signed.maven.sanitizer.configuration;

import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.DefaultModelTransformer;
import com.github.signed.maven.sanitizer.pom.Extractor;
import com.github.signed.maven.sanitizer.pom.ModelTransformer;
import com.github.signed.maven.sanitizer.pom.Selector;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManagement;
import com.github.signed.maven.sanitizer.pom.dependencies.DropDependency;
import com.google.common.collect.Lists;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.hamcrest.Matcher;

import java.util.List;

import static com.github.signed.maven.sanitizer.pom.NoAction.noAction;
import static com.github.signed.maven.sanitizer.pom.NoSelection.nothing;

public class ForDependencyReferences {

    public static ForDependencyReferences inAllModules() {
        return new ForDependencyReferences();
    }

    private final Matcher<Model> moduleMatcher = MavenMatchers.anything();
    private final List<Extractor<Dependency>> extractors = Lists.newArrayList();
    private Selector<Dependency> selector = nothing();
    private Action<Dependency> action = noAction();

    public ForDependencyReferences referencesTo(Selector<Dependency> selector) {
        this.selector = selector;
        return this;
    }

    public ForDependencyReferences focusOnActualDependenciesAndDependencyManagement() {
        this.extractors.add(new DependenciesFromDependencyManagement());
        this.extractors.add(new DependenciesFromDependencies());
        return this;
    }

    public ForDependencyReferences drop() {
        this.action = new DropDependency();
        return this;
    }

    public ModelTransformer build() {
        return new DefaultModelTransformer<>(this.selector, this.action, moduleMatcher, this.extractors);
    }
}