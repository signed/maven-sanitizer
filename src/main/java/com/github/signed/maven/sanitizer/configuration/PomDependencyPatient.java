package com.github.signed.maven.sanitizer.configuration;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.pom.Patient;

public class PomDependencyPatient implements Patient<Dependency> {
    private final Dependency dependency;

    public PomDependencyPatient(Dependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public Dependency getAsWritten() {
        return dependency;
    }

    @Override
    public Dependency fullyPopulated() {
        return dependency;
    }
}
