package com.github.signed.maven.sanitizer.pom.modules;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Lists;

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
        dropModule.perform(new Module("to-drop"), null);

        assertThat(modules, Matchers.empty());
    }

    @Test
    public void keepAllOtherModules() throws Exception {
        modules.add(0, new Module("to-keep"));
        dropModule.performOn(modules);
        dropModule.perform(new Module("to-drop"), null);

        assertThat(modules, Matchers.contains(new Module("to-keep")));
    }
}
