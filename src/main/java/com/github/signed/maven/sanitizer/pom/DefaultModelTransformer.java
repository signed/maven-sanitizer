package com.github.signed.maven.sanitizer.pom;

public class DefaultModelTransformer<MavenModelElement> implements ModelTransformer {
    private final Selector<MavenModelElement> selector;
    private final Extractor<MavenModelElement> extractor;
    private final Action<MavenModelElement> action;

    public DefaultModelTransformer(Selector<MavenModelElement> selector, Extractor<MavenModelElement> extractor, Action<MavenModelElement> action) {
        this.selector = selector;
        this.extractor = extractor;
        this.action = action;
    }

    @Override
    public void transform(TheModels models) {
        for (MavenModelElement element : extractor.elements(models.fullyPopulatedModel)) {
            action.performOn(extractor.elements(models.targetModelToWrite));
            selector.executeActionOnMatch(element, action);
        }
    }
}
