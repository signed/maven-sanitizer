package com.github.signed.maven.sanitizer.pom;

public class NoSelection<T> implements Selector<T> {

    public static <T> Selector<T> nothing() {
        return new NoSelection<>();
    }

    @Override
    public void executeActionOnMatch(T element, Action<T> action) {
        //do nothing;
    }
}
