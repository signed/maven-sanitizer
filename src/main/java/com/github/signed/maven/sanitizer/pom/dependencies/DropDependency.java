package com.github.signed.maven.sanitizer.pom.dependencies;

import java.util.Iterator;

import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.Strings;
import org.apache.maven.model.Dependency;

public class DropDependency implements Action<Dependency> {
    private Iterable<Dependency> dependencies;
    private final Strings strings = new Strings();

    @Override
    public void performOn(Iterable<Dependency> elements) {
        dependencies = elements;
    }

    @Override
    public void perform(Dependency toDrop) {
        Iterator<Dependency> dependencyIterator = dependencies.iterator();
        while (dependencyIterator.hasNext()) {
            Dependency dependency = dependencyIterator.next();
            if (!sameGroupId(dependency, toDrop)) {
                continue;
            }
            if (!sameArtifactId(dependency, toDrop)) {
                continue;
            }

            if(!matchingVersion(dependency, toDrop)){
                continue;
            }

            if(!matchingClassifier(dependency, toDrop)){
                continue;
            }

            if(!matchingType(dependency, toDrop)){
                continue;
            }

            dependencyIterator.remove();
        }
    }

    private boolean sameGroupId(Dependency dependency, Dependency toDrop) {
        return dependency.getGroupId().equals(toDrop.getGroupId());
    }

    private boolean sameArtifactId(Dependency dependency, Dependency toDrop) {
        return dependency.getArtifactId().equals(toDrop.getArtifactId());
    }

    private boolean matchingVersion(Dependency dependency, Dependency toDrop) {
        String vanillaVersion = dependency.getVersion();

        return vanillaVersion.startsWith("${") || strings.matching(vanillaVersion, toDrop.getVersion());
    }

    private boolean matchingClassifier(Dependency dependency, Dependency toDrop) {
        return strings.matching(dependency.getClassifier(), toDrop.getClassifier());
    }

    private boolean matchingType(Dependency dependency, Dependency toDrop) {
        return strings.matching(dependency.getType(), toDrop.getType());
    }
}