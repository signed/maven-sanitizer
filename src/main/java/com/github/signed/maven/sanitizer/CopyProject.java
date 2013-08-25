package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.List;

public class CopyProject {
    private final CopyPom copyPom;
    private final CleanRoom cleanRoom;

    public CopyProject(CleanRoom cleanRoom, CopyPom copyPom) {
        this.cleanRoom = cleanRoom;
        this.copyPom = copyPom;
    }

    void copy(MavenProject mavenProject) {
        Path baseDir = mavenProject.getBasedir().toPath();
        cleanRoom.createDirectoryAssociatedTo(baseDir);

        copyPom(mavenProject);
        copySourceRoots(mavenProject, baseDir);
        copyResources(mavenProject, baseDir);
    }

    private void copyPom(MavenProject mavenProject) {
        copyPom.from(mavenProject);
    }

    private void copySourceRoots(MavenProject mavenProject, Path baseDir) {
        List<String> compileSourceRoots = mavenProject.getCompileSourceRoots();
        for (String compileSourceRoot : compileSourceRoots) {
            Path sourceCompileSourceRoot = baseDir.resolve(compileSourceRoot);
            cleanRoom.copyContentBelowInAssociatedDirectory(sourceCompileSourceRoot);
        }
    }

    private void copyResources(MavenProject mavenProject, Path baseDir) {
        List<Resource> resources = mavenProject.getResources();
        for (Resource resource : resources) {
            Path sourceResource = baseDir.resolve(resource.getDirectory());
            cleanRoom.copyContentBelowInAssociatedDirectory(sourceResource);
        }
    }
}