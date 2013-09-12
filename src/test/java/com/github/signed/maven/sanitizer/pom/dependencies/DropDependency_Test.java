package com.github.signed.maven.sanitizer.pom.dependencies;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import com.google.common.collect.Lists;
import org.apache.maven.model.Dependency;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class DropDependency_Test {


    public static class DependencyBuilder{

        public static DependencyBuilder anyDependency(){
            DependencyBuilder dependencyBuilder = new DependencyBuilder();
            dependencyBuilder.withGroupId("org.example");
            dependencyBuilder.withArtifactId("artifact");
            return dependencyBuilder;
        }

        private String groupId;
        private String artifact;
        private String version;

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
            return withVersion(format("${%s}", propertyName));
        }

        public Dependency build(){
            Dependency dependency = new Dependency();
            dependency.setGroupId(groupId);
            dependency.setArtifactId(artifact);
            dependency.setVersion(version);
            return dependency;
        }
    }

    @Test
    public void dropDependencyEvenIfVersionIsAProperty() throws Exception {
        DropDependency dropDependency = new DropDependency();
        String resolvedVersion = "5.11.4";
        DependencyBuilder base = DependencyBuilder.anyDependency();
        Dependency resolved = base.withVersion(resolvedVersion).build();
        Dependency vanillaPomWithoutResolvedProperties = base.withVersionAsProperty("a.maven.property.with.the.version.number").build();

        List<Dependency> target = Lists.newArrayList(vanillaPomWithoutResolvedProperties);


        dropDependency.performOn(target);
        dropDependency.perform(resolved);

        assertThat(target.size(), CoreMatchers.is(0));
    }
}
