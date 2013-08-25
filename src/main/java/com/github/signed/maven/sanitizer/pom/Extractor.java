package com.github.signed.maven.sanitizer.pom;

import org.apache.maven.model.Model;

public interface Extractor<T> {
    Iterable<T> elements(Model model);
}
