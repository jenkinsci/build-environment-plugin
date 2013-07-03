package org.jenkinsci.plugins.buildenvironment.data;

import java.util.TreeMap;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

public class EnvVarsData extends Data {

    public EnvVarsData(AbstractProject<?, ?> project, AbstractBuild<?, ?> build, String name, String id) {
        super(project, build, name, id);
        initializeDataMap();
    }

    @SuppressWarnings("deprecation")
    private void initializeDataMap() {
        this.data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.
        data.putAll(this.getBuild().getEnvVars());
    }
}
