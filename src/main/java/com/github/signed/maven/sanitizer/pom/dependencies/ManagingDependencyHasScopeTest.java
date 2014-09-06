package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.Selector;
import com.google.common.base.Optional;

public class ManagingDependencyHasScopeTest implements Selector<Dependency> {
    @Override
    public boolean executeActionOnMatch(Patient<Dependency> patient, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        Optional<Dependency> managingDependency = infectedProject.getEntryInDependencyManagementFor(patient.getAsWritten());
        return managingDependency.isPresent() && "test".equals(managingDependency.get().getScope());
    }
}
