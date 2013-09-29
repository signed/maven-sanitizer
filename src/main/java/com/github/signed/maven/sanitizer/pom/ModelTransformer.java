package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;

public interface ModelTransformer {

    void transform(infectedProject models, DiagnosticsWriter diagnosticsWriter);
}
