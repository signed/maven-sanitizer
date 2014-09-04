package com.github.signed.maven.sanitizer.pom.dependencies;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.InfectedProject;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class DeriveVersionFromDependencyManagement implements Action<Dependency> {
    private Iterable<Dependency> elements;

    @Override
    public void performOn(Iterable<Dependency> elements) {
        this.elements = elements;
    }

    @Override
    public void perform(final Dependency element, InfectedProject infectedProject) {
        Model originalModel = infectedProject.project().getParent().getOriginalModel();
        final List<Dependency> dependencies = originalModel.getDependencyManagement().getDependencies();
        Dependency managedDependency = Iterables.find(dependencies, new Predicate<Dependency>() {
            @Override
            public boolean apply(Dependency dependency) {
                return element.getGroupId().equals(dependency.getGroupId())
                        && element.getArtifactId().equals(dependency.getArtifactId())
                        && ((null == element.getType())?"jar":element.getType()).equals(dependency.getType());
            }
        });

        Iterable<Dependency> toUpdate = Iterables.filter(elements, new Predicate<Dependency>() {
            @Override
            public boolean apply(Dependency dependency) {
                return element.getGroupId().equals(dependency.getGroupId())
                        && element.getArtifactId().equals(dependency.getArtifactId());
            }
        });

        if(Iterables.isEmpty(toUpdate)){
            return;
        }

        Iterables.get(toUpdate, 0).setVersion(managedDependency.getVersion());
    }
}
