package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.configuration.Configuration;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.maven.model.Dependency;
import org.hamcrest.Matcher;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class DropModuleSteps {

    private final TemporaryFolder folder = new TemporaryFolder();
    private final Fixture fixture = new Fixture();
    private Path source;
    private Path destination;
    private Configuration configuration;
    private String nameOfModuleToBeDropped;
    private String groupId;
    private String artifactId;

    @Before
    public void createDestinationDirectory() throws IOException {
        folder.create();
        destination = folder.getRoot().toPath();
    }

    @After
    public void destroyDestinationDirectory() {
        folder.delete();
    }

    @Given("^the multi module sample in src/test/resources$")
    public void theMultiModuleSampleInSrcTestResources() throws Throwable {
        source = fixture.multiModule.containingDirectory;
    }

    @When("^I configure the module (.*) with groupId (.*) and artifactId (.*) to be dropped$")
    public void iConfigureTheModuleToBeDropped(final String moduleName, final String groupId, final String artifactId) throws Throwable {
        nameOfModuleToBeDropped = moduleName;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.configuration = new DropModule(nameOfModuleToBeDropped, groupId, artifactId);
    }

    @When("^sanitize the maven project file$")
    public void sanitizeTheMavenProjectFile() throws Throwable {
        MavenSanitizer sanitizer = new MavenSanitizer(source, destination, configuration);
        sanitizer.configure();
        sanitizer.sanitize();
    }

    @Then("^the reactor pom does not contain the module any more$")
    public void theReactorPomDoesNotContainTheModuleAnyMore() throws Throwable {
        assertThat(sanitizedBuild().reactor().getModules(), not(hasItem(nameOfModuleToBeDropped)));
    }

    @Then("^the dropped module does not exist in the destination directory$")
    public void theDroppedModuleDoesNotExistInTheDestinationDirectory() throws Throwable {
        SourceToDestinationTreeMapper mapper = new SourceToDestinationTreeMapper(source, destination);
        Path expectedLocationIfModuleWouldBeCopied = mapper.map(source.resolve(nameOfModuleToBeDropped));
        assertThat(expectedLocationIfModuleWouldBeCopied.toFile().exists(), is(false));
    }

    @Then("^the dependency to the dropped module in the war module is removed$")
    public void the_dependency_to_the_dropped_module_in_the_war_module_is_removed() throws Throwable {
        assertThat(sanitizedBuild().warModule().getDependencies(), not(containsDependency(groupId, artifactId)));
    }

    @Then("^the entry in the dependency management section of the parent is removed$")
    public void the_entry_in_the_dependency_management_section_of_the_parent_is_removed() throws Throwable {
        assertThat(sanitizedBuild().parent().getDependencyManagement().getDependencies(), not(containsDependency(groupId, artifactId)));
    }

    private Matcher<Iterable<? super Dependency>> containsDependency(String groupId, String artifactId) {
        return hasItem(new DependencyMatcher(groupId, artifactId));
    }

    private SanitizedBuild sanitizedBuild() {
        return new SanitizedBuild(destination);
    }

}
