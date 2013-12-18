package org.jenkinsci.plugins.buildenvtest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jenkins.model.Jenkins;

import hudson.model.Result;
import hudson.model.FreeStyleProject;

import org.jenkinsci.plugins.buildenvironment.actions.BuildEnvironmentBuildAction;
import org.jenkinsci.plugins.buildenvironment.actions.utils.Constants;
import org.jenkinsci.plugins.buildenvironment.data.Data;
import org.jenkinsci.plugins.buildenvironment.data.DataDifferenceObject;
import org.jenkinsci.plugins.buildenvtest.SampleBuildCause;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class BuildEnvironmentTest {
    
    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();
    
    final String jobName = "test_job1";
    FreeStyleProject testJob;

    BuildEnvironmentBuildAction buildEnvAction1;
    BuildEnvironmentBuildAction buildEnvAction2;

    @Test
    public void testBuildActionNatural() throws IOException,
            InterruptedException {
        setUpTestEnvironmentAndVariables();
        checkBuildEnvironmentActions();
        checkDataHolders();
        checkDifference();
        checkExport();
        trueFalseToYesNoTest();
    }

    @Test
    public void testBuildActionStatic() throws IOException {
        setUpTestEnvironmentAndVariables();
        assertEquals(buildEnvAction1.getDisplayName(), Constants.NAME);
        assertEquals(buildEnvAction1.getIconFileName(),
                Constants.MENUICONFILENAME);
        assertEquals(buildEnvAction1.getSummaryIconFilename(),
                Constants.SUMMARYICONFILENAME);
        assertEquals(buildEnvAction1.getUrlName(), Constants.URL);
        assertEquals(buildEnvAction1.getSearchUrl(),
                buildEnvAction1.getUrlName());
    }

    @Test
    public void testDifferenceForm() throws IOException, SAXException {
        setUpTestEnvironmentAndVariables();
        final HtmlPage page = jenkinsRule.createWebClient().goTo("job/" + this.jobName
                + "/1/compare_environment/");
        HtmlForm diffForm = page.getFormByName("diffForm");
        assertNotNull(diffForm);
        diffForm.submit();
    }

    /**
     * initializes the testjob, build it and then populates the two buildAction
     * objects.
     * 
     * @throws IOException
     */
    private void setUpTestEnvironmentAndVariables() throws IOException {
        assertNotNull(Jenkins.getInstance().getPlugin("build-environment"));
        createTestJobAndBuildIt();
        retrieveBuildEnvironmentActionFromBuilds();
    }

    private void checkDataHolders() {
        for (Data currentData : buildEnvAction1.getDataHoldersList()) {
            assertNotNull(currentData.getId());
            assertNotNull(currentData.getName());
            assertNotNull(currentData.getData());
            assertTrue(currentData.getData().size() > 4);
        }
    }

    private void checkDifference() {
        List<DataDifferenceObject> diff = buildEnvAction1.getDifference();
        assertNotNull(diff);
        for (DataDifferenceObject currentDiff : diff) {
            assertNotNull(currentDiff);
            assertTrue(currentDiff.getMap().size() > 0);
            assertNotNull(currentDiff.getName());
            assertEquals(currentDiff.getDifferentCount(), 0);
        }
    }

    private void checkExport() {
        assertNotNull(buildEnvAction1.getEnvironmentVariablesForExport());
        assertTrue(buildEnvAction1.getEnvironmentVariablesForExport().size() > 5);
        assertTrue(buildEnvAction1.getEnvironmentVariablesForExport().size() % 2 == 0);
    }

    private void checkBuildEnvironmentActions() {
        assertNotNull(buildEnvAction1);
        assertEquals(3, buildEnvAction1.getDataHoldersList().size());
        assertNotNull(buildEnvAction1.getBuildsWithAction());
        assertNotNull(buildEnvAction1.getBuilds());
        assertEquals(buildEnvAction1.getBuildsWithAction().size(), 2);

        assertTrue(buildEnvAction1.getBuilds().size() >= buildEnvAction1
                .getBuildsWithAction().size());
        assertEquals(buildEnvAction1.getBuild(), buildEnvAction1.getBuild1());
        assertEquals(buildEnvAction1.getBuild(), buildEnvAction1.getBuild2());

        assertNotNull(buildEnvAction1.getAbstractProject());

        checkColors();

        assertTrue(buildEnvAction1.getDifferentCount(buildEnvAction2
                .getDataHoldersList().get(0)) >= 0);

        assertFalse(buildEnvAction2.isDifferentFromPrevious(buildEnvAction2
                .getDataHoldersList().get(2), "Project name"));
        assertTrue(buildEnvAction2.isDifferentFromPrevious(buildEnvAction2
                .getDataHoldersList().get(0), "BUILD_NUMBER"));
    }

    private void retrieveBuildEnvironmentActionFromBuilds() {
        buildEnvAction1 = testJob.getBuildByNumber(1).getAction(
                BuildEnvironmentBuildAction.class);
        buildEnvAction2 = testJob.getBuildByNumber(2).getAction(
                BuildEnvironmentBuildAction.class);
    }

    private void createTestJobAndBuildIt() throws IOException {
        testJob = Jenkins.getInstance().createProject(FreeStyleProject.class,
                this.jobName);
        testJob.scheduleBuild(new SampleBuildCause());
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            // we have been interrupted
        }
        testJob.scheduleBuild(new SampleBuildCause());
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            // we have been interrupted
        }
        assertEquals(2, testJob.getBuilds().size());
        assertTrue(testJob.getBuildByNumber(1).getResult() != Result.FAILURE);
    }

    private void checkColors() {
        assertEquals(buildEnvAction1.getBackgroundColor(true),
                Constants.getBackgroundDifferenceColorAsString());
        assertEquals(buildEnvAction1.getBackgroundColor(false),
                Constants.getBackgroundNoDifferenceColorAsString());
    }

    private void trueFalseToYesNoTest() {
        assertEquals("yes", buildEnvAction1.trueFalseToYesNo(true));
        assertEquals("no", buildEnvAction1.trueFalseToYesNo(false));
    }
}
