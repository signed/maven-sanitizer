package com.github.signed.maven.sanitizer.pom.dependencies;

import static java.lang.String.format;

import org.apache.maven.model.Dependency;

public class DependencyBuilder {

    private static String propertyWith(String propertyName) {
        return format("${%s}", propertyName);
    }

    public static DependencyBuilder anyDependency(){
        DependencyBuilder dependencyBuilder = new DependencyBuilder();
        dependencyBuilder.withGroupId("org.example");
        dependencyBuilder.withArtifactId("artifact");
        return dependencyBuilder;
    }

    private String groupId;
    private String artifact;
    private String version;
    private String scope;

    private DependencyBuilder withArtifactId(String artifact) {
        this.artifact = artifact;
        return this;
    }

    public DependencyBuilder withGroupId(String groupId){
        this.groupId = groupId;
        return this;
    }

    public DependencyBuilder withVersion(String version){
        this.version = version;
        return this;
    }

    public DependencyBuilder withVersionAsProperty(String propertyName){
        return withVersion(propertyWith(propertyName));
    }

    public DependencyBuilder withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public DependencyBuilder withScopeAsProperty(String propertyName) {
        return withScope(propertyWith(propertyName));
    }

    public Dependency build(){
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifact);
        dependency.setVersion(version);
        dependency.setScope(scope);
        return dependency;
    }
}
