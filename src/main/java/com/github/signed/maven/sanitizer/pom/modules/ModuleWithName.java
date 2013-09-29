package com.github.signed.maven.sanitizer.pom.modules;

import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.Selector;

public class ModuleWithName implements Selector<Module> {
    public ModuleWithName(Module module) {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Override
    public void executeActionOnMatch(Module element, Action<Module> action) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
