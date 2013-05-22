package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.TreeMap;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Node;
import hudson.model.JobPropertyDescriptor;
import hudson.slaves.NodeProperty;

public class EnvVarsData extends Data {

    public EnvVarsData(AbstractProject<?, ?> project, AbstractBuild<?, ?> build) {
        super(project, build);
    }

    @Override
    protected String getFileName() {
        return "env_vars.xml";
    }

    protected TreeMap<String, String> createDataMap() {
        TreeMap<String, String> data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.
        
        data.putAll(this.getBuild().getEnvVars());
        return data;
    }
}
