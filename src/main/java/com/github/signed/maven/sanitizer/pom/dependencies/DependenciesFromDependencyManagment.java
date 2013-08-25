package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Extractor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;

import java.util.Collections;
import java.util.List;

public class DependenciesFromDependencyManagment implements Extractor<Dependency> {
    @Override
    public Iterable<Dependency> elements(Model model) {
        DependencyManagement dependencyManagement = model.getDependencyManagement();
        if (null == dependencyManagement) {
            return Collections.emptyList();
        }
        List<Dependency> dependencies = dependencyManagement.getDependencies();
        if (null == dependencies) {
            return Collections.emptyList();
        }

        return dependencies;
    }
}
