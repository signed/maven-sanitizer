package com.github.signed.maven.sanitizer.configuration;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.pom.Patient;

public class WellBehavedPatient implements Patient<Dependency> {
    private final Dependency dependencyAsWritten;
    private final Dependency fullyPopulatedDependency;

    public WellBehavedPatient(Dependency dependencyAsWritten, Dependency fullyPopulatedDependency) {
        this.dependencyAsWritten = dependencyAsWritten;
        this.fullyPopulatedDependency = fullyPopulatedDependency;
    }

    @Override
    public Dependency getAsWritten() {
        return dependencyAsWritten;
    }

    @Override
    public Dependency fullyPopulated() {
        return fullyPopulatedDependency;
    }
}
