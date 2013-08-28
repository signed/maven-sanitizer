package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Critic;
import com.github.signed.maven.sanitizer.pom.Action;
import org.apache.maven.model.Dependency;

public class DependenciesInTestScope implements Critic<Dependency> {
    @Override
    public void criticise(Dependency dependency, Action<Dependency> transformations) {
        if("test".equals(dependency.getScope())){
            transformations.execute(dependency);
        }
    }
}
