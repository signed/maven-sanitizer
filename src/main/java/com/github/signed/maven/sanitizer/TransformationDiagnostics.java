package com.github.signed.maven.sanitizer;

import java.nio.file.Path;

public class TransformationDiagnostics implements DiagnosticsReader, DiagnosticsWriter{

    @Override
    public boolean isSafeToCopy(Path path) {
        return true;
    }
}
