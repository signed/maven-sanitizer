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

    public DefaultModelTransformer(Selector<MavenModelElement> selector, Action<MavenModelElement> action, Matcher<Model> projectMatcher, List<Extractor<MavenModelElement>> extractors) {
        this.selector = selector;
        this.extractors = extractors;
        this.action = action;
        this.projectMatcher = projectMatcher;
    }

    @Override
    public void transform(InfectedProject infectedProject, DiagnosticsWriter diagnosticsWriter) {
        if (!projectMatcher.matches(infectedProject.fullyPopulatedModel)) {
            return;
        }

        for (Extractor<MavenModelElement> extractor : extractors) {
            transform(infectedProject, extractor, diagnosticsWriter);
        }
    }

    private void transform(InfectedProject infectedProject, Extractor<MavenModelElement> extractor, DiagnosticsWriter diagnosticsWriter) {
        ModelElementCombiner<MavenModelElement> modelElementCombiner = new RefusingCombiner<>();
        Iterable<MavenModelElement> asWritten = extractor.elements(infectedProject.modelAsWritten);
        Iterable<MavenModelElement> fullyPopulated = extractor.elements(infectedProject.fullyPopulatedModel);

        for (Patient<MavenModelElement> patient : modelElementCombiner.combine(asWritten, fullyPopulated)) {
            action.performOn(extractor.elements(infectedProject.targetModelToWrite));
            selector.executeActionOnMatch(patient, action, diagnosticsWriter, infectedProject);
        }
    }
}