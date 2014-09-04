package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;

public class DeriveVersionFromDependencyManagement implements Action<Dependency> {
    private Iterable<Dependency> elements;

    @Override
    public void performOn(Iterable<Dependency> elements) {
        this.elements = elements;
    }

    @Override
    public void perform(Dependency element, InfectedProject infectedProject) {
        System.out.println("implement me");
    }
}
