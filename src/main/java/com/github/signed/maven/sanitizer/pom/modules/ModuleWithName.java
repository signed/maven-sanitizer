package com.github.signed.maven.sanitizer.pom.modules;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.Selector;

public class ModuleWithName implements Selector<Module> {
    private final Module module;

    public ModuleWithName(Module module) {
        this.module = module;
    }

    @Override
    public void executeActionOnMatch(Patient<Module> candidate, Action<Module> action, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        if (module.equals(candidate.fullyPouplated())) {
            diagnosticsWriter.ignorePathAndEverythingBelow(infectedProject.resolvePathFor(module));
            action.perform(candidate.fullyPouplated());
        }
    }
}