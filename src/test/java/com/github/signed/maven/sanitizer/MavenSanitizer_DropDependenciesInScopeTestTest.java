package com.github.signed.maven.sanitizer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import org.apache.maven.model.Model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MavenSanitizer_DropDependenciesInScopeTestTest {
    private final SanitizerIntegrationTestFixture fixture = new SanitizerIntegrationTestFixture();

    @Before
    public void setUp() throws Exception {
        fixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        fixture.tearDown();
    }

    @Test
    public void doNotDeriveTheVersionForDependenciesInScopeOtherThanTest() throws Exception {
        fixture.configurationBuilder.dropDependenciesInScopeTest();
        fixture.runDropDependencyWithNoDependenciesInTestScope();

        fixture.paths.hamcrestInCompileScope();
        fixture.runSanitizer();

        Model child = fixture.paths.noDependenciesInScopeTest().getChildModule();
        assertThat(child.getDependencies().get(0).getVersion(), nullValue());
    }
}