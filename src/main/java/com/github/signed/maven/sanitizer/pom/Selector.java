package com.github.signed.maven.sanitizer.pom;

public interface Selector<T> {
    void executeActionOnMatch(T element, Action<T> action);
}
