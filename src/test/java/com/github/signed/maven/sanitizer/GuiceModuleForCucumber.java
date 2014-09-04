package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class GuiceModuleForCucumber extends AbstractModule{
    @Override
    protected void configure() {
        bind(SanitizerIntegrationTestFixture.class).in(Singleton.class);
    }

    @Provides
    public ConfigurationBuilder configurationBuilder(SanitizerIntegrationTestFixture fixture){
        return fixture.configurationBuilder;
    }

    @Provides
    public CucumberPaths paths( SanitizerIntegrationTestFixture fixture){
        return fixture.paths;
    }
}
