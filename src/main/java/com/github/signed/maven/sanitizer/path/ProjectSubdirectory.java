package com.github.signed.maven.sanitizer.path;

import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProjectSubdirectory implements PathsProvider {

    private final List<String> subdirectories;
    private final String groupId;
    private final String artifactId;

    public ProjectSubdirectory(String groupId, String artifactId, String... subdirectories) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.subdirectories = Arrays.asList(subdirectories);
    }

    @Override
    public Iterable<Path> paths(MavenProject mavenProject) {
        if(!groupId.equals(mavenProject.getGroupId()) || !artifactId.equals(mavenProject.getArtifactId())){
            return Collections.emptyList();
        }

        List<Path> paths = new ArrayList<>();
        for (String subdirectory : subdirectories) {
            paths.add(mavenProject.getBasedir().toPath().resolve(subdirectory));
        }
        return paths;
    }
}
