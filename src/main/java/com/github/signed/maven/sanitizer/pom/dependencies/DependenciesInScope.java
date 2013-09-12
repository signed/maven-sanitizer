package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Selector;
import com.github.signed.maven.sanitizer.pom.Action;
import org.apache.maven.model.Dependency;

public class DependenciesInScope implements Selector<Dependency> {
    private final String scope;

    public static DependenciesInScope Test() {
        return new DependenciesInScope("test");
    }

    public static DependenciesInScope Runtime() {
        return new DependenciesInScope("runtime");
    }

    private DependenciesInScope(String scope) {
        this.scope = scope;
    }

    @Override
    public void executeActionOnMatch(Dependency dependency, Action<Dependency> action) {
        if (scope.equals(dependency.getScope())) {
            action.perform(dependency);
        }
    }
}
