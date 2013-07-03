package org.jenkinsci.plugins.buildenvtest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jenkins.model.Jenkins;

import hudson.model.FreeStyleProject;

import org.jenkinsci.plugins.buildenvironment.actions.BuildEnvironmentBuildAction;
import org.jenkinsci.plugins.buildenvironment.actions.utils.Constants;
import org.jenkinsci.plugins.buildenvironment.actions.utils.StringPair;
import org.jenkinsci.plugins.buildenvironment.data.Data;
import org.jenkinsci.plugins.buildenvironment.data.DataDifferenceObject;
import org.jenkinsci.plugins.buildenvtest.SampleBuildCause;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class BuildEnvironmentTest extends HudsonTestCase {

    @Test
    public void test1() throws IOException {
        assertNotNull(Jenkins.getInstance().getPlugin("build-environment"));
        FreeStyleProject testJob = Jenkins.getInstance().createProject(
                FreeStyleProject.class, "test_job1");
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
        BuildEnvironmentBuildAction buildEnvAction1 = testJob.getBuildByNumber(1).getAction(BuildEnvironmentBuildAction.class);
        BuildEnvironmentBuildAction buildEnvAction2 = testJob.getBuildByNumber(2).getAction(BuildEnvironmentBuildAction.class);
        assertNotNull(buildEnvAction1);
        assertEquals(3, buildEnvAction1.getDataHoldersList().size());
        assertNotNull(buildEnvAction1.getBuildsWithAction());
        assertNotNull(buildEnvAction1.getBuilds());
        assertEquals(buildEnvAction1.getBuildsWithAction().size(), 2);
        
        assertTrue(buildEnvAction1.getBuilds().size() >= buildEnvAction1.getBuildsWithAction().size());
        assertEquals(buildEnvAction1.getBuild(), buildEnvAction1.getBuild1());
        assertEquals(buildEnvAction1.getBuild(), buildEnvAction1.getBuild2());
        
        assertNotNull(buildEnvAction1.getAbstractProject());
        
        assertEquals(buildEnvAction1.getBackgroundColor(true), Constants.getBackgroundDifferenceColorAsString());
        assertEquals(buildEnvAction1.getBackgroundColor(false), Constants.getBackgroundNoDifferenceColorAsString());
        
        assertTrue(buildEnvAction1.getDifferentCount(buildEnvAction2.getDataHoldersList().get(0)) >= 0);
        
        assertFalse(buildEnvAction2.isDifferentFromPrevious(buildEnvAction2.getDataHoldersList().get(2), "Project name"));
        assertTrue(buildEnvAction2.isDifferentFromPrevious(buildEnvAction2.getDataHoldersList().get(0), "BUILD_NUMBER"));
        
        for(Data currentData : buildEnvAction1.getDataHoldersList()) {
            assertNotNull(currentData.getBuild());
            assertNotNull(currentData.getId());
            assertNotNull(currentData.getName());
            assertNotNull(currentData.getData());
            assertNotNull(currentData.getProject());
            assertTrue(currentData.getData().size() > 4);
        }
        
        List<DataDifferenceObject> diff = buildEnvAction1.getDifference();
        assertNotNull(diff);
        for(DataDifferenceObject currentDiff : diff) {
           assertNotNull(currentDiff);
           assertTrue(currentDiff.getMap().size() > 0);
           assertNotNull(currentDiff.getName());
           assertEquals(currentDiff.getDifferentCount(), 0);
        }
        
        trueFalseToYesNoTest(buildEnvAction1);
        
        stringPairTest();     
        
    }

    private void trueFalseToYesNoTest(
            BuildEnvironmentBuildAction buildEnvAction1) {
        assertEquals("yes", buildEnvAction1.trueFalseToYesNo(true));
        assertEquals("no", buildEnvAction1.trueFalseToYesNo(false));
    }

    private void stringPairTest() {
        final String first = "first";
        final String second = "second";
                
        StringPair sp = new StringPair(first, second);
        assertEquals(first, sp.getFirst());
        assertEquals(second, sp.getSecond());
        assertTrue(sp.areDifferent());
    }
}
