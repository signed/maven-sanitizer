package com.github.signed.maven.sanitizer.pom;

public class FullyPopulatedOnly<MavenModelElement> implements Patient<MavenModelElement> {
    private final MavenModelElement fullyPopulated;

    public FullyPopulatedOnly(MavenModelElement fullyPopulated){
        this.fullyPopulated = fullyPopulated;
    }

    @Override
    public MavenModelElement getAsWritten() {
        throw new RuntimeException("not supported");
    }

    @Override
    public final MavenModelElement fullyPopulated(){
        return fullyPopulated;
    }
}
