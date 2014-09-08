package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.configuration.DependencyOfTypePom;
import com.google.common.base.Predicate;

public class DependencyGuava {
    public static Predicate<Dependency> dependenciesOfTypePom() {
        return new DependencyOfTypePom();
    }
}
