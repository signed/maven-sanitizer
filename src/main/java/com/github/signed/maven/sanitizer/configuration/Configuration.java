package com.github.signed.maven.sanitizer.configuration;

import com.github.signed.maven.sanitizer.CollectPathsToCopy;
import com.github.signed.maven.sanitizer.pom.PomTransformer;

public interface Configuration {

    void configure(CollectPathsToCopy projectFiles);

    void configure(PomTransformer pomTransformation);
}
