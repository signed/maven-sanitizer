package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Extractor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import java.util.Collections;
import java.util.List;

public class DependenciesFromDependencies implements Extractor<Dependency> {
    @Override
    public Iterable<Dependency> elements(Model model) {
        List<Dependency> dependencies = model.getDependencies();
        if( null == dependencies){
            return Collections.emptyList();
        }
        return dependencies;
    }
}
