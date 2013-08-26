package com.github.signed.maven.model;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;

public class AssemblyDescriptors_Test {

    @Test
    public void returnNoPathsIfNoAssemblyPluginIsConfigured() throws Exception {
        assertThat(new AssemblyDescriptors().paths(MavenProjectBuilder.hire().build()), Matchers.<Path>iterableWithSize(0));
    }
}
