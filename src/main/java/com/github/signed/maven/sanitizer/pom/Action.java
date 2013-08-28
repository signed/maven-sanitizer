package com.github.signed.maven.sanitizer.pom;

public interface Action<T> {

    void performOn(Iterable<T> elements);

    void execute(T element);
}
