package com.github.signed.maven.sanitizer.pom.modules;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.Selector;

public class ModuleWithName implements Selector<Module> {
    private final Module module;

    public ModuleWithName(Module module) {
        this.module = module;
    }

    @Override
    public boolean executeActionOnMatch(Patient<Module> candidate, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        if (module.equals(candidate.fullyPopulated())) {
            diagnosticsWriter.ignorePathAndEverythingBelow(infectedProject.resolvePathFor(module));
            return true;
        }
        return false;
    }
}