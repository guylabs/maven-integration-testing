package org.apache.maven.it;

import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;

public class MavenITmngXXXXConcurrentWritesToResolverStatusFile extends AbstractMavenIntegrationTestCase {

    private static final String RESOURCE_NAME = "mng-xxxx-concurrent-writes-to-resolver-status-file";

    public MavenITmngXXXXConcurrentWritesToResolverStatusFile() {
        super("[4.0.0-alpha-1,)");
    }

    public void testConcurrentWritesToResolverStatusFile() throws Exception {
        final File testDir = ResourceExtractor.simpleExtractResources(getClass(), File.separator + RESOURCE_NAME);

        // Install the plugin to the local repository
        Verifier verifier = new Verifier(new File(testDir.getAbsolutePath(), "plugin-project").getAbsolutePath());
        verifier.deleteDirectory("../repo");
        verifier.setLocalRepo(new File(testDir.getAbsolutePath(), "repo").getAbsolutePath());
        verifier.executeGoal("install");
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        // Run several times such that the resolver-status.properties file gets corrupted by concurrent writes
        for (int i = 0; i < 10; i++) {

            verifier = new Verifier(new File(testDir.getAbsolutePath()).getAbsolutePath());
            verifier.setLocalRepo(new File(testDir.getAbsolutePath(), "repo").getAbsolutePath());
            verifier.addCliOption("--non-recursive");
            verifier.addCliOption("-U");

            // Set settings.xml file and add debug output including thread names
            verifier.filterFile("settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties());
            verifier.addCliOption("-s");
            verifier.addCliOption("settings.xml");
            verifier.addCliOption("-X");
            verifier.addCliOption("-Dorg.slf4j.simpleLogger.showThreadName=true");
            verifier.addCliOption( "-Daether.updateCheckManager.sessionState=false" );

            // Execute the plugin mojo
            verifier.executeGoal("mng-xxxx-concurrent-writes-to-resolver-status-file:plugin:plugin-mojo");
            verifier.verifyErrorFreeLog();
            verifier.resetStreams();
        }
    }
}
