package com.github.signed.maven.sanitizer.pom;

public class Patient<MavenModelElement>{
    private final MavenModelElement asWritten;
    private final MavenModelElement fullyPopulated;

    public Patient(MavenModelElement asWritten, MavenModelElement fullyPopulated){
        this.asWritten = asWritten;
        this.fullyPopulated = fullyPopulated;
    }

    public MavenModelElement getAsWritten(){
        return asWritten;
    }

    public final MavenModelElement fullyPouplated(){
        return fullyPopulated;
    }
}
