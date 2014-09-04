package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.Selector;

public class NotSelector<MavenModelElement> implements Selector<MavenModelElement> {

    public static <MavenModelElement> Selector<MavenModelElement> not(Selector<MavenModelElement> selector) {
        return new NotSelector<>(selector);
    }

    private final Selector<MavenModelElement> wrapped;

    public NotSelector(Selector<MavenModelElement> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean executeActionOnMatch(Patient<MavenModelElement> patient, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        return !wrapped.executeActionOnMatch(patient, diagnosticsWriter, infectedProject);
    }
}
