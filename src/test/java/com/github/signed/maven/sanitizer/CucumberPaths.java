package com.github.signed.maven.sanitizer;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.cli.MavenFacade;
import org.apache.maven.project.MavenProject;

public class CucumberPaths {
    public static CucumberPaths CreateCucumberPaths() {
        return new CucumberPaths();
    }

    public Path source;
    public Path destination;

    private CucumberPaths() {
    }

    public SanitizedMultiModuleBuild sanitizedMultiModuleBuildBuild() {
        return new SanitizedMultiModuleBuild(source, destination);
    }

    public HamcrestInCompileScope hamcrestInCompileScope(){
        return new HamcrestInCompileScope(destination);
    }

    public DependenciesInScopeTest noDependenciesInScopeTest(){
        return new DependenciesInScopeTest(sanitizedMavenProject());
    }

    private List<MavenProject> sanitizedMavenProject(){
        return new MavenFacade().getMavenProjects(destination);
    }

}
