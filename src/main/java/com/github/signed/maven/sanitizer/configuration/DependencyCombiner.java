package com.github.signed.maven.sanitizer.configuration;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.pom.ModelElementCombiner;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class DependencyCombiner implements ModelElementCombiner<Dependency> {
    @Override
    public Iterable<Patient<Dependency>> combine(final Iterable<Dependency> dependenciesAsWritten, final Iterable<Dependency> fullyPopulatedDepencencies) {
        return Iterables.transform(dependenciesAsWritten, new Function<Dependency, Patient<Dependency>>() {
            @Override
            public Patient<Dependency> apply(final Dependency dependencyAsWritten) {
                final Dependency matchedFullyPopulatedDependency = Iterables.find(fullyPopulatedDepencencies, new MatchingDependency(dependencyAsWritten));
                return new WellBehavedPatient(dependencyAsWritten, matchedFullyPopulatedDependency);
            }
        });
    }

    public static class MatchingDependency implements Predicate<Dependency> {
        private final Dependency dependencyAsWritten;

        public MatchingDependency(Dependency dependencyAsWritten) {
            this.dependencyAsWritten = dependencyAsWritten;
        }

        @Override
        public boolean apply(Dependency fullyPopulatedDependency) {
            return dependencyAsWritten.getGroupId().equals(fullyPopulatedDependency.getGroupId())
                    && dependencyAsWritten.getArtifactId().equals(fullyPopulatedDependency.getArtifactId())
                    && ((null == dependencyAsWritten.getType() ? "jar" : dependencyAsWritten.getType())).equals(fullyPopulatedDependency.getType())
                    ;
        }
    }
}
