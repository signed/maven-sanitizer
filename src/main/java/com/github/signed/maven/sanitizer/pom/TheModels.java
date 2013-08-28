package com.github.signed.maven.sanitizer.pom;

import org.apache.maven.model.Model;

public class TheModels {

    public final Model fullyPopulatedModel;
    public final Model targetModelToWrite;

    public TheModels(Model model, Model targetModelToWrite) {
        this.fullyPopulatedModel = model;
        this.targetModelToWrite = targetModelToWrite;
    }
}
