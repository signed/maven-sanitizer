package com.github.signed.maven.sanitizer.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;

import org.apache.maven.model.Dependency;
import org.junit.Test;
import com.github.signed.maven.sanitizer.pom.DependencyCombiner;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyBuilder;

public class DependencyCombiner_Test {

    @Test
    public void pomsAreNotPartOfTheFullyPopulatedModelJustReturnTheWrittenVersionAsFullyPopulated() throws Exception {
        Dependency pom = DependencyBuilder.anyDependency().withType("pom").build();
        Iterable<Dependency> asWritten= Collections.singletonList(pom);
        Iterable<Dependency> fullyPopulated = Collections.emptyList();
        Iterable<Patient<Dependency>> combine = new DependencyCombiner().combine(asWritten, fullyPopulated);

        Patient<Dependency> pomPatient = combine.iterator().next();

        assertThat(pomPatient.getAsWritten(), sameInstance(pomPatient.fullyPopulated()));
    }
}