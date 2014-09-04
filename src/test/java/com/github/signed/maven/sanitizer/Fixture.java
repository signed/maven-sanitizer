package com.github.signed.maven.sanitizer;

import java.io.File;
import java.nio.file.Path;

public class Fixture {
    public final String testClassTargetRoot = MavenSanitizer_Test.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    public final File singleModule = new File(testClassTargetRoot, "sample/pom.xml").getAbsoluteFile();


    public final DropDependencyFixture dropDependency = new DropDependencyFixture(testClassTargetRoot);

    public final TestData multiModule = new TestData(new File(testClassTargetRoot, "multimodule/pom.xml").getAbsoluteFile());


    public static class DropDependencyFixture{
        public final TestData hamcrestInCompileScope;

        public DropDependencyFixture(String testClassTargetRoot) {
            hamcrestInCompileScope = new TestData(new File(testClassTargetRoot, "droptestscopedependencies/addversiontocompilescopedependency/pom.xml").getAbsoluteFile());
        }
    }

    public static class TestData {
        public final Path pom;
        public final File pomFile;

        public final Path containingDirectory;

        public TestData(File absoluteFile) {
            this.pomFile = absoluteFile;
            pom = this.pomFile.toPath();
            this.containingDirectory = pom.getParent();
        }

    }
}