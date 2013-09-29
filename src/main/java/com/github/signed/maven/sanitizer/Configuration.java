package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.PomTransformer;

public interface Configuration {

    void configure(CollectPathsToCopy projectFiles);

    void configure(PomTransformer pomTransformation);
}
