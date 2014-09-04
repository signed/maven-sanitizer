package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;

public interface Selector<T> {
    boolean executeActionOnMatch(Patient<T> patient, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject);
}
