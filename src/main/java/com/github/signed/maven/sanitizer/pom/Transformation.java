package com.github.signed.maven.sanitizer.pom;

public interface Transformation<T> {
    void execute(T element);
}
