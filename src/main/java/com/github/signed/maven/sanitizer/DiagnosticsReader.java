package com.github.signed.maven.sanitizer;

import java.nio.file.Path;

public interface DiagnosticsReader {
    boolean isSafeToCopy(Path path);
}
