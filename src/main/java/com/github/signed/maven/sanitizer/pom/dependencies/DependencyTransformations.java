package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;

public interface DependencyTransformations {
    void drop(Dependency dependency);
}
