package com.github.signed.maven.sanitizer;

import cucumber.api.java.en.Then;

import javax.inject.Inject;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CopySourcesSteps {

    private final CucumberPaths paths;

    @Inject
    public CopySourcesSteps(CucumberPaths paths){
        this.paths = paths;
    }

    @Then("^the source root of the artifact module is copied to the destination directory$")
    public void the_source_root_of_the_artifact_module_is_copied_to_the_destination_directory() throws Throwable {
        assertThat(artifactRoot().resolve("src/main/java").toFile().isDirectory(), is(true));
    }

    @Then("^the resource root of the artifact module is copied to the destination directory$")
    public void the_resource_root_of_the_artifact_module_is_copied_to_the_destination_directory() throws Throwable {
        assertThat(artifactRoot().resolve("src/main/resources").toFile().isDirectory(), is(true));
    }

    private Path artifactRoot() {
        return paths.sanitizedBuild().getRootOfModule("artifact");
    }
}
