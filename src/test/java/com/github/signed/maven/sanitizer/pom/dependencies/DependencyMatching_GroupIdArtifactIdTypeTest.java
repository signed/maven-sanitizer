package com.github.signed.maven.sanitizer.pom.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.apache.maven.model.Dependency;
import org.junit.Test;
import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.FullyPopulatedOnly;
import com.github.signed.maven.sanitizer.pom.InfectedProject;

public class DependencyMatching_GroupIdArtifactIdTypeTest {

    @SuppressWarnings("unchecked")
    private final Action<Dependency> mock = mock(Action.class);
    private final DependencyMatching dependencyMatching = DependencyMatching.dependencyWith("org.example", "artifact", "zip");

    @Test
    public void executeActionOnExactMatch() throws Exception {
        Dependency dependency = DependencyBuilder.anyDependency().withGroupId("org.example").withArtifactId("artifact").withType("zip").build();

        assertThat("should be an exact match", match(dependency));
    }

    @Test
    public void doNotExecuteTheActionOnTypeMismatch() throws Exception {
        DependencyBuilder dependencyBuilder = DependencyBuilder.anyDependency().withGroupId("org.example").withArtifactId("artifact").withType("jar");
        match(dependencyBuilder.build());

        verifyZeroInteractions(mock);
    }

    private boolean match(Dependency dependency) {
        FullyPopulatedOnly<Dependency> patient = new FullyPopulatedOnly<>(dependency);
        return dependencyMatching.executeActionOnMatch(patient, mock, mock(DiagnosticsWriter.class), mock(InfectedProject.class));
    }
}
