package com.github.signed.maven.sanitizer.pom.modules;

import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.Selector;

public class ModuleWithName implements Selector<Module> {
    private final Module module;

    public ModuleWithName(Module module) {
        this.module = module;
    }

    @Override
    public void executeActionOnMatch(Module element, Action<Module> action) {
        if (module.equals(element)) {
            action.perform(element);
        }
    }
}