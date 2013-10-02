package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import org.apache.maven.model.Dependency;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DependencyMatching_Test {

    @SuppressWarnings("unchecked")
    private final Action<Dependency> mock = mock(Action.class);
    private final DependencyMatching dependencyMatching = new DependencyMatching("org.example", "artifact", "zip");

    @Test
    public void executeActionOnExactMatch() throws Exception {
        Dependency dependency = DependencyBuilder.anyDependency().withGroupId("org.example").withArtifactId("artifact").withType("zip").build();
        match(dependency);

        verify(mock).perform(dependency);
    }

    @Test
    public void doNotExecuteTheActionOnTypeMismatch() throws Exception {
        DependencyBuilder dependencyBuilder = DependencyBuilder.anyDependency().withGroupId("org.example").withArtifactId("artifact").withType("jar");
        match(dependencyBuilder.build());

        verifyZeroInteractions(mock);
    }

    private void match(Dependency dependency) {
        dependencyMatching.executeActionOnMatch(dependency, mock, mock(DiagnosticsWriter.class), mock(InfectedProject.class));
    }
}
