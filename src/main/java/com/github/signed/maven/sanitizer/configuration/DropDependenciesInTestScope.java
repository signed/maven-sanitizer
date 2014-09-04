package com.github.signed.maven.sanitizer.configuration;

import com.github.signed.maven.sanitizer.CollectPathsToCopy;
import com.github.signed.maven.sanitizer.pom.PomTransformer;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesInScope;
import com.github.signed.maven.sanitizer.pom.dependencies.DependenciesWithInheritedVersion;
import com.github.signed.maven.sanitizer.pom.dependencies.DeriveVersionFromDependencyManagement;

public class DropDependenciesInTestScope implements Configuration {

    @Override
    public void configure(CollectPathsToCopy projectFiles) {

    }

    @Override
    public void configure(PomTransformer pomTransformation) {
        during(pomTransformation, ForDependencyReferences.inAllModules().focusOnActualDependenciesAndDependencyManagement().drop().referencesTo(DependenciesInScope.Test()));
        during(pomTransformation, ForDependencyReferences.inAllModules().focusOnActualDependencies().perform(new DeriveVersionFromDependencyManagement()).referencesTo(new DependenciesWithInheritedVersion()));
    }

    private void during(PomTransformer copyingThePom, ForDependencyReferences perform) {
        copyingThePom.addTransformer(perform.build());
    }
}
