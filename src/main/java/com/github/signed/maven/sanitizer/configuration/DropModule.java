package com.github.signed.maven.sanitizer.configuration;

import com.github.signed.maven.sanitizer.CollectPathsToCopy;
import com.github.signed.maven.sanitizer.MavenMatchers;
import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.DefaultModelTransformer;
import com.github.signed.maven.sanitizer.pom.Extractor;
import com.github.signed.maven.sanitizer.pom.ModelTransformer;
import com.github.signed.maven.sanitizer.pom.PomTransformer;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyMatching;
import com.github.signed.maven.sanitizer.pom.modules.Module;
import com.github.signed.maven.sanitizer.pom.modules.ModuleWithName;
import com.github.signed.maven.sanitizer.pom.modules.ModulesFromReactor;
import org.apache.maven.model.Model;
import org.hamcrest.Matcher;

import java.util.Collections;
import java.util.List;

public class DropModule implements Configuration {
    private final String moduleName;
    private final String groupId;
    private final String artifactId;

    public DropModule(String moduleName, String groupId, String artifactId) {
        this.moduleName = moduleName;
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    @Override
    public void configure(CollectPathsToCopy projectFiles) {
        //nothing to do in this case
    }

    @Override
    public void configure(PomTransformer pomTransformation) {
        List<Extractor<Module>> moduleExtractors = Collections.<Extractor<Module>>singletonList(new ModulesFromReactor());
        ModuleWithName moduleWithName = new ModuleWithName(new Module(moduleName));
        Action<Module> action = new com.github.signed.maven.sanitizer.pom.modules.DropModule();
        Matcher<Model> any = MavenMatchers.anything();
        ModelTransformer transformer = new DefaultModelTransformer<>(moduleWithName, action, any, moduleExtractors);

        pomTransformation.addTransformer(transformer);
        pomTransformation.addTransformer(ForDependencyReferences.inAllModules().focusOnActualDependenciesAndDependencyManagement().drop().referencesTo(DependencyMatching.dependencyWith(groupId, artifactId)).build());
    }
}
