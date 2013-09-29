package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;

public class NoSelection<T> implements Selector<T> {

    public static <T> Selector<T> nothing() {
        return new NoSelection<>();
    }

    @Override
    public void executeActionOnMatch(T candidate, Action<T> action, DiagnosticsWriter diagnosticsWriter) {
        //do nothing;
    }
}
