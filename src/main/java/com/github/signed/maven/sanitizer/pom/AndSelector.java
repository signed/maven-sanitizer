package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;

public class AndSelector<MavenModelElement> implements Selector<MavenModelElement> {

    private final Selector<MavenModelElement> first;
    private final Selector<MavenModelElement> second;

    public AndSelector(Selector<MavenModelElement> first, Selector<MavenModelElement> second){
        this.first = first;
        this.second = second;
    }
    @Override
    public boolean executeActionOnMatch(Patient<MavenModelElement> patient, DiagnosticsWriter diagnosticsWriter, InfectedProject infectedProject) {
        return first.executeActionOnMatch(patient, diagnosticsWriter, infectedProject) && second.executeActionOnMatch(patient, diagnosticsWriter, infectedProject);
    }
}
