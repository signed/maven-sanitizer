package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.Selector;

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
    public boolean executeActionOnMatch(Patient<Dependency> patient, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        return scope.equals(patient.fullyPopulated().getScope());
    }
}
