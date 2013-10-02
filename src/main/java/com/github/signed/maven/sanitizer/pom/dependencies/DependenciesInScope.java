package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
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
    public void executeActionOnMatch(Dependency candidate, Action<Dependency> action, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        if (scope.equals(candidate.getScope())) {
            action.perform(candidate);
        }
    }
}
