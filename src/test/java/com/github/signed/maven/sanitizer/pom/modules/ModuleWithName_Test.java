package com.github.signed.maven.sanitizer.pom.modules;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.Action;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ModuleWithName_Test {
    @SuppressWarnings("unchecked")
    private final Action<Module> action = mock(Action.class);
    private final ModuleWithName selector = new ModuleWithName(new Module("to-drop"));
    private final DiagnosticsWriter diagnosticsWriter = mock(DiagnosticsWriter.class);

    @Test
    public void executeActionIfModuleNameMatchesExpectedName() throws Exception {
        Module candidate = new Module("to-drop");
        selector.executeActionOnMatch(candidate, action, diagnosticsWriter);

        verify(action).perform(candidate);
    }

    @Test
    public void doNotExecuteActionOnDifferentModuleName() throws Exception {
        Module candidate = new Module("to-keep");
        selector.executeActionOnMatch(candidate, action, diagnosticsWriter);

        Mockito.verifyZeroInteractions(action);
    }
}
