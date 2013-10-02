package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Dependency;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DependencyMatcher extends TypeSafeMatcher<Dependency> {

    public static DependencyMatcher dependency(String groupId, String artifactId) {
        return new DependencyMatcher(groupId, artifactId);
    }

    private final String groupId;
    private final String artifactId;

    public DependencyMatcher(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    @Override
    protected boolean matchesSafely(Dependency item) {
        return item.getGroupId().equals(groupId) && item.getArtifactId().equals(artifactId);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("dependency ").appendValue(groupId + ":" + artifactId + ":*:*");
    }

    @Override
    protected void describeMismatchSafely(Dependency item, Description mismatchDescription) {
        mismatchDescription.appendValue(item.getGroupId() + ":" + item.getArtifactId() + ":" + item.getVersion() + ":" + item.getClassifier());
    }
}
