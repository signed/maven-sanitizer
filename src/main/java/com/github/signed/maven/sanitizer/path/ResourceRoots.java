package com.github.signed.maven.sanitizer.path;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.signed.maven.sanitizer.path.BasePath.baseDirectoryOf;

public class ResourceRoots implements PathsProvider {
    @Override
    public Iterable<Path> paths(MavenProject mavenProject) {
        List<Resource> resources = mavenProject.getResources();
        Set<Path> resourcesToCopy = new HashSet<>();
        for (Resource resource : resources) {
            Path sourceResource = baseDirectoryOf(mavenProject).resolve(resource.getDirectory());
            resourcesToCopy.add(sourceResource);
        }
        return resourcesToCopy;
    }
}
