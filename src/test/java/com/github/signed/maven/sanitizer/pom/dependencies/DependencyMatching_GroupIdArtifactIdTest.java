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

public class DependencyMatching_GroupIdArtifactIdTest {

    @SuppressWarnings("unchecked")
    private final Action<Dependency> mock = mock(Action.class);
    private final DependencyMatching dependencyMatching = DependencyMatching.dependencyWith("org.example", "artifact");
    private final DependencyBuilder builder = DependencyBuilder.anyDependency().withGroupId("org.example").withArtifactId("artifact");

    @Test
    public void reportExactMatch() throws Exception {
        Dependency dependency = builder.build();

        assertThat("Should be an exact match", match(dependency));
    }

    @Test
    public void doNotExecuteOnArtifactMissmatch() throws Exception {
        match(builder.withArtifactId("another").build());

        verifyZeroInteractions(mock);
    }

    @Test
    public void doNotExecuteOnGroupIdMissmatch() throws Exception {
        match(builder.withGroupId("org.another").build());

        verifyZeroInteractions(mock);
    }

    private boolean match(Dependency dependency) {
        FullyPopulatedOnly<Dependency> patient = new FullyPopulatedOnly<>(dependency);
        return dependencyMatching.executeActionOnMatch(patient, mock(DiagnosticsWriter.class), mock(InfectedProject.class));
    }
}
