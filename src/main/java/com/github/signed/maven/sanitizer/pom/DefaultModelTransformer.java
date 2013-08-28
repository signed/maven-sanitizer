package com.github.signed.maven.sanitizer.pom;

public class DefaultModelTransformer<MavenModelElement> implements ModelTransformer {
    private final Critic<MavenModelElement> critic;
    private final Extractor<MavenModelElement> extractor;
    private final Action<MavenModelElement> action;

    public DefaultModelTransformer(Critic<MavenModelElement> critic, Extractor<MavenModelElement> extractor, Action<MavenModelElement> action) {
        this.critic = critic;
        this.extractor = extractor;
        this.action = action;
    }

    @Override
    public void transform(TheModels models) {
        for (MavenModelElement element : extractor.elements(models.fullyPopulatedModel)) {
            action.performOn(extractor.elements(models.targetModelToWrite));
            critic.criticise(element, action);
        }
    }
}
