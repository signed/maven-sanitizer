package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CleanRoom;
import com.github.signed.maven.sanitizer.pom.SanitizedPom;

import java.nio.file.Path;

import static com.github.signed.maven.sanitizer.path.BasePath.baseDirectoryOf;

public class CleanRoomGuard {
    private final ModelSerializer serializer = new ModelSerializer();
    private final CleanRoom cleanRoom;
    private final DiagnosticsReader diagnoses;

    public CleanRoomGuard(CleanRoom cleanRoom, DiagnosticsReader diagnoses) {
        this.cleanRoom = cleanRoom;
        this.diagnoses = diagnoses;
    }

    public void process(CleanRoomApplication cleanRoomApplication) {
        for (SanitizedPom sanitizedPom : cleanRoomApplication.sanitizedPoms()) {
            toCleanRoom(sanitizedPom);
        }
        for (Path path : cleanRoomApplication.pathsToCopy()) {
            if( diagnoses.isSafeToCopy(path)){
                cleanRoom.copyContentBelowInAssociatedDirectory(path);
            }
        }
    }

    public void toCleanRoom(SanitizedPom sanitizedPom) {
        cleanRoom.createDirectoryAssociatedTo(baseDirectoryOf(sanitizedPom.sourceMavenProject));
        String content = serializer.serializeModelToString(sanitizedPom.transformedModelToWrite);
        Path pom = sanitizedPom.sourceMavenProject.getFile().toPath();
        cleanRoom.writeStringToPathAssociatedWith(pom, content);
    }
}
