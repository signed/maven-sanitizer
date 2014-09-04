package com.github.signed.maven.sanitizer.pom.modules;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;
import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;

public class ModuleWithName_Test {
    @SuppressWarnings("unchecked")
    private final Action<Module> action = mock(Action.class);
    private final ModuleWithName selector = new ModuleWithName(new Module("to-drop"));
    private final DiagnosticsWriter diagnosticsWriter = mock(DiagnosticsWriter.class);
    private final InfectedProject infectedProject = mock(InfectedProject.class);

    @Test
    public void executeActionIfModuleNameMatchesExpectedName() throws Exception {
        Module candidate = new Module("to-drop");

        Patient<Module> patient = new Patient<>(null, candidate);
        selector.executeActionOnMatch(patient, action, diagnosticsWriter, infectedProject);

        verify(action).perform(candidate);
    }

    @Test
    public void doNotExecuteActionOnDifferentModuleName() throws Exception {
        Patient<Module> patient = new Patient<>(null, new Module("to-keep"));
        selector.executeActionOnMatch(patient, action, diagnosticsWriter, infectedProject);

        Mockito.verifyZeroInteractions(action);
    }
}
