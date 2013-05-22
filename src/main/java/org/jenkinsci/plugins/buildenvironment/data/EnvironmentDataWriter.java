package org.jenkinsci.plugins.buildenvironment.data;

import static hudson.model.Result.SUCCESS;

import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.jenkinsci.plugins.buildenvironment.actions.BuildEnvironmentBuildAction;
import org.jenkinsci.plugins.buildenvironment.actions.utils.Constants;


import hudson.Extension;
import hudson.XmlFile;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

/**
 * Listens for builds and writes data when one is about to start.
 * 
 * @author yboev
 * 
 */
@Extension
public class EnvironmentDataWriter extends RunListener<AbstractBuild<?, ?>> {
    HashMap<String, String> data;
    
    private static final Logger LOGGER = Logger.getLogger(EnvironmentDataWriter.class.getName());
    @Override
    public void onCompleted(AbstractBuild<?, ?> build, TaskListener listener) {
        AbstractProject<?, ?> project = build.getParent();
        
        String projectName = project.getName();
        String buildId = build.getId();
        
        ProjectData projectInfo = new ProjectData(project, build);
        SlaveData slaveInfo = new SlaveData(project, build);
        EnvVarsData envVars = new EnvVarsData(project, build);
         
        LOGGER.info("writing environment data file now...");
        try {
            new XmlFile(Hudson.XSTREAM, new File(slaveInfo.getFullFileName())).write(slaveInfo.createDataMap());
            LOGGER.info("environment slave file written!");
        } catch (IOException e) {
            LOGGER.info("writing slave data file unsuccessful!! :(");
            e.printStackTrace();
        } catch (NullPointerException e) {
            LOGGER.info("writing slave data file unsuccessful!! :(");
        }
        try {
            new XmlFile(Hudson.XSTREAM, new File(projectInfo.getFullFileName())).write(projectInfo.createDataMap());
            LOGGER.info("environment project file written!");
        } catch (IOException e) {
            LOGGER.info("writing project data file unsuccessful!! :(");
            e.printStackTrace();
        } catch (NullPointerException e) {
            LOGGER.info("writing project data file unsuccessful!! :(");
        }
        try {
            new XmlFile(Hudson.XSTREAM, new File(envVars.getFullFileName())).write(envVars.createDataMap());
            LOGGER.info("environment variables file written!");
        } catch (IOException e) {
            LOGGER.info("writing env vars data file unsuccessful!! :(");
            e.printStackTrace();
        } catch (NullPointerException e) {
            LOGGER.info("writing env vars data file unsuccessful!! :(");
        }
        
        // add action for the current build
        build.addAction(new BuildEnvironmentBuildAction(build));
    }
    
}
