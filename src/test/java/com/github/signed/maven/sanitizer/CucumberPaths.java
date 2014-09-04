package com.github.signed.maven.sanitizer;

import java.nio.file.Path;

public class CucumberPaths {
    public Path source;
    public Path destination;

    public SanitizedMultiModuleBuild sanitizedMultiModuleBuildBuild() {
        return new SanitizedMultiModuleBuild(source, destination);
    }

    public HamcrestInCompileScope hamcrestInCompileScope(){
        return new HamcrestInCompileScope(destination);
    }
}
