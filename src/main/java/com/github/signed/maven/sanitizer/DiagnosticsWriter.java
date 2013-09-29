package com.github.signed.maven.sanitizer;

import java.nio.file.Path;

public interface DiagnosticsWriter {

    void ignorePathAndEverythingBelow(Path path);
}
