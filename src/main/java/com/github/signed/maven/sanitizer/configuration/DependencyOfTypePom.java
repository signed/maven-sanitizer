package com.github.signed.maven.sanitizer.configuration;

import org.apache.maven.model.Dependency;
import com.google.common.base.Predicate;

public class DependencyOfTypePom implements Predicate<Dependency> {
    @Override
    public boolean apply(Dependency input) {
        return "pom".equals(input.getType());
    }
}
