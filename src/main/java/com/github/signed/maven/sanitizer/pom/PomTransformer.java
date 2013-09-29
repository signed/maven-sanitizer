package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.DiagnosticsWriter;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;

public class PomTransformer {
    private final List<ModelTransformer> modelTransformers = new ArrayList<>();
    private final DiagnosticsWriter diagnosis;

    public PomTransformer(DiagnosticsWriter diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void addTransformer(ModelTransformer transformer) {
        modelTransformers.add(transformer);
    }

    public SanitizedPom transformPomIn(MavenProject mavenProject) {
        TheModels models = new TheModels(mavenProject.getModel(), mavenProject.getOriginalModel().clone());
        transform(models);
        return new SanitizedPom(mavenProject, models.targetModelToWrite);
    }

    private void transform(TheModels models) {
        for (ModelTransformer modelTransformer : modelTransformers) {
            modelTransformer.transform(models, diagnosis);
        }
    }
}