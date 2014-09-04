package com.github.signed.maven.sanitizer.pom;

public interface Patient<MavenModelElement> {
    MavenModelElement getAsWritten();

    MavenModelElement fullyPopulated();
}
