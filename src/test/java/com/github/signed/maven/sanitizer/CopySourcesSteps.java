package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import javax.inject.Inject;
import java.nio.file.Path;

import static com.github.signed.matcher.file.IsADirectory.aDirectory;
import static com.github.signed.matcher.file.IsFile.isAFile;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CopySourcesSteps {

    private final CucumberPaths paths;
    private final ConfigurationBuilder configurationBuilder;

    @Inject
    public CopySourcesSteps(CucumberPaths paths, ConfigurationBuilder configurationBuilder){
        this.paths = paths;
        this.configurationBuilder = configurationBuilder;
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

    @And("^I configure to copy the subdirectory (.*) in the module (.*) (.*)$")
    public void I_configure_to_copy_the_subdirectory_important_in_the_module_org_example_parent(String subdirectory, String moduleGroupId, String moduleArtifactId) throws Throwable {
        configurationBuilder.includeSubDirectory(moduleGroupId, moduleArtifactId, subdirectory);
    }

    @Then("^the directory (.*) exists below the module root of (.*) (.*) in the destination directory$")
    public void the_directory_important_exists_below_the_module_root_of_org_example_parent_in_the_destination_directory(String directoryName, String groupId, String artifactId) throws Throwable {
        assertThat(paths.sanitizedBuild().getRootOfModule(artifactId).resolve(directoryName).toFile(), is(aDirectory()));
    }

    private Path artifactRoot() {
        return paths.sanitizedBuild().getRootOfModule("artifact");
    }
}
