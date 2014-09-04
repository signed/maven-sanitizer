package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;

public interface Selector<T> {
    void executeActionOnMatch(Patient<T> patient, Action<T> action, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject);
}
