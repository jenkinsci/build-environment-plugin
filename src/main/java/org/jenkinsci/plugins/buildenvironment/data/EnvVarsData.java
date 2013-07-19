package org.jenkinsci.plugins.buildenvironment.data;

import java.util.TreeMap;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

/**
 * Represents information about the environment variables used during the build.
 * 
 * @author yboev
 * 
 */
public class EnvVarsData extends Data {

    /**
     * Constructor method.
     * 
     * @param project
     *            AbstractProject.
     * @param build
     *            AbstractBuild.
     * @param name
     *            Name as String.
     * @param id
     *            ID as String.
     */
    public EnvVarsData(AbstractProject<?, ?> project,
            AbstractBuild<?, ?> build, String name, String id) {
        super(name, id);
        initializeDataMap(build);
    }

    /**
     * Initialize method. Here data is added to the map.
     */
    @SuppressWarnings("deprecation")
    private void initializeDataMap(AbstractBuild<?, ?> build) {
        this.data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.
        data.putAll(build.getEnvVars());
    }
}
