package com.github.signed.maven.sanitizer;

import java.nio.file.Path;

public class CucumberPaths {
    public Path source;
    public Path destination;

    public SanitizedBuild sanitizedBuild() {
        return new SanitizedBuild(destination);
    }
}
