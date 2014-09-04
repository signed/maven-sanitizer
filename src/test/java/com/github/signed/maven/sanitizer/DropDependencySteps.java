package com.github.signed.maven.sanitizer;

import static com.github.signed.maven.sanitizer.MavenMatchersForTest.containsDependency;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import javax.inject.Inject;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DropDependencySteps {

    private final ConfigurationBuilder configurationBuilder;
    private final CucumberPaths paths;

    @Inject
    public DropDependencySteps(ConfigurationBuilder configurationBuilder, CucumberPaths paths) {
        this.configurationBuilder = configurationBuilder;
        this.paths = paths;
    }

    @When("^I configure to drop dependencies in test scope$")
    public void I_configure_to_drop_dependencies_in_test_scope() throws Throwable {
        configurationBuilder.dropDependenciesInScopeTest();
    }

    @Then("^there are no dependencies in scope test remaining in the dependency section in the entire build$")
    public void there_are_no_dependencies_in_scope_test_remaining_in_the_entire_build() throws Throwable {
        assertThat(paths.sanitizedMultiModuleBuildBuild().artifactModule().getDependencies(), not(containsDependency("org.mockito", "mockito-core")));
    }

    @Then("^there are no dependencies in scope test remaining in the dependency management  section in the entire build$")
    public void there_are_no_dependencies_in_scope_test_remaining_in_the_dependency_management_section_in_the_entire_build() throws Throwable {
        assertThat(paths.sanitizedMultiModuleBuildBuild().parentModule().getDependencyManagement().getDependencies(), not(containsDependency("org.mockito", "mockito-core")));
    }

    @Then("^the managed dependencies do not include hamcrest$")
    public void the_managed_dependencies_do_not_include_hamcrest() throws Throwable {
        HamcrestInCompileScope hamcrestInCompileScope = paths.hamcrestInCompileScope();
        assertThat(hamcrestInCompileScope.managesDependencies().getDependencyManagement().getDependencies(), not(containsDependency("org.hamcrest", "hamcrest-library")));
    }

    @Then("^the hamcrest dependency in compile scope has version 1.3$")
    public void the_hamcrest_dependency_in_compile_scope_has_version() throws Throwable {
        List<Dependency> dependencies = paths.hamcrestInCompileScope().includesHamcrestInCompileScope().getDependencies();
        Dependency dependency = dependencies.get(0);
        assertThat(dependency.getVersion(), is("1.3"));
    }
}
