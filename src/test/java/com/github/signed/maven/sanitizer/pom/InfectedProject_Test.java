package com.github.signed.maven.sanitizer.pom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import com.github.signed.maven.model.MavenProjectBuilder;
import com.github.signed.maven.sanitizer.pom.dependencies.DependencyBuilder;

public class InfectedProject_Test {


    @Test
    public void returnAbsentForNotManagedDependency() throws Exception {
        MavenProject reactorProject = MavenProjectBuilder.reactor().build();
        MavenProjectBuilder childBuilder = MavenProjectBuilder.childOf(reactorProject);
        Dependency unmanagedDependency = DependencyBuilder.anyDependency().build();
        childBuilder.addDependencyWithManagedVersion(unmanagedDependency);
        MavenProject child = childBuilder.build();

        assertThat(new InfectedProject(child).getEntryInDependencyManagementFor(unmanagedDependency).isPresent(), is(false));
    }
}