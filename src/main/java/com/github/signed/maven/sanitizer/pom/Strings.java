package com.github.signed.maven.sanitizer.pom;

public class Strings {

    public boolean matching(String dependency, String toDrop) {
        return null == dependency || dependency.equals(toDrop);
    }
}