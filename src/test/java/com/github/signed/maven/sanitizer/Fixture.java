package com.github.signed.maven.sanitizer;

import java.io.File;

public class Fixture {
    public final String testClassTargetRoot = Application_Test.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    public final File singleModule = new File(testClassTargetRoot, "sample/pom.xml").getAbsoluteFile();
    public final File multiModule = new File(testClassTargetRoot, "multimodule/pom.xml").getAbsoluteFile();
}