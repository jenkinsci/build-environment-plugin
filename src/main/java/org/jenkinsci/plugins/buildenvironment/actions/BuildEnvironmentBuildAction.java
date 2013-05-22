package org.jenkinsci.plugins.buildenvironment.actions;

import java.io.IOException;
import java.util.TreeMap;

import org.jenkinsci.plugins.buildenvironment.actions.utils.Constants;
import org.jenkinsci.plugins.buildenvironment.data.EnvVarsData;
import org.jenkinsci.plugins.buildenvironment.data.ProjectData;
import org.jenkinsci.plugins.buildenvironment.data.SlaveData;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;

public class BuildEnvironmentBuildAction implements Action  {

    /**
     * The current build.
     */
    private AbstractBuild<?, ?> build;
    private AbstractProject<?, ?> project;

    /**
     * Constructor method.
     * 
     * @param build
     *            build for which the action is constructed.
     */
    public BuildEnvironmentBuildAction(Run<?, ?> build) {
        super();
        this.build = (AbstractBuild<?, ?>) build;
        this.project = (AbstractProject<?, ?>) build.getParent();
    }

    public String getDisplayName() {
        return Constants.NAME;
    }

    public String getIconFileName() {
        return Constants.ICONFILENAME;
    }

    public String getUrlName() {
        return Constants.URL;
    }
    
    public TreeMap<String, String> getProjectMap() throws IOException {
        return new ProjectData(this.project, this.build).getData();
    }
    
    public TreeMap<String, String> getSlaveMap() throws IOException {
        return new SlaveData(this.project, this.build).getData();
    }
    
    public TreeMap<String, String> getEnvVarsMap() throws IOException {
        return new EnvVarsData(this.project, this.build).getData();
    }

    public String getSearchUrl() {
        return Constants.URL;
    }
   
}
