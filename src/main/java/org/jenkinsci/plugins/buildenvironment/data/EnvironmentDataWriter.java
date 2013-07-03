package org.jenkinsci.plugins.buildenvironment.data;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.listeners.RunListener;

import org.jenkinsci.plugins.buildenvironment.actions.BuildEnvironmentBuildAction;

/**
 * Listens for builds and writes data when one is about to start.
 * 
 * @author yboev
 * 
 */
@Extension
public class EnvironmentDataWriter extends RunListener<AbstractBuild<?, ?>> {
    
//    private static final Logger LOGGER = Logger.getLogger(EnvironmentDataWriter.class.getName());
    @Override
    public void onCompleted(AbstractBuild<?, ?> build, TaskListener listener) {
                
        // add action for the current build
        build.addAction(new BuildEnvironmentBuildAction(build));
    }
    
}
