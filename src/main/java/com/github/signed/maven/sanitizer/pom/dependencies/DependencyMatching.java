package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.Matchers;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Selector;
import com.github.signed.maven.sanitizer.pom.Strings;
import org.apache.maven.model.Dependency;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DependencyMatching implements Selector<Dependency> {

    public static DependencyMatching dependencyWith(String groupId, String artifactId){
        return new DependencyMatching(new MavenStringMatcher(groupId),new MavenStringMatcher(artifactId), Matchers.<String>always());
    }

    public static DependencyMatching dependencyWith(String groupId, String artifactId, String type) {
        return new DependencyMatching(new MavenStringMatcher(groupId), new MavenStringMatcher(artifactId), new MavenStringMatcher(type));
    }

    private final Matcher<String> groupIdMatcher;
    private final Matcher<String> artifactIdMatcher;
    private final Matcher<String> typeMatcher;

    private DependencyMatching(Matcher<String> groupIdMatcher, Matcher<String> artifactIdMatcher, Matcher<String> typeMatcher) {
        this.groupIdMatcher = groupIdMatcher;
        this.artifactIdMatcher = artifactIdMatcher;
        this.typeMatcher = typeMatcher;
    }

    @Override
    public void executeActionOnMatch(final Dependency candidate, Action<Dependency> action, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        if( !groupIdMatcher.matches(candidate.getGroupId())){
            return;
        }
        if( !artifactIdMatcher.matches(candidate.getArtifactId())){
            return;
        }
        if( !typeMatcher.matches(candidate.getType())){
            return;
        }

        action.perform(candidate);
    }

    public static class MavenStringMatcher extends TypeSafeMatcher<String> {
        private final Strings strings = new Strings();
        private final String expected;

        public MavenStringMatcher(String expected) {
            this.expected = expected;
        }

        @Override
        protected boolean matchesSafely(String item) {
            return strings.matching(item, expected);
        }

        @Override
        public void describeTo(Description description) {

        }
    }
}
