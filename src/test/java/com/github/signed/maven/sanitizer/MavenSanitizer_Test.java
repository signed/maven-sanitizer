package com.github.signed.maven.sanitizer;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.FileReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;


public class MavenSanitizer_Test {
    public final Fixture fixture = new Fixture();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();


    @Test
    @Ignore("do not run on command line")
    public void copyTheStuff() throws Exception {
        String [] args = {fixture.multiModule.containingDirectory.toString(), folder.getRoot().toPath().toString()};
        MavenSanitizer.main(args);
        System.out.println("done");
    }

    @Test
    @Ignore("do not run on command line")
    public void testName() throws Exception {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(fixture.singleModule));
        model.setPomFile(fixture.singleModule);

        MavenProject project = new MavenProject(model);
        List<String> compileSourceRoots = project.getCompileSourceRoots();
        for (String compileSourceRoot : compileSourceRoots) {
            System.out.println(compileSourceRoot);
        }

        Iterator<Dependency> dependencyIterator = model.getDependencies().iterator();
        while (dependencyIterator.hasNext()) {
            Dependency dependency = dependencyIterator.next();
            if ("test".equals(dependency.getScope())) {
                dependencyIterator.remove();
            }
        }

        model.getDevelopers().clear();
        model.getContributors().clear();

        DefaultModelWriter modelWriter = new DefaultModelWriter();
        StringWriter writer = new StringWriter();
        modelWriter.write(writer, null, model);
        System.out.println("writer = " + writer);
    }

    @Test
    @Ignore("do not run on command line")
    public void embedder() throws Exception {
        System.setProperty("user.dir", fixture.multiModule.containingDirectory.toAbsolutePath().toString());
        MavenCli.main(new String[]{"compile"});
    }
}