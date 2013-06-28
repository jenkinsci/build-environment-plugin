package org.jenkinsci.plugins.buildenvtest;

import java.io.IOException;

import jenkins.model.Jenkins;

import hudson.model.Cause;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;

import org.jenkinsci.plugins.buildenvironment.actions.BuildEnvironmentBuildAction;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class BuildEnvironmentTest extends HudsonTestCase {

    @SuppressWarnings("deprecation")
    @Test
    public void test1() throws IOException {
        assertNotNull(Jenkins.getInstance().getPlugin("build-environment"));
        FreeStyleProject testJob = Jenkins.getInstance().createProject(
                FreeStyleProject.class, "test_job1");
        testJob.scheduleBuild(null);
        BuildEnvironmentBuildAction buildEnvAction = testJob.getAction(BuildEnvironmentBuildAction.class);
        assertNotNull(buildEnvAction);
        assertEquals(3, buildEnvAction.getDataHoldersList().size());
        
        
    }
}
