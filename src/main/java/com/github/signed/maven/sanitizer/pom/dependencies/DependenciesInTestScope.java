package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Selector;
import com.github.signed.maven.sanitizer.pom.Action;
import org.apache.maven.model.Dependency;

public class DependenciesInTestScope implements Selector<Dependency> {
    @Override
    public void criticise(Dependency dependency, Action<Dependency> transformations) {
        if("test".equals(dependency.getScope())){
            transformations.execute(dependency);
        }
    }
}
