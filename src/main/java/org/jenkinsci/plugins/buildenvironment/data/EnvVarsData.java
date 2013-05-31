package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.TreeMap;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Node;
import hudson.model.JobPropertyDescriptor;
import hudson.slaves.NodeProperty;

public class EnvVarsData extends Data {

    public EnvVarsData(AbstractProject<?, ?> project, AbstractBuild<?, ?> build, String name) {
        super(project, build, name);
        initializeDataMap();
    }

    private void initializeDataMap() {
        this.data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.

        data.putAll(this.getBuild().getEnvVars());
    }
}
