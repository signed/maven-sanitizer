package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import org.apache.maven.model.Model;
import org.hamcrest.Matcher;

import java.util.List;

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
        if( !projectMatcher.matches(infectedProject.fullyPopulatedModel)){
            return;
        }

        for (Extractor<MavenModelElement> extractor : extractors) {
            transform(infectedProject, extractor, diagnosticsWriter);
        }
    }

    private void transform(InfectedProject infectedProject, Extractor<MavenModelElement> extractor, DiagnosticsWriter diagnosticsWriter) {
        for (MavenModelElement element : extractor.elements(infectedProject.fullyPopulatedModel)) {
            action.performOn(extractor.elements(infectedProject.targetModelToWrite));
            selector.executeActionOnMatch(element, action, diagnosticsWriter, infectedProject);
        }
    }
}