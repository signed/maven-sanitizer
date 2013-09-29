package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CopyPom;

public interface Configuration {

    void configure(CollectPathsToCopy projectFiles);

    void configure(CopyPom copyPom);
}
