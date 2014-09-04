package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.Selector;

public class DependenciesWithInheritedVersion implements Selector<Dependency> {
    @Override
    public void executeActionOnMatch(Patient<Dependency> candidate, Action<Dependency> action, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        if( null == candidate.getAsWritten().getVersion()){
            action.perform(candidate.fullyPopulated());
        }
    }
}
