package com.github.signed.maven.sanitizer.configuration;

import com.github.signed.maven.sanitizer.CollectPathsToCopy;
import com.github.signed.maven.sanitizer.pom.ModelTransformer;
import com.github.signed.maven.sanitizer.pom.PomTransformer;

public class SimplePomTransformation implements Configuration {
    private final ModelTransformer pomTransformation2;

    public SimplePomTransformation(ModelTransformer pomTransformation2) {
        this.pomTransformation2 = pomTransformation2;
    }

    @Override
    public void configure(CollectPathsToCopy projectFiles) {

    }

    @Override
    public void configure(PomTransformer pomTransformation) {
        pomTransformation.addTransformer(pomTransformation2);
    }
}
