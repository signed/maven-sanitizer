package com.github.signed.maven.sanitizer.pom;

public interface Selector<T> {
    void executeActionOnMatch(T candidate, Action<T> action);
}
