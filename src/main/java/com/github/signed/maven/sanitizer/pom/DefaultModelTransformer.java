package com.github.signed.maven.sanitizer.pom;

import java.util.List;

import org.apache.maven.model.Model;
import org.hamcrest.Matcher;
import com.github.signed.maven.sanitizer.DiagnosticsWriter;

public class DefaultModelTransformer<MavenModelElement> implements ModelTransformer {
    private final Selector<MavenModelElement> selector;
    private final List<Extractor<MavenModelElement>> extractors;
    private final Action<MavenModelElement> action;
    private final Matcher<Model> projectMatcher;
    private final ModelElementCombiner<MavenModelElement> modelElementCombiner;

    public DefaultModelTransformer(Selector<MavenModelElement> selector, Action<MavenModelElement> action, Matcher<Model> projectMatcher, List<Extractor<MavenModelElement>> extractors, ModelElementCombiner<MavenModelElement> modelElementCombiner) {
        this.selector = selector;
        this.extractors = extractors;
        this.action = action;
        this.projectMatcher = projectMatcher;
        this.modelElementCombiner = modelElementCombiner;
    }

    @Override
    public void transform(InfectedProject infectedProject, DiagnosticsWriter diagnosticsWriter) {
        if (!projectMatcher.matches(infectedProject.fullyPopulatedModel)) {
            return;
        }

        for (Extractor<MavenModelElement> extractor : extractors) {
            Iterable<MavenModelElement> asWritten = extractor.elements(infectedProject.modelAsWritten);
            Iterable<MavenModelElement> fullyPopulated = extractor.elements(infectedProject.fullyPopulatedModel);
            Iterable<Patient<MavenModelElement>> patients = modelElementCombiner.combine(asWritten, fullyPopulated);
            transform(infectedProject, extractor, diagnosticsWriter, patients);
        }
    }

    private void transform(InfectedProject infectedProject, Extractor<MavenModelElement> extractor, DiagnosticsWriter diagnosticsWriter, Iterable<Patient<MavenModelElement>> patients) {
        for (Patient<MavenModelElement> patient : patients) {
            action.performOn(extractor.elements(infectedProject.targetModelToWrite));
            if(selector.executeActionOnMatch(patient, diagnosticsWriter, infectedProject)){
                action.perform(patient.fullyPopulated());
            }
        }
    }
}