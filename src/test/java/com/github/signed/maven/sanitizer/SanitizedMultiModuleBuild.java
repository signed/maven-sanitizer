package com.github.signed.maven.sanitizer;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.cli.MavenFacade;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

public class SanitizedMultiModuleBuild {

    private final List<MavenProject> mavenProjects;
    private final SourceToDestinationTreeMapper mapper;
    private final Path source;

    public SanitizedMultiModuleBuild(Path source, Path destination) {
        this.source = source;
        mapper = new SourceToDestinationTreeMapper(source, destination);
        mavenProjects = new MavenFacade().getMavenProjects(destination);
    }

    public Path getRootOfModule(String moduleName) {
        return mapper.map(source.resolve(moduleName));
    }

    public Model warModule() {
        return getModelFor("war");
    }

    public Model reactor() {
        return getModelFor("multimodule");
    }

    public Model artifactModule() {
        return getModelFor("artifact");
    }

    public Model parentModule() {
        return getModelFor("parent");
    }

    private Model getModelFor(String moduleName) {
        for (MavenProject mavenProject : this.mavenProjects) {
            if (moduleName.equals(mavenProject.getName())) {
                return mavenProject.getOriginalModel();
            }
        }
        throw new RuntimeException("there was no module with the expected name " + moduleName);
    }
}
