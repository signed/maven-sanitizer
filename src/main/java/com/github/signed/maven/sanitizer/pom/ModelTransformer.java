package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.TransformationDiagnostics;

public interface ModelTransformer {

    void transform(TheModels models, TransformationDiagnostics transformationDiagnostics);
}
