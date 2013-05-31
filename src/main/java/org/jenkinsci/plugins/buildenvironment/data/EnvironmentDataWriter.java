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
                
        // add action for the current build
        build.addAction(new BuildEnvironmentBuildAction(build));
    }
    
}
