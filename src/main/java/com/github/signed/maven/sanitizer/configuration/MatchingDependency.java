package com.github.signed.maven.sanitizer.configuration;

import org.apache.maven.model.Dependency;
import com.google.common.base.Predicate;

public class MatchingDependency implements Predicate<Dependency> {
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
