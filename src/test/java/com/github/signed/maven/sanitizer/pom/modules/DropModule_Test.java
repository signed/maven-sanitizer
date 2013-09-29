package com.github.signed.maven.sanitizer.pom.modules;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class DropModule_Test {
    private final List<Module> modules = Lists.newArrayList();
    private final DropModule dropModule = new DropModule();

    @Before
    public void addModules() throws Exception {
        modules.add(new Module("to-drop"));
    }

    @Test
    public void removeModuleToDropFromTheAvailableModules() throws Exception {
        dropModule.performOn(modules);
        dropModule.perform(new Module("to-drop"));

        assertThat(modules, Matchers.empty());
    }

    @Test
    public void keepAllOtherModules() throws Exception {
        modules.add(0, new Module("to-keep"));
        dropModule.performOn(modules);
        dropModule.perform(new Module("to-drop"));

        assertThat(modules, Matchers.contains(new Module("to-keep")));
    }
}
