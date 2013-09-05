package com.github.signed.maven.sanitizer.pom;

import org.apache.maven.project.MavenProject;
import org.hamcrest.Matcher;

public class DefaultModelTransformer<MavenModelElement> implements ModelTransformer {
    private final Selector<MavenModelElement> selector;
    private final Extractor<MavenModelElement> extractor;
    private final Action<MavenModelElement> action;
    private final Matcher<MavenProject> projectMatcher;

    public DefaultModelTransformer(Selector<MavenModelElement> selector, Extractor<MavenModelElement> extractor, Action<MavenModelElement> action, Matcher<MavenProject> projectMatcher) {
        this.selector = selector;
        this.extractor = extractor;
        this.action = action;
        this.projectMatcher = projectMatcher;
    }

    @Override
    public void transform(TheModels models) {
        if( !projectMatcher.matches(models.fullyPopulatedModel)){
            return;
        }
        for (MavenModelElement element : extractor.elements(models.fullyPopulatedModel)) {
            action.performOn(extractor.elements(models.targetModelToWrite));
            selector.executeActionOnMatch(element, action);
        }
    }
}
