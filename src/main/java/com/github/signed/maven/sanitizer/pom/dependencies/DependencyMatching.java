package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.Selector;
import com.github.signed.maven.sanitizer.pom.Strings;

public class DependencyMatching implements Selector<Dependency> {

    public static DependencyMatching dependencyWith(String groupId, String artifactId){
        return new DependencyMatching(new MavenStringMatcher(groupId),new MavenStringMatcher(artifactId), MavenMatchers.<String>anything());
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
    public void executeActionOnMatch(final Patient<Dependency> patient, Action<Dependency> action, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        if( !groupIdMatcher.matches(patient.fullyPouplated().getGroupId())){
            return;
        }
        if( !artifactIdMatcher.matches(patient.fullyPouplated().getArtifactId())){
            return;
        }
        if( !typeMatcher.matches(patient.fullyPouplated().getType())){
            return;
        }

        action.perform(patient.fullyPouplated());
    }

    public static class MavenStringMatcher extends TypeSafeMatcher<String> {
        private final Strings strings = new Strings();
        private final String expected;

        public MavenStringMatcher(String expected) {
            this.expected = expected;
        }

        @Override
        protected boolean matchesSafely(String item) {
            return strings.matching(item, expected);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void describeTo(Description description) {

        }
    }
}
