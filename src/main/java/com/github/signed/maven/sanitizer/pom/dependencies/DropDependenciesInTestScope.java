package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;

public class DropDependenciesInTestScope implements DependencyCritic {
    @Override
    public void criticise(Dependency dependency, DependencyTransformations transformations) {
        if("test".equals(dependency.getScope())){
            transformations.drop(dependency);
        }
    }
}
