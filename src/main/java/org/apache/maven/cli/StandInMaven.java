package org.apache.maven.cli;

import org.apache.maven.DefaultMaven;
import org.apache.maven.Maven;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequestPopulator;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.model.building.ModelProblem;
import org.apache.maven.model.building.ModelProblemUtils;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.building.UrlModelSource;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.StringUtils;
import org.eclipse.aether.RepositorySystemSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StandInMaven implements Maven{

    private final Logger logger = LoggerFactory.getLogger(StandInMaven.class);

    private MavenExecutionRequestPopulator executionRequestPopulator;
    private PlexusContainer container;
    private Maven maven;
    private List<MavenProject> projectsForMavenReactor;

    public void setOriginalMaven(Maven maven){
        this.maven = maven;
    }

    public void setContainer(PlexusContainer container){
        this.container = container;
    }

    public void setExecutionRequestPopulator(MavenExecutionRequestPopulator executionRequestPopulator){
        this.executionRequestPopulator = executionRequestPopulator;
    }

    @Override
    public MavenExecutionResult execute(MavenExecutionRequest request) {

        try {
            executionRequestPopulator.populateDefaults(request);
            without(request);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new DefaultMavenExecutionResult();
    }

    private void without(MavenExecutionRequest request) throws ComponentLookupException, ProjectBuildingException {
        RepositorySystemSession repositorySession = ((DefaultMaven) maven).newRepositorySession(request);

        ProjectBuildingRequest projectBuildingRequest = request.getProjectBuildingRequest();
        projectBuildingRequest.setRepositorySession(repositorySession);

        ProjectBuilder projectBuilder = container.lookup(ProjectBuilder.class);

        projectsForMavenReactor = getProjectsForMavenReactor(request, projectBuilder);
    }

    public List<MavenProject> getProjects(){
        return projectsForMavenReactor;
    }

    private List<MavenProject> getProjectsForMavenReactor(MavenExecutionRequest request, ProjectBuilder projectBuilder)
            throws ProjectBuildingException {
        List<MavenProject> projects = new ArrayList<>();

        // We have no POM file.
        //
        if (request.getPom() == null) {
            ModelSource modelSource = new UrlModelSource(DefaultMaven.class.getResource("project/standalone.xml"));
            MavenProject project =
                    projectBuilder.build(modelSource, request.getProjectBuildingRequest()).getProject();
            project.setExecutionRoot(true);
            projects.add(project);
            request.setProjectPresent(false);
            return projects;
        }

        List<File> files = Arrays.asList(request.getPom().getAbsoluteFile());
        collectProjects(projects, files, request, projectBuilder);
        return projects;
    }

    private void collectProjects(List<MavenProject> projects, List<File> files, MavenExecutionRequest request, ProjectBuilder projectBuilder)
            throws ProjectBuildingException {
        ProjectBuildingRequest projectBuildingRequest = request.getProjectBuildingRequest();

        List<ProjectBuildingResult> results = projectBuilder.build(files, request.isRecursive(), projectBuildingRequest);

        boolean problems = false;

        for (ProjectBuildingResult result : results) {
            projects.add(result.getProject());

            if (!result.getProblems().isEmpty() && logger.isWarnEnabled()) {
                logger.warn("");
                logger.warn("Some problems were encountered while building the effective model for "
                        + result.getProject().getId());

                for (ModelProblem problem : result.getProblems()) {
                    String location = ModelProblemUtils.formatLocation(problem, result.getProjectId());
                    logger.warn(problem.getMessage() + (StringUtils.isNotEmpty(location) ? " @ " + location : ""));
                }

                problems = true;
            }
        }

        if (problems) {
            logger.warn("");
            logger.warn("It is highly recommended to fix these problems"
                    + " because they threaten the stability of your build.");
            logger.warn("");
            logger.warn("For this reason, future Maven versions might no"
                    + " longer support building such malformed projects.");
            logger.warn("");
        }
    }
}