package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import cucumber.api.guice.CucumberScopes;

public class GuiceModuleForCucumber extends AbstractModule{
    @Override
    protected void configure() {
        bind(ConfigurationBuilder.class).in(CucumberScopes.SCENARIO);
        bind(CucumberPaths.class).in(CucumberScopes.SCENARIO);
    }
}
