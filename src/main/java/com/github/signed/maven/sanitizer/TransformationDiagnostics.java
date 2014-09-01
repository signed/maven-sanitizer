package com.github.signed.maven.sanitizer;

import com.google.common.collect.Sets;

import java.nio.file.Path;
import java.util.Set;

public class TransformationDiagnostics implements DiagnosticsReader, DiagnosticsWriter{

    private Set<Path> unsafePaths = Sets.newHashSet();

    @Override
    public boolean isSafeToCopy(Path path) {
        for (Path unsafePath : unsafePaths) {
            if(path.startsWith(unsafePath)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void ignorePathAndEverythingBelow(Path path) {
        unsafePaths.add(path.normalize());
    }
}
