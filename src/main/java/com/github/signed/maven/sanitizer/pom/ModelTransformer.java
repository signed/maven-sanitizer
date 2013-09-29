package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;

public interface ModelTransformer {

    void transform(TheModels models, DiagnosticsWriter diagnosticsWriter);
}
