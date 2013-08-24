package org.apache.maven.cli;

import com.github.signed.maven.sanitizer.Fixture;
import com.google.inject.AbstractModule;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.maven.DefaultMaven;
import org.apache.maven.Maven;
import org.apache.maven.cli.event.DefaultEventSpyContext;
import org.apache.maven.cli.event.ExecutionEventLogger;
import org.apache.maven.cli.logging.Slf4jLoggerManager;
import org.apache.maven.cli.logging.Slf4jStdoutLogger;
import org.apache.maven.cli.transfer.ConsoleMavenTransferListener;
import org.apache.maven.cli.transfer.QuietMavenTransferListener;
import org.apache.maven.cli.transfer.Slf4jMavenTransferListener;
import org.apache.maven.eventspy.internal.EventSpyDispatcher;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequestPopulator;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.internal.LifecycleWeaveBuilder;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.building.ModelProblem;
import org.apache.maven.model.building.ModelProblemUtils;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.building.UrlModelSource;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuilder;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingResult;
import org.apache.maven.settings.building.SettingsProblem;
import org.apache.maven.settings.building.SettingsSource;
import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.logging.LoggerManager;
import org.codehaus.plexus.util.StringUtils;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.transfer.TransferListener;
import org.junit.Test;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BringPlexusOnline_Test {

    private final Logger logger = LoggerFactory.getLogger(BringPlexusOnline_Test.class);
    private final Fixture fixture = new Fixture();

    private static final String EXT_CLASS_PATH = "maven.ext.class.path";
    public static final String userHome = System.getProperty("user.home");
    public static final File userMavenConfigurationHome = new File(userHome, ".m2");
    public static final File DEFAULT_USER_SETTINGS_FILE = new File(userMavenConfigurationHome, "settings.xml");

    public static final File DEFAULT_GLOBAL_SETTINGS_FILE =
            new File(System.getProperty("maven.home", System.getProperty("user.dir", "")), "conf/settings.xml");

    private EventSpyDispatcher eventSpyDispatcher;
    private ILoggerFactory slf4jLoggerFactory = LoggerFactory.getILoggerFactory();
    private LoggerManager plexusLoggerManager = new Slf4jLoggerManager();
    private Logger slf4jLogger;
    private Maven maven;
    private MavenExecutionRequestPopulator executionRequestPopulator;
    private SettingsBuilder settingsBuilder;
    private DefaultSecDispatcher dispatcher;
    private ModelProcessor modelProcessor;
    private MavenSession session;
    private ProjectBuilder projectBuilder;

    @Test
    public void bringPlexusOnline() throws Exception {
        System.setProperty("user.dir", fixture.multiModule.getParent());
        MavenCli.CliRequest cliRequest = new MavenCli.CliRequest(new String[0], null);
        initialize(cliRequest);
        cli(cliRequest);
        PlexusContainer container = container(cliRequest);
        settings(cliRequest);
        populateRequest(cliRequest);

        executionRequestPopulator.populateDefaults(cliRequest.request);

        RepositorySystemSession repositorySession = ((DefaultMaven) maven).newRepositorySession(cliRequest.request);

        ProjectBuildingRequest projectBuildingRequest = cliRequest.request.getProjectBuildingRequest();
        projectBuildingRequest.setRepositorySession(repositorySession);

        projectBuilder = container.lookup(ProjectBuilder.class);


        List<MavenProject> projects = getProjectsForMavenReactor(cliRequest.request);

        MavenExecutionResult result = new DefaultMavenExecutionResult();
        session = new MavenSession(container, repositorySession, cliRequest.request, result);
        session.setProjects(projects);

        MavenProject project = getProjectWith(projects, "artifact");
        Dependency dependency = getDependencyWith(project, "junit");
        assertThat(dependency.getScope(), is("test"));
    }

    private MavenProject getProjectWith(List<MavenProject> projects, String artifactId) {
        for (MavenProject project : projects) {
            if (artifactId.equals(project.getArtifactId())) {
                return project;
            }
        }
        return null;
    }

    private Dependency getDependencyWith(MavenProject project, String artifactId) {
        List<Dependency> dependencies = project.getDependencies();
        for (Dependency dependency : dependencies) {
            if(artifactId.equals(dependency.getArtifactId())){
                return dependency;
            }
        }
        return null;
    }

    private void collectProjects(List<MavenProject> projects, List<File> files, MavenExecutionRequest request)
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

    private List<MavenProject> getProjectsForMavenReactor(MavenExecutionRequest request)
            throws ProjectBuildingException {
        List<MavenProject> projects = new ArrayList<MavenProject>();

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
        collectProjects(projects, files, request);
        return projects;
    }

    private MavenExecutionRequest populateRequest(MavenCli.CliRequest cliRequest) {
        MavenExecutionRequest request = cliRequest.request;
        CommandLine commandLine = cliRequest.commandLine;
        String workingDirectory = cliRequest.workingDirectory;
        boolean quiet = cliRequest.quiet;
        boolean showErrors = cliRequest.showErrors;

        String[] deprecatedOptions = {"up", "npu", "cpu", "npr"};
        for (String deprecatedOption : deprecatedOptions) {
            if (commandLine.hasOption(deprecatedOption)) {
                slf4jLogger.warn("Command line option -" + deprecatedOption
                        + " is deprecated and will be removed in future Maven versions.");
            }
        }

        // ----------------------------------------------------------------------
        // Now that we have everything that we need we will fire up plexus and
        // bring the maven component to life for use.
        // ----------------------------------------------------------------------

        if (commandLine.hasOption(CLIManager.BATCH_MODE)) {
            request.setInteractiveMode(false);
        }

        boolean noSnapshotUpdates = false;
        if (commandLine.hasOption(CLIManager.SUPRESS_SNAPSHOT_UPDATES)) {
            noSnapshotUpdates = true;
        }

        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------

        @SuppressWarnings("unchecked")
        List<String> goals = commandLine.getArgList();

        boolean recursive = true;

        // this is the default behavior.
        String reactorFailureBehaviour = MavenExecutionRequest.REACTOR_FAIL_FAST;

        if (commandLine.hasOption(CLIManager.NON_RECURSIVE)) {
            recursive = false;
        }

        if (commandLine.hasOption(CLIManager.FAIL_FAST)) {
            reactorFailureBehaviour = MavenExecutionRequest.REACTOR_FAIL_FAST;
        } else if (commandLine.hasOption(CLIManager.FAIL_AT_END)) {
            reactorFailureBehaviour = MavenExecutionRequest.REACTOR_FAIL_AT_END;
        } else if (commandLine.hasOption(CLIManager.FAIL_NEVER)) {
            reactorFailureBehaviour = MavenExecutionRequest.REACTOR_FAIL_NEVER;
        }

        if (commandLine.hasOption(CLIManager.OFFLINE)) {
            request.setOffline(true);
        }

        boolean updateSnapshots = false;

        if (commandLine.hasOption(CLIManager.UPDATE_SNAPSHOTS)) {
            updateSnapshots = true;
        }

        String globalChecksumPolicy = null;

        if (commandLine.hasOption(CLIManager.CHECKSUM_FAILURE_POLICY)) {
            globalChecksumPolicy = MavenExecutionRequest.CHECKSUM_POLICY_FAIL;
        } else if (commandLine.hasOption(CLIManager.CHECKSUM_WARNING_POLICY)) {
            globalChecksumPolicy = MavenExecutionRequest.CHECKSUM_POLICY_WARN;
        }

        File baseDirectory = new File(workingDirectory, "").getAbsoluteFile();

        // ----------------------------------------------------------------------
        // Profile Activation
        // ----------------------------------------------------------------------

        List<String> activeProfiles = new ArrayList<String>();

        List<String> inactiveProfiles = new ArrayList<String>();

        if (commandLine.hasOption(CLIManager.ACTIVATE_PROFILES)) {
            String[] profileOptionValues = commandLine.getOptionValues(CLIManager.ACTIVATE_PROFILES);
            if (profileOptionValues != null) {
                for (String profileOptionValue : profileOptionValues) {
                    StringTokenizer profileTokens = new StringTokenizer(profileOptionValue, ",");

                    while (profileTokens.hasMoreTokens()) {
                        String profileAction = profileTokens.nextToken().trim();

                        if (profileAction.startsWith("-") || profileAction.startsWith("!")) {
                            inactiveProfiles.add(profileAction.substring(1));
                        } else if (profileAction.startsWith("+")) {
                            activeProfiles.add(profileAction.substring(1));
                        } else {
                            activeProfiles.add(profileAction);
                        }
                    }
                }
            }
        }

        TransferListener transferListener;

        if (quiet) {
            transferListener = new QuietMavenTransferListener();
        } else if (request.isInteractiveMode() && !cliRequest.commandLine.hasOption(CLIManager.LOG_FILE)) {
            //
            // If we're logging to a file then we don't want the console transfer listener as it will spew
            // download progress all over the place
            //
            transferListener = getConsoleTransferListener();
        } else {
            transferListener = getBatchTransferListener();
        }

        ExecutionListener executionListener = new ExecutionEventLogger();
        executionListener = eventSpyDispatcher.chainListener(executionListener);

        String alternatePomFile = null;
        if (commandLine.hasOption(CLIManager.ALTERNATE_POM_FILE)) {
            alternatePomFile = commandLine.getOptionValue(CLIManager.ALTERNATE_POM_FILE);
        }

        File userToolchainsFile;
        if (commandLine.hasOption(CLIManager.ALTERNATE_USER_TOOLCHAINS)) {
            userToolchainsFile = new File(commandLine.getOptionValue(CLIManager.ALTERNATE_USER_TOOLCHAINS));
            userToolchainsFile = resolveFile(userToolchainsFile, workingDirectory);
        } else {
            userToolchainsFile = MavenCli.DEFAULT_USER_TOOLCHAINS_FILE;
        }

        request.setBaseDirectory(baseDirectory).setGoals(goals)
                .setSystemProperties(cliRequest.systemProperties)
                .setUserProperties(cliRequest.userProperties)
                .setReactorFailureBehavior(reactorFailureBehaviour) // default: fail fast
                .setRecursive(recursive) // default: true
                .setShowErrors(showErrors) // default: false
                .addActiveProfiles(activeProfiles) // optional
                .addInactiveProfiles(inactiveProfiles) // optional
                .setExecutionListener(executionListener)
                .setTransferListener(transferListener) // default: batch mode which goes along with interactive
                .setUpdateSnapshots(updateSnapshots) // default: false
                .setNoSnapshotUpdates(noSnapshotUpdates) // default: false
                .setGlobalChecksumPolicy(globalChecksumPolicy) // default: warn
                .setUserToolchainsFile(userToolchainsFile);

        if (alternatePomFile != null) {
            File pom = resolveFile(new File(alternatePomFile), workingDirectory);
            if (pom.isDirectory()) {
                pom = new File(pom, "multimodule/parent/pom.xml");
            }

            request.setPom(pom);
        } else {
            File pom = modelProcessor.locatePom(baseDirectory);

            if (pom.isFile()) {
                request.setPom(pom);
            }
        }

        if ((request.getPom() != null) && (request.getPom().getParentFile() != null)) {
            request.setBaseDirectory(request.getPom().getParentFile());
        }

        if (commandLine.hasOption(CLIManager.RESUME_FROM)) {
            request.setResumeFrom(commandLine.getOptionValue(CLIManager.RESUME_FROM));
        }

        if (commandLine.hasOption(CLIManager.PROJECT_LIST)) {
            String[] values = commandLine.getOptionValues(CLIManager.PROJECT_LIST);
            List<String> projects = new ArrayList<String>();
            for (String value : values) {
                String[] tmp = StringUtils.split(value, ",");
                projects.addAll(Arrays.asList(tmp));
            }
            request.setSelectedProjects(projects);
        }

        if (commandLine.hasOption(CLIManager.ALSO_MAKE)
                && !commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS)) {
            request.setMakeBehavior(MavenExecutionRequest.REACTOR_MAKE_UPSTREAM);
        } else if (!commandLine.hasOption(CLIManager.ALSO_MAKE)
                && commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS)) {
            request.setMakeBehavior(MavenExecutionRequest.REACTOR_MAKE_DOWNSTREAM);
        } else if (commandLine.hasOption(CLIManager.ALSO_MAKE)
                && commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS)) {
            request.setMakeBehavior(MavenExecutionRequest.REACTOR_MAKE_BOTH);
        }

        String localRepoProperty = request.getUserProperties().getProperty(MavenCli.LOCAL_REPO_PROPERTY);

        if (localRepoProperty == null) {
            localRepoProperty = request.getSystemProperties().getProperty(MavenCli.LOCAL_REPO_PROPERTY);
        }

        if (localRepoProperty != null) {
            request.setLocalRepositoryPath(localRepoProperty);
        }

        final String threadConfiguration = commandLine.hasOption(CLIManager.THREADS)
                ? commandLine.getOptionValue(CLIManager.THREADS)
                : request.getSystemProperties().getProperty(
                MavenCli.THREADS_DEPRECATED); // TODO: Remove this setting. Note that the int-tests use it

        if (threadConfiguration != null) {
            request.setPerCoreThreadCount(threadConfiguration.contains("C"));
            if (threadConfiguration.contains("W")) {
                LifecycleWeaveBuilder.setWeaveMode(request.getUserProperties());
            }
            request.setThreadCount(threadConfiguration.replace("C", "").replace("W", "").replace("auto", ""));
        }

        request.setCacheNotFound(true);
        request.setCacheTransferError(false);

        return request;
    }

    private void cli(MavenCli.CliRequest cliRequest)
            throws Exception {
        //
        // Parsing errors can happen during the processing of the arguments and we prefer not having to check if the logger is null
        // and construct this so we can use an SLF4J logger everywhere.
        //
        slf4jLogger = new Slf4jStdoutLogger();

        CLIManager cliManager = new CLIManager();

        try {
            cliRequest.commandLine = cliManager.parse(cliRequest.args);
        } catch (ParseException e) {
            System.err.println("Unable to parse command line options: " + e.getMessage());
            cliManager.displayHelp(System.out);
            throw e;
        }

        if (cliRequest.commandLine.hasOption(CLIManager.HELP)) {
            cliManager.displayHelp(System.out);
            throw new MavenCli.ExitException(0);
        }

        if (cliRequest.commandLine.hasOption(CLIManager.VERSION)) {
            System.out.println(CLIReportingUtils.showVersion());
            throw new MavenCli.ExitException(0);
        }
    }

    private void initialize(MavenCli.CliRequest cliRequest) {
        if (cliRequest.workingDirectory == null) {
            cliRequest.workingDirectory = System.getProperty("user.dir");
        }

        //
        // Make sure the Maven home directory is an absolute path to save us from confusion with say drive-relative
        // Windows paths.
        //
        String mavenHome = System.getProperty("maven.home");

        if (mavenHome != null) {
            System.setProperty("maven.home", new File(mavenHome).getAbsolutePath());
        }
    }

    private void settings(MavenCli.CliRequest cliRequest)
            throws Exception {
        File userSettingsFile;

        if (cliRequest.commandLine.hasOption(CLIManager.ALTERNATE_USER_SETTINGS)) {
            userSettingsFile = new File(cliRequest.commandLine.getOptionValue(CLIManager.ALTERNATE_USER_SETTINGS));
            userSettingsFile = resolveFile(userSettingsFile, cliRequest.workingDirectory);

            if (!userSettingsFile.isFile()) {
                throw new FileNotFoundException("The specified user settings file does not exist: "
                        + userSettingsFile);
            }
        } else {
            userSettingsFile = DEFAULT_USER_SETTINGS_FILE;
        }

        File globalSettingsFile;

        if (cliRequest.commandLine.hasOption(CLIManager.ALTERNATE_GLOBAL_SETTINGS)) {
            globalSettingsFile =
                    new File(cliRequest.commandLine.getOptionValue(CLIManager.ALTERNATE_GLOBAL_SETTINGS));
            globalSettingsFile = resolveFile(globalSettingsFile, cliRequest.workingDirectory);

            if (!globalSettingsFile.isFile()) {
                throw new FileNotFoundException("The specified global settings file does not exist: "
                        + globalSettingsFile);
            }
        } else {
            globalSettingsFile = DEFAULT_GLOBAL_SETTINGS_FILE;
        }

        cliRequest.request.setGlobalSettingsFile(globalSettingsFile);
        cliRequest.request.setUserSettingsFile(userSettingsFile);

        SettingsBuildingRequest settingsRequest = new DefaultSettingsBuildingRequest();
        settingsRequest.setGlobalSettingsFile(globalSettingsFile);
        settingsRequest.setUserSettingsFile(userSettingsFile);
        settingsRequest.setSystemProperties(cliRequest.systemProperties);
        settingsRequest.setUserProperties(cliRequest.userProperties);

        eventSpyDispatcher.onEvent(settingsRequest);

        slf4jLogger.debug("Reading global settings from "
                + getSettingsLocation(settingsRequest.getGlobalSettingsSource(), settingsRequest.getGlobalSettingsFile()));
        slf4jLogger.debug("Reading user settings from "
                + getSettingsLocation(settingsRequest.getUserSettingsSource(), settingsRequest.getUserSettingsFile()));

        SettingsBuildingResult settingsResult = settingsBuilder.build(settingsRequest);

        eventSpyDispatcher.onEvent(settingsResult);

        executionRequestPopulator.populateFromSettings(cliRequest.request, settingsResult.getEffectiveSettings());

        if (!settingsResult.getProblems().isEmpty() && slf4jLogger.isWarnEnabled()) {
            slf4jLogger.warn("");
            slf4jLogger.warn("Some problems were encountered while building the effective settings");

            for (SettingsProblem problem : settingsResult.getProblems()) {
                slf4jLogger.warn(problem.getMessage() + " @ " + problem.getLocation());
            }

            slf4jLogger.warn("");
        }
    }

    private PlexusContainer container(MavenCli.CliRequest cliRequest)
            throws Exception {
        if (cliRequest.classWorld == null) {
            cliRequest.classWorld = new ClassWorld("plexus.core", Thread.currentThread().getContextClassLoader());
        }

        DefaultPlexusContainer container = null;

        ContainerConfiguration cc = new DefaultContainerConfiguration()
                .setClassWorld(cliRequest.classWorld)
                .setRealm(setupContainerRealm(cliRequest))
                .setClassPathScanning(PlexusConstants.SCANNING_INDEX)
                .setAutoWiring(true)
                .setName("maven");

        container = new DefaultPlexusContainer(cc, new AbstractModule() {
            protected void configure() {
                bind(ILoggerFactory.class).toInstance(slf4jLoggerFactory);
            }
        });

        // NOTE: To avoid inconsistencies, we'll use the TCCL exclusively for lookups
        container.setLookupRealm(null);

        container.setLoggerManager(plexusLoggerManager);

        customizeContainer(container);

        container.getLoggerManager().setThresholds(cliRequest.request.getLoggingLevel());

        Thread.currentThread().setContextClassLoader(container.getContainerRealm());

        eventSpyDispatcher = container.lookup(EventSpyDispatcher.class);

        DefaultEventSpyContext eventSpyContext = new DefaultEventSpyContext();
        Map<String, Object> data = eventSpyContext.getData();
        data.put("plexus", container);
        data.put("workingDirectory", cliRequest.workingDirectory);
        data.put("systemProperties", cliRequest.systemProperties);
        data.put("userProperties", cliRequest.userProperties);
        data.put("versionProperties", CLIReportingUtils.getBuildProperties());
        eventSpyDispatcher.init(eventSpyContext);

        // refresh logger in case container got customized by spy
        slf4jLogger = slf4jLoggerFactory.getLogger(this.getClass().getName());

        maven = container.lookup(Maven.class);

        executionRequestPopulator = container.lookup(MavenExecutionRequestPopulator.class);

        modelProcessor = createModelProcessor(container);

        settingsBuilder = container.lookup(SettingsBuilder.class);

        dispatcher = (DefaultSecDispatcher) container.lookup(SecDispatcher.class, "maven");

        return container;
    }

    private ClassRealm setupContainerRealm(MavenCli.CliRequest cliRequest)
            throws Exception {
        ClassRealm containerRealm = null;

        String extClassPath = cliRequest.userProperties.getProperty(EXT_CLASS_PATH);
        if (extClassPath == null) {
            extClassPath = cliRequest.systemProperties.getProperty(EXT_CLASS_PATH);
        }

        if (StringUtils.isNotEmpty(extClassPath)) {
            String[] jars = StringUtils.split(extClassPath, File.pathSeparator);

            if (jars.length > 0) {
                ClassRealm coreRealm = cliRequest.classWorld.getClassRealm("plexus.core");
                if (coreRealm == null) {
                    coreRealm = (ClassRealm) cliRequest.classWorld.getRealms().iterator().next();
                }

                ClassRealm extRealm = cliRequest.classWorld.newRealm("maven.ext", null);

                slf4jLogger.debug("Populating class realm " + extRealm.getId());

                for (String jar : jars) {
                    File file = resolveFile(new File(jar), cliRequest.workingDirectory);

                    slf4jLogger.debug("  Included " + file);

                    extRealm.addURL(file.toURI().toURL());
                }

                extRealm.setParentRealm(coreRealm);

                containerRealm = extRealm;
            }
        }

        return containerRealm;
    }

    protected void customizeContainer(PlexusContainer container) {
    }

    protected ModelProcessor createModelProcessor(PlexusContainer container)
            throws ComponentLookupException {
        return container.lookup(ModelProcessor.class);
    }

    static File resolveFile(File file, String workingDirectory) {
        if (file == null) {
            return null;
        } else if (file.isAbsolute()) {
            return file;
        } else if (file.getPath().startsWith(File.separator)) {
            // drive-relative Windows path
            return file.getAbsoluteFile();
        } else {
            return new File(workingDirectory, file.getPath()).getAbsoluteFile();
        }
    }

    private Object getSettingsLocation(SettingsSource source, File file) {
        if (source != null) {
            return source.getLocation();
        }
        return file;
    }

    protected TransferListener getConsoleTransferListener() {
        return new ConsoleMavenTransferListener(System.out);
    }

    protected TransferListener getBatchTransferListener() {
        return new Slf4jMavenTransferListener();
    }


}