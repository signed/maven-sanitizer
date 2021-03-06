package com.github.signed.maven.sanitizer.pom.modules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;
import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.FullyPopulatedOnly;
import com.github.signed.maven.sanitizer.pom.InfectedProject;

public class ModuleWithName_Test {
    @SuppressWarnings("unchecked")
    private final Action<Module> action = mock(Action.class);
    private final ModuleWithName selector = new ModuleWithName(new Module("to-drop"));
    private final DiagnosticsWriter diagnosticsWriter = mock(DiagnosticsWriter.class);
    private final InfectedProject infectedProject = mock(InfectedProject.class);

    @Test
    public void executeActionIfModuleNameMatchesExpectedName() throws Exception {
        Module candidate = new Module("to-drop");
        FullyPopulatedOnly<Module> patient = new FullyPopulatedOnly<>(candidate);

        assertThat("should signal match", selector.executeActionOnMatch(patient, diagnosticsWriter, infectedProject));
    }

    @Test
    public void doNotExecuteActionOnDifferentModuleName() throws Exception {
        FullyPopulatedOnly<Module> patient = new FullyPopulatedOnly<>(new Module("to-keep"));
        selector.executeActionOnMatch(patient, diagnosticsWriter, infectedProject);

        Mockito.verifyZeroInteractions(action);
    }
}
