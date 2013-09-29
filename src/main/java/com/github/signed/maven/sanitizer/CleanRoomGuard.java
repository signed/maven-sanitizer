package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.CleanRoom;

import java.nio.file.Path;

public class CleanRoomGuard {

    private final CleanRoom cleanRoom;

    public CleanRoomGuard(CleanRoom cleanRoom) {
        this.cleanRoom = cleanRoom;
    }

    public void copyToCleanRoom(CleanRoomApplication cleanRoomApplication) {
        for (Path path : cleanRoomApplication.pathsToCopy()) {
            cleanRoom.copyContentBelowInAssociatedDirectory(path);
        }
    }
}
