package com.github.signed.maven.sanitizer;

import cucumber.api.java.en.Then;

import javax.inject.Inject;
import java.nio.file.Path;

import static com.github.signed.matcher.file.IsADirectory.aDirectory;
import static com.github.signed.matcher.file.IsFile.isAFile;
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

    @Then("^the configured warSourceDirectory is copied to the destination directory$")
    public void the_configured_warSourceDirectory_is_copied_to_the_destination_directory() throws Throwable {
        assertThat(paths.sanitizedBuild().getRootOfModule("war").resolve("src/main/webcontent").toFile(), aDirectory());
    }

    @Then("^the configured assembly descriptor is copied is copied to the destination directory$")
    public void the_configured_assembly_descriptor_is_copied_is_copied_to_the_destination_directory() throws Throwable {
        assertThat(paths.sanitizedBuild().getRootOfModule("assembly").resolve("src/main/assembly/descriptor.xml").toFile(), isAFile());
    }

    private Path artifactRoot() {
        return paths.sanitizedBuild().getRootOfModule("artifact");
    }
}
