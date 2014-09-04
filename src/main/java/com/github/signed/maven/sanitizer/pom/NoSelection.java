package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;

public class NoSelection<T> implements Selector<T> {

    public static <T> Selector<T> nothing() {
        return new NoSelection<>();
    }

    @Override
    public boolean executeActionOnMatch(Patient<T> candidate, Action<T> action, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        return false;
    }
}
