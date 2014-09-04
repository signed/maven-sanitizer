package com.github.signed.maven.sanitizer.pom;

public interface ModelElementCombiner<MavenModelElement> {
    Iterable<Patient<MavenModelElement>> combine(Iterable<MavenModelElement> asWritten, Iterable<MavenModelElement> fullyPopulated);
}
