package com.github.signed.maven.sanitizer;

import java.io.IOException;

import javax.inject.Inject;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class SanitizerInvocationSteps {

    private final SanitizerIntegrationTestFixture sanitizerFixture;

    @Before
    public void createDestinationDirectory() throws IOException {
        sanitizerFixture.setUp();
    }

    @After
    public void destroyDestinationDirectory() {
        sanitizerFixture.tearDown();
    }

    @Inject
    public SanitizerInvocationSteps(SanitizerIntegrationTestFixture sanitizerFixture) {
        this.sanitizerFixture = sanitizerFixture;
    }

    @Given("^a build that manages hamcrest in test scope in version 1.3$")
    @And("^in one of its modules includes hamcrest in compile scope$")
    public void a_build_that_manages_hamcrest_in_test_scope_in_version() throws Throwable {
        sanitizerFixture.runDropDependencyWithHamcrestInCompileScope();
    }

    @Given("^the multi module sample in src/test/resources$")
    public void theMultiModuleSampleInSrcTestResources() throws Throwable {
        sanitizerFixture.runMultiModule();
    }

    @When("^sanitize the maven project file$")
    @And("^sanitize the maven project files$")
    public void sanitizeTheMavenProjectFile() throws Throwable {
        sanitizerFixture.runSanitizer();
    }
}