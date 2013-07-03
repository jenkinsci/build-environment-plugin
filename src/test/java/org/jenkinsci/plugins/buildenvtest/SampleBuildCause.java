package org.jenkinsci.plugins.buildenvtest;

import hudson.model.Cause;

/**
 * This class contains the cause for restarting a single job.
 * 
 * @author yboev
 * 
 */
public class SampleBuildCause extends Cause {
    
    @Override
    public String getShortDescription() {
        return "Test Build cause";
    }
}
