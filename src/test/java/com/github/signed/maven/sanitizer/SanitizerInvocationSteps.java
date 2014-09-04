package com.github.signed.maven.sanitizer;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.rules.TemporaryFolder;
import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class SanitizerInvocationSteps {

    private final TemporaryFolder folder = new TemporaryFolder();
    private final Fixture fixture = new Fixture();
    private final ConfigurationBuilder configurationBuilder;
    private final CucumberPaths paths;
    private final DebugOutput debugOutput = new DebugOutput();

    @Before
    public void createDestinationDirectory() throws IOException {
        debugOutput.logCalledMethod();
        folder.create();
        paths.destination = folder.getRoot().toPath();
    }

    @After
    public void destroyDestinationDirectory() {
        debugOutput.logCalledMethod();
        folder.delete();
    }

    @Inject
    public SanitizerInvocationSteps(ConfigurationBuilder configurationBuilder, CucumberPaths paths){
        debugOutput.logCalledMethod();
        this.configurationBuilder = configurationBuilder;
        this.paths = paths;
    }

    @Given("^a build that manages hamcrest in test scope in version 1.3$")
    @And("^in one of its modules includes hamcrest in compile scope$")
    public void a_build_that_manages_hamcrest_in_test_scope_in_version() throws Throwable {
        paths.source = fixture.dropDependency.hamcrestInCompileScope.containingDirectory;
    }

    @Given("^the multi module sample in src/test/resources$")
    public void theMultiModuleSampleInSrcTestResources() throws Throwable {
        paths.source = fixture.multiModule.containingDirectory;
    }

    @When("^sanitize the maven project file$")
    @And("^sanitize the maven project files$")
    public void sanitizeTheMavenProjectFile() throws Throwable {
        MavenSanitizer sanitizer = new MavenSanitizer(paths.source, paths.destination, configurationBuilder.build());
        sanitizer.configure();
        sanitizer.sanitize();
    }
}