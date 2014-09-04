package com.github.signed.maven.sanitizer.pom.modules;

import static com.google.common.collect.Iterables.transform;

import java.util.List;

import org.apache.maven.model.Model;
import com.github.signed.maven.sanitizer.pom.Extractor;
import com.google.common.base.Function;

public class ModulesFromReactor implements Extractor<Module> {
    @Override
    public Iterable<Module> elements(Model model) {
        List<String> modules = model.getModules();
        return transform(modules, new Function<String, Module>() {
            @Override
            public Module apply(String input) {
                return new Module(input);
            }
        });
    }
}
