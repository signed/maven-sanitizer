package com.github.signed.maven.sanitizer.pom;

public class DefaultModelTransformer<MavenModelElement> implements ModelTransformer {
    private final Critic<MavenModelElement> critic;
    private final Extractor<MavenModelElement> extractor;
    private final Transformation<MavenModelElement> transformation;

    public DefaultModelTransformer(Critic<MavenModelElement> critic, Extractor<MavenModelElement> extractor, Transformation<MavenModelElement> transformation) {
        this.critic = critic;
        this.extractor = extractor;
        this.transformation = transformation;
    }

    @Override
    public void transform(TheModels models) {
        for (MavenModelElement element : extractor.elements(models.fullyPopulatedModel)) {
            transformation.performOn(extractor.elements(models.targetModelToWrite));
            critic.criticise(element, transformation);
        }
    }
}
