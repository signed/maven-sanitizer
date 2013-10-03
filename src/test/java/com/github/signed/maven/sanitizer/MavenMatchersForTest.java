package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Dependency;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.hasItem;

public class MavenMatchersForTest {
    public static Matcher<Iterable<? super Dependency>> containsDependency(String groupId, String artifactId) {
        return hasItem(new DependencyMatcher(groupId, artifactId));
    }
}
