package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Transformation;
import org.apache.maven.model.Dependency;

public interface DependencyCritic {
    void criticise(Dependency dependency, Transformation<Dependency> transformations);
}
