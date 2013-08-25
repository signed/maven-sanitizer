package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Transformation;
import org.apache.maven.model.Dependency;

public class DropDependenciesInTestScope implements DependencyCritic {
    @Override
    public void criticise(Dependency dependency, Transformation<Dependency> transformations) {
        if("test".equals(dependency.getScope())){
            transformations.execute(dependency);
        }
    }
}
