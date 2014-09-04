package com.github.signed.maven.sanitizer;

import java.io.IOException;

import org.junit.rules.TemporaryFolder;

import com.github.signed.maven.sanitizer.configuration.ConfigurationBuilder;

public class SanitizerIntegrationTestFixture {
    public final Fixture fixture = new Fixture();
    public final TemporaryFolder folder = new TemporaryFolder();
    public final CucumberPaths paths = CucumberPaths.CreateCucumberPaths();
    public final ConfigurationBuilder configurationBuilder = ConfigurationBuilder.CreateConfigurationWithSomeDefaults();

    public void runSanitizer() {
        MavenSanitizer sanitizer = new MavenSanitizer(paths.source, paths.destination, configurationBuilder.build());
        sanitizer.configure();
        sanitizer.sanitize();
    }

    public void runDropDependencyWithHamcrestInCompileScope() {
        paths.source = fixture.dropDependency.hamcrestInCompileScope.containingDirectory;
    }

    public void runMultiModule() {
        paths.source = fixture.multiModule.containingDirectory;
    }

    public void runDropDependencyWithNoDependenciesInTestScope() {
        paths.source = fixture.dropDependency.noDependencyInScopeTest.containingDirectory;
    }

    public void setUp() throws IOException {
        folder.create();
        paths.destination = folder.getRoot().toPath();
    }

    public void tearDown() {
        folder.delete();
    }
}
