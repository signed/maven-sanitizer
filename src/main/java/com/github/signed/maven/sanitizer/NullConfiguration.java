package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CopyPom;

public class NullConfiguration implements Configuration {
    @Override
    public void configure(CopyProjectFiles projectFiles) {
        //do nothing
    }

    @Override
    public void configure(CopyPom copyPom) {
        //do nothing
    }
}
