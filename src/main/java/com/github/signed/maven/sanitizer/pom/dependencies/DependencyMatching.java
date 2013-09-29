package com.github.signed.maven.sanitizer.pom.dependencies;

import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.Selector;
import com.github.signed.maven.sanitizer.pom.Strings;
import org.apache.maven.model.Dependency;

public class DependencyMatching implements Selector<Dependency> {

    private String groupId;
    private String artifactId;
    private String type;

    public DependencyMatching(String groupId, String artifactId, String type) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.type = type;
    }

    @Override
    public void executeActionOnMatch(Dependency candidate, Action<Dependency> action) {
        Strings strings = new Strings();
        if( !strings.matching(candidate.getGroupId(), groupId)){
            return;
        }
        if( !strings.matching(candidate.getArtifactId(), artifactId)){
            return;
        }

        if( !strings.matching(candidate.getType(), type)){
            return;
        }

        action.perform(candidate);
    }
}
