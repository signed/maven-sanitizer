package com.github.signed.maven.sanitizer.pom.modules;

import com.github.signed.maven.model.MavenProjectBuilder;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModulesFromReactor_Test {

    @Test
    public void extractModulesFromModulesSection() throws Exception {
        MavenProject project = MavenProjectBuilder.hire().withModule("some-sub-module").withModule("another-module").build();

        Iterable<Module> foundModules = new ModulesFromReactor().elements(project.getModel());
        assertThat(foundModules, allOf(hasItem(new Module("some-sub-module")), hasItem(new Module("another-module"))));
    }
}
