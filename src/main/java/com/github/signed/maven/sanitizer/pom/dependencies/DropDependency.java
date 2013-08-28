package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Strings;
import com.github.signed.maven.sanitizer.pom.Transformation;
import org.apache.maven.model.Dependency;

import java.util.Iterator;

public class DropDependency implements Transformation<Dependency> {
    private Iterable<Dependency> dependencies;
    private final Strings strings = new Strings();

    @Override
    public void performOn(Iterable<Dependency> elements) {
        dependencies = elements;
    }

    @Override
    public void execute(Dependency toDrop) {
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
        return strings.matching(dependency.getVersion(), toDrop.getVersion());
    }

    private boolean matchingClassifier(Dependency dependency, Dependency toDrop) {
        return strings.matching(dependency.getClassifier(), toDrop.getClassifier());
    }

    private boolean matchingType(Dependency dependency, Dependency toDrop) {
        return strings.matching(dependency.getType(), toDrop.getType());
    }
}