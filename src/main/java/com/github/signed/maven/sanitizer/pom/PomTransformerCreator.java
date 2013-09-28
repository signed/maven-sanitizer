package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencies;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesFromDependencyManagement;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import java.util.Collections;
import java.util.List;

public class PomTransformerCreator {

    private final CopyPom copyPom;

    public PomTransformerCreator(CopyPom copyPom) {
        this.copyPom = copyPom;
    }

    public void addDependencyTransformation(Selector<Dependency> selector, Action<Dependency> action) {
        List<Extractor<Dependency>> extractors = Collections.<Extractor<Dependency>>singletonList(new DependenciesFromDependencies());
        copyPom.addTransformer(new DefaultModelTransformer<>(selector, action, MavenMatchers.<Model>anything(), extractors));
        final DependenciesFromDependencyManagement dependenciesFromDependencyManagement = new DependenciesFromDependencyManagement();
        List<Extractor<Dependency>> extractorys = Collections.<Extractor<Dependency>>singletonList(dependenciesFromDependencyManagement);
        copyPom.addTransformer(new DefaultModelTransformer<>(selector, action, MavenMatchers.<Model>anything(), extractorys));
    }
}