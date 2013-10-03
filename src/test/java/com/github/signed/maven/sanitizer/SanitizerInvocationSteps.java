package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.junit.rules.TemporaryFolder;

import javax.inject.Inject;
import java.io.IOException;

public class SanitizerInvocationSteps {

    private final TemporaryFolder folder = new TemporaryFolder();
    private final Fixture fixture = new Fixture();
    private final ConfigurationBuilder configurationBuilder;
    private final CucumberPaths paths;

    @Before
    public void createDestinationDirectory() throws IOException {
        folder.create();
        paths.destination = folder.getRoot().toPath();
    }

    @After
    public void destroyDestinationDirectory() {
        folder.delete();
    }

    @Inject
    public SanitizerInvocationSteps(ConfigurationBuilder configurationBuilder, CucumberPaths paths){
        this.configurationBuilder = configurationBuilder;
        this.paths = paths;
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