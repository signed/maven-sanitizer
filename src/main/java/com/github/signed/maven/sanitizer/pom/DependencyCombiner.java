package com.github.signed.maven.sanitizer.pom;

import static com.github.signed.maven.sanitizer.pom.dependencies.DependencyGuava.dependenciesOfTypePom;
import static com.google.common.base.Predicates.not;

import java.util.Collection;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.configuration.MatchingDependency;
import com.github.signed.maven.sanitizer.configuration.PomDependencyPatient;
import com.github.signed.maven.sanitizer.configuration.WellBehavedPatient;
import com.github.signed.maven.sanitizer.pom.ModelElementCombiner;
import com.github.signed.maven.sanitizer.pom.Patient;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DependencyCombiner implements ModelElementCombiner<Dependency> {
    @Override
    public Iterable<Patient<Dependency>> combine(final Iterable<Dependency> dependenciesAsWritten, final Iterable<Dependency> fullyPopulatedDepencencies) {
        Collection<Patient<Dependency>> result = Lists.newArrayList();
        Iterable<Dependency> poms = Iterables.filter(dependenciesAsWritten, dependenciesOfTypePom());
        Iterables.addAll(result, Iterables.transform(poms, new Function<Dependency, Patient<Dependency>>() {
            @Override
            public Patient<Dependency> apply(Dependency input) {
                return new PomDependencyPatient(input);
            }
        }));
        Iterable<Dependency> rest = Iterables.filter(dependenciesAsWritten, not(dependenciesOfTypePom()));
        Iterables.addAll(result, Iterables.transform(rest, new Function<Dependency, Patient<Dependency>>() {
            @Override
            public Patient<Dependency> apply(final Dependency dependencyAsWritten) {
                final Dependency matchedFullyPopulatedDependency = Iterables.find(fullyPopulatedDepencencies, new MatchingDependency(dependencyAsWritten));
                return new WellBehavedPatient(dependencyAsWritten, matchedFullyPopulatedDependency);
            }
        }));
        return result;
    }

}
