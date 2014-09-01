package com.github.signed.maven.sanitizer;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TransformationDiagnostics_Test {
    private final TransformationDiagnostics transformationDiagnostics = new TransformationDiagnostics();

    @Before
    public void setUp() throws Exception {
        transformationDiagnostics.ignorePathAndEverythingBelow(Paths.get("/tmp/unsafe"));
    }

    @Test
    public void itIsNotSafeToCopyAnIgnoredPath() throws Exception {
        assertThat(transformationDiagnostics.isSafeToCopy(Paths.get("/tmp/unsafe")), is(false));
    }

    @Test
    public void itIsNotSafeToCopyAPathBelowAnIgnoredPath() throws Exception {
        assertThat(transformationDiagnostics.isSafeToCopy(Paths.get("/tmp/unsafe/some/way/down")), is(false));
    }

    @Test
    public void itIsSafeToCopyAPathThatIsAbove() throws Exception {
        assertThat(transformationDiagnostics.isSafeToCopy(Paths.get("/tmp")), is(true));
    }

    @Test
    public void copeWithPathThatContainRelativePathElements() throws Exception {
        transformationDiagnostics.ignorePathAndEverythingBelow(Paths.get("/tmp/donotcare/../anotherunsafe/"));
        assertThat(transformationDiagnostics.isSafeToCopy(Paths.get("/tmp/anotherunsafe")), is(false));
    }
}
