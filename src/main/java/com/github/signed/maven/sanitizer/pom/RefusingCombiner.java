package com.github.signed.maven.sanitizer.pom;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class RefusingCombiner<MavenModelElement> implements ModelElementCombiner<MavenModelElement> {

    @Override
    public Iterable<Patient<MavenModelElement>> combine(Iterable<MavenModelElement> asWritten, Iterable<MavenModelElement> fullyPopulated) {
        return Iterables.transform(fullyPopulated, new Function<MavenModelElement, Patient<MavenModelElement>>() {
            @Override
            public Patient<MavenModelElement> apply(@Nullable MavenModelElement mavenModelElement) {
                return new FullyPopulatedOnly<>(mavenModelElement);
            }
        });
    }
}
