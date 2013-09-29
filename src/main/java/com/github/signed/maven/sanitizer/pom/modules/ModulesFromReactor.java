package com.github.signed.maven.sanitizer.pom.modules;

import com.github.signed.maven.sanitizer.pom.Extractor;
import com.google.common.base.Function;
import org.apache.maven.model.Model;

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.collect.Iterables.transform;

public class ModulesFromReactor implements Extractor<Module> {
    @Override
    public Iterable<Module> elements(Model model) {
        List<String> modules = model.getModules();
        return transform(modules, new Function<String, Module>() {
            @Override
            public Module apply(@Nullable String input) {
                return new Module(input);
            }
        });
    }
}
