package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Dependency;

public class DropDependenciesInTestScope implements CopyProject.DependencyCritic {
    @Override
    public void criticise(Dependency dependency, CopyProject.DependencyTransformations transformations) {
        if("test".equals(dependency.getScope())){
            transformations.drop(dependency);
        }
    }
}
