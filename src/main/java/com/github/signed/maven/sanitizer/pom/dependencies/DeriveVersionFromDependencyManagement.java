package com.github.signed.maven.sanitizer.pom.dependencies;

import org.apache.maven.model.Dependency;
import com.github.signed.maven.sanitizer.pom.Action;

public class DeriveVersionFromDependencyManagement implements Action<Dependency> {
    private Iterable<Dependency> elements;

    @Override
    public void performOn(Iterable<Dependency> elements) {
        this.elements = elements;
    }

    @Override
    public void perform(Dependency element) {
        System.out.println("implement me");
    }
}
