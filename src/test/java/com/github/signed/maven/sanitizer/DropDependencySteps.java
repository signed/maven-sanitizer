package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;
import com.github.signed.maven.sanitizer.configuration.ForDependencyReferences;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesInScope;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javax.inject.Inject;

import static com.github.signed.maven.sanitizer.MavenMatchersForTest.containsDependency;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class DropDependencySteps {

    private final ConfigurationBuilder configurationBuilder;
    private final CucumberPaths paths;

    @Inject
    public DropDependencySteps(ConfigurationBuilder configurationBuilder, CucumberPaths paths){
        this.configurationBuilder = configurationBuilder;
        this.paths = paths;
    }

    @When("^I configure to drop dependencies in test scope$")
    public void I_configure_to_drop_dependencies_in_test_scope() throws Throwable {
        configurationBuilder.duringPomTransformation(ForDependencyReferences.inAllModules().focusOnActualDependenciesAndDependencyManagement().drop().referencesTo(DependenciesInScope.Test()));
    }

    @Then("^there are no dependencies in scope test remaining in the entire build$")
    public void there_are_no_dependencies_in_scope_test_remaining_in_the_entire_build() throws Throwable {
        assertThat(paths.sanitizedBuild().artifactModule().getDependencies(), not(containsDependency("org.mockito", "mockito-core")));
    }
}
