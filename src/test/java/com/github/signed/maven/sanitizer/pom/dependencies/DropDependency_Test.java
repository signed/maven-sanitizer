package com.github.signed.maven.sanitizer.pom.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import com.google.common.collect.Lists;

public class DropDependency_Test {
    private final DropDependency dropDependency = new DropDependency();
    private final DependencyBuilder base = DependencyBuilder.anyDependency();

    @Test
    public void dropDependencyEvenIfVersionIsAProperty() throws Exception {
        String resolvedVersion = "5.11.4";
        Dependency resolved = base.withVersion(resolvedVersion).build();
        Dependency vanillaPomWithoutResolvedProperties = base.withVersionAsProperty("a.maven.property.with.the.version.number").build();

        List<Dependency> target = Lists.newArrayList(vanillaPomWithoutResolvedProperties);

        dropDependency.performOn(target);
        dropDependency.perform(resolved, null);

        assertThat(target.size(), CoreMatchers.is(0));
    }

    @Test
    public void copeWithMissingVersionNumberInVanillaPom() throws Exception {
        String resolvedVersion = "5.11.4";
        Dependency resolved = base.withVersion(resolvedVersion).build();
        Dependency vanillaPomWithoutResolvedProperties = base.withVersion(null).build();

        List<Dependency> target = Lists.newArrayList(vanillaPomWithoutResolvedProperties);

        dropDependency.performOn(target);
        dropDependency.perform(resolved, null);

        assertThat(target.size(), CoreMatchers.is(0));
    }

    @Test
    public void dropDependencyEvenIfScopeIsAProperty() throws Exception {
        Dependency resolved = base.withScope("runtime").build();
        Dependency vanillaPomWithoutResolvedProperties = base.withScopeAsProperty("a.maven.property.with.the.scope").build();

        List<Dependency> target = Lists.newArrayList(vanillaPomWithoutResolvedProperties);

        dropDependency.performOn(target);
        dropDependency.perform(resolved, null);

        assertThat(target.size(), CoreMatchers.is(0));
    }


}
