package com.github.signed.maven.sanitizer;

import com.github.signed.maven.sanitizer.pom.Action;
import com.github.signed.maven.sanitizer.pom.CopyPom;
import com.github.signed.maven.sanitizer.pom.DefaultModelTransformer;
import com.github.signed.maven.sanitizer.pom.Extractor;
import com.github.signed.maven.sanitizer.pom.ModelTransformer;
import com.github.signed.maven.sanitizer.pom.modules.DropModule;
import com.github.signed.maven.sanitizer.pom.modules.ModuleWithName;
import com.github.signed.maven.sanitizer.pom.modules.ModulesFromReactor;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.maven.cli.MavenFacade;
import org.apache.maven.model.Model;
import com.github.signed.maven.sanitizer.pom.modules.Module;
import org.apache.maven.project.MavenProject;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class DropModuleSteps {

    private final TemporaryFolder folder = new TemporaryFolder();
    private final Fixture fixture = new Fixture();
    private Path source;
    private Path destination;
    private Configuration configuration;
    private String nameOfModuleToBeDropped;

    @Before
    public void createDestinationDirectory() throws IOException {
        folder.create();
        destination = folder.getRoot().toPath();
    }

    @After
    public void destroyDestinationDirectory(){
        folder.delete();
    }

    @Given("^the multi module sample in src/test/resources$")
    public void theMultiModuleSampleInSrcTestResources() throws Throwable {
        source = fixture.multiModule.containingDirectory;
    }

    @When("^I configure the module (.*) to be dropped$")
    public void iConfigureTheModuleToBeDropped(final String moduleName) throws Throwable {
        nameOfModuleToBeDropped = moduleName;
        this.configuration = new Configuration() {
            @Override
            public void configure(CopyProjectFiles projectFiles) {
                //nothing to do in this case
            }

            @Override
            public void configure(CopyPom copyPom) {
                List<Extractor<Module>> moduleExtractors = Collections.<Extractor<Module>>singletonList(new ModulesFromReactor());
                ModuleWithName moduleWithName = new ModuleWithName(new Module(nameOfModuleToBeDropped));
                Action<Module> action = new DropModule();
                Matcher<Model> any = MavenMatchers.anything();
                ModelTransformer transformer = new DefaultModelTransformer<>(moduleWithName, action, any, moduleExtractors );

                copyPom.addTransformer(transformer);
            }
        };
    }

    @When("^sanitize the maven project file$")
    public void sanitizeTheMavenProjectFile() throws Throwable {
        MavenSanitizer sanitizer = new MavenSanitizer(source, destination, configuration);
        sanitizer.configure();
        sanitizer.sanitize();
    }

    @Then("^the reactor pom does not contain the module any more$")
    public void theReactorPomDoesNotContainTheModuleAnyMore() throws Throwable {
        List<MavenProject> mavenProjects = new MavenFacade().getMavenProjects(destination);


        String moduleName = "multimodule";
        for (MavenProject mavenProject : mavenProjects) {
            if(moduleName.equals(mavenProject.getName())){
                Model originalModel = mavenProject.getOriginalModel();
                assertThat(originalModel.getModules(), not(hasItem(nameOfModuleToBeDropped)));
            }
        }
        Assert.fail("there was no module with the expected name "+moduleName);
    }
}
