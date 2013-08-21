package com.github.signed.maven.sanitizer;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Iterator;

public class Application_Test {
    @Test
    public void testName() throws Exception {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(Application_Test.class.getResourceAsStream("/sample/pom.xml"));


        Iterator<Dependency> dependencyIterator = model.getDependencies().iterator();
        while (dependencyIterator.hasNext()){
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
}
