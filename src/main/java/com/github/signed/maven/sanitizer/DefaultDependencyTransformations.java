package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Dependency;

import java.util.Iterator;
import java.util.List;

public class DefaultDependencyTransformations implements CopyProject.DependencyTransformations {
    private final List<Dependency> dependencies;

    public DefaultDependencyTransformations(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public void drop(Dependency toDrop) {
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

    private boolean matchingVersion(Dependency dependency, Dependency toDrop) {
        return matching(dependency.getVersion(), toDrop.getVersion());
    }

    private boolean matchingType(Dependency dependency, Dependency toDrop) {
        return matching(dependency.getType(), toDrop.getType());
    }

    private boolean matchingClassifier(Dependency dependency, Dependency toDrop) {
        return matching(dependency.getClassifier(), toDrop.getClassifier());
    }

    private boolean matching(String dependency, String toDrop) {
        return null == dependency || dependency.equals(toDrop);
    }

    private boolean sameArtifactId(Dependency dependency, Dependency toDrop) {
        return dependency.getArtifactId().equals(toDrop.getArtifactId());
    }

    private boolean sameGroupId(Dependency dependency, Dependency toDrop) {
        return dependency.getGroupId().equals(toDrop.getGroupId());
    }
}
