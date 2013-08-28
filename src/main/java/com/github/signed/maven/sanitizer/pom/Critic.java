package com.github.signed.maven.sanitizer.pom;

public interface Critic<T> {
    void criticise(T element, Action<T> action);
}
