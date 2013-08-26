package com.github.signed.maven.sanitizer;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

public class CopyProject {
    private final CopyPom copyPom;
    private final CleanRoom cleanRoom;

    public CopyProject(CleanRoom cleanRoom, CopyPom copyPom) {
        this.cleanRoom = cleanRoom;
        this.copyPom = copyPom;
    }

    void copy(MavenProject mavenProject) {
        cleanRoom.createDirectoryAssociatedTo(baseDirectoryOf(mavenProject));

        copyPom.from(mavenProject);
        copySourceRootsOf(mavenProject);
        copyResourcesOf(mavenProject);
    }

    private void copySourceRootsOf(MavenProject mavenProject) {
        List<String> compileSourceRoots = mavenProject.getCompileSourceRoots();
        for (String compileSourceRoot : compileSourceRoots) {
            Path sourceCompileSourceRoot = baseDirectoryOf(mavenProject).resolve(compileSourceRoot);
            cleanRoom.copyContentBelowInAssociatedDirectory(sourceCompileSourceRoot);
        }
    }

    private void copyResourcesOf(MavenProject mavenProject) {
        List<Resource> resources = mavenProject.getResources();
        Set<Path> alreadyProcessed = new HashSet<>();
        for (Resource resource : resources) {
            Path sourceResource = baseDirectoryOf(mavenProject).resolve(resource.getDirectory());
            if( !alreadyProcessed.contains(sourceResource)) {
                alreadyProcessed.add(sourceResource);
                cleanRoom.copyContentBelowInAssociatedDirectory(sourceResource);
            }
        }
    }

    private Path baseDirectoryOf(MavenProject mavenProject) {
        return mavenProject.getBasedir().toPath();
    }

}