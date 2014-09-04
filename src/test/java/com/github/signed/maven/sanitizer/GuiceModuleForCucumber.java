package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class GuiceModuleForCucumber extends AbstractModule{
    @Override
    protected void configure() {
        bind(ConfigurationBuilder.class).in(Singleton.class);
        bind(CucumberPaths.class).in(Singleton.class);
    }
}
