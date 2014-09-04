package com.github.signed.maven.sanitizer.pom.dependencies;

import static java.lang.String.format;

import org.apache.maven.model.Dependency;

public class DependencyBuilder {

    private String type;

    private static String propertyWith(String propertyName) {
        return format("${%s}", propertyName);
    }

    public static DependencyBuilder anyDependency(){
        DependencyBuilder dependencyBuilder = new DependencyBuilder();
        dependencyBuilder.withGroupId("org.example");
        dependencyBuilder.withArtifactId("artifact");
        dependencyBuilder.withVersion("43b");
        return dependencyBuilder;
    }

    public static DependencyBuilder like(Dependency dependency) {
        DependencyBuilder builder = new DependencyBuilder();
        builder.withGroupId(dependency.getGroupId());
        builder.withArtifactId(dependency.getArtifactId());
        builder.withVersion(dependency.getVersion());
        builder.withScope(dependency.getScope());
        builder.withType(dependency.getType());
        return builder;
    }

    private String groupId;
    private String artifact;
    private String version;
    private String scope;

    public DependencyBuilder withArtifactId(String artifact) {
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

    public DependencyBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public DependencyBuilder but() {
        return this;
    }

    public Dependency build(){
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifact);
        dependency.setVersion(version);
        dependency.setType(type);
        dependency.setScope(scope);
        return dependency;
    }
}
