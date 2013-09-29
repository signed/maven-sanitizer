package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.github.signed.maven.sanitizer.pom.SanitizedPom;

import java.nio.file.Path;

import static com.github.signed.maven.sanitizer.path.BasePath.baseDirectoryOf;

public class CleanRoomGuard {
    private final ModelSerializer serializer = new ModelSerializer();
    private final CleanRoom cleanRoom;

    public CleanRoomGuard(CleanRoom cleanRoom) {
        this.cleanRoom = cleanRoom;
    }

    public void process(CleanRoomApplication cleanRoomApplication) {
        for (SanitizedPom sanitizedPom : cleanRoomApplication.sanitizedPoms()) {
            toCleanRoom(sanitizedPom);
        }
        for (Path path : cleanRoomApplication.pathsToCopy()) {
            cleanRoom.copyContentBelowInAssociatedDirectory(path);
        }
    }

    public void toCleanRoom(SanitizedPom sanitizedPom) {
        cleanRoom.createDirectoryAssociatedTo(baseDirectoryOf(sanitizedPom.sourceMavenProject));
        String content = serializer.serializeModelToString(sanitizedPom.transformedModelToWrite);
        Path pom = sanitizedPom.sourceMavenProject.getFile().toPath();
        cleanRoom.writeStringToPathAssociatedWith(pom, content);
    }
}
