package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.TransformationDiagnostics;
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
    public void transform(TheModels models, TransformationDiagnostics transformationDiagnostics) {
        if( !projectMatcher.matches(models.fullyPopulatedModel)){
            return;
        }

        for (Extractor<MavenModelElement> extractor : extractors) {
            transform(models, extractor);
        }
    }

    private void transform(TheModels models, Extractor<MavenModelElement> currentExtractor) {
        for (MavenModelElement element : currentExtractor.elements(models.fullyPopulatedModel)) {
            action.performOn(currentExtractor.elements(models.targetModelToWrite));
            selector.executeActionOnMatch(element, action);
        }
    }
}
