package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javax.inject.Inject;
import java.nio.file.Path;

import static com.github.signed.maven.sanitizer.MavenMatchersForTest.containsDependency;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class DropModuleSteps {

    private final ConfigurationBuilder configurationBuilder;
    private final CucumberPaths paths;
    private String nameOfModuleToBeDropped;
    private String groupId;
    private String artifactId;

    @Inject
    public DropModuleSteps(ConfigurationBuilder configurationBuilder, CucumberPaths paths){
        this.configurationBuilder = configurationBuilder;
        this.paths = paths;
    }

    @When("^I configure the module (.*) with groupId (.*) and artifactId (.*) to be dropped$")
    public void iConfigureTheModuleToBeDropped(final String moduleName, final String groupId, final String artifactId) throws Throwable {
        nameOfModuleToBeDropped = moduleName;
        this.groupId = groupId;
        this.artifactId = artifactId;
        configurationBuilder.dropModule(moduleName, groupId, artifactId);
    }

    @Then("^the reactor pom does not contain the module any more$")
    public void theReactorPomDoesNotContainTheModuleAnyMore() throws Throwable {
        assertThat(paths.sanitizedBuild().reactor().getModules(), not(hasItem(nameOfModuleToBeDropped)));
    }

    @Then("^the dropped module does not exist in the destination directory$")
    public void theDroppedModuleDoesNotExistInTheDestinationDirectory() throws Throwable {
        SourceToDestinationTreeMapper mapper = new SourceToDestinationTreeMapper(paths.source, paths.destination);
        Path expectedLocationIfModuleWouldBeCopied = mapper.map(paths.source.resolve(nameOfModuleToBeDropped));
        assertThat(expectedLocationIfModuleWouldBeCopied.toFile().exists(), is(false));
    }

    @Then("^the dependency to the dropped module in the war module is removed$")
    public void the_dependency_to_the_dropped_module_in_the_war_module_is_removed() throws Throwable {
        assertThat(paths.sanitizedBuild().warModule().getDependencies(), not(containsDependency(groupId, artifactId)));
    }

    @Then("^the entry in the dependency management section of the parent is removed$")
    public void the_entry_in_the_dependency_management_section_of_the_parent_is_removed() throws Throwable {
        assertThat(paths.sanitizedBuild().parent().getDependencyManagement().getDependencies(), not(containsDependency(groupId, artifactId)));
    }
}
