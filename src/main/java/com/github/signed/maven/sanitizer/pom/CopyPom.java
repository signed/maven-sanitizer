package com.github.signed.maven.sanitizer.pom;

import com.github.signed.maven.sanitizer.ModelSerializer;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CopyPom {
    private final ModelSerializer serializer = new ModelSerializer();
    private final CleanRoom cleanRoom;
    private final List<ModelTransformer> modelTransformers = new ArrayList<>();

    public CopyPom(CleanRoom cleanRoom) {
        this.cleanRoom = cleanRoom;
    }

    public void addTransformer(ModelTransformer transformer) {
        modelTransformers.add(transformer);
    }

    public void from(MavenProject mavenProject) {
        Path pom = mavenProject.getFile().toPath();
        Model model = mavenProject.getModel();
        Model targetModelToWrite = mavenProject.getOriginalModel().clone();
        TheModels models = new TheModels(model, targetModelToWrite);
        transform(models);
        String content = serializer.serializeModelToString(targetModelToWrite);
        cleanRoom.writeStringToPathAssociatedWith(pom, content);
    }

    private void transform(TheModels models) {
        for (ModelTransformer modelTransformer : modelTransformers) {
            modelTransformer.transform(models);
        }
    }
}