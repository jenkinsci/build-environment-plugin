package org.jenkinsci.plugins.buildenvironment.data;

import java.util.TreeMap;

import hudson.model.AbstractBuild;

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
     * @param build
     *            AbstractBuild.
     * @param name
     *            Name as String.
     * @param id
     *            ID as String.
     */
    public EnvVarsData(AbstractBuild<?, ?> build, String name, String id) {
        super(name, id);
        initializeDataMap(build);
    }

    /**
     * Initialize method. Here data is added to the map.
     * 
     * @param build
     *            Current build.
     */
    @SuppressWarnings("deprecation")
    private void initializeDataMap(AbstractBuild<?, ?> build) {
        TreeMap<String, String> data = this.initEmptyMap();
        // get data, add it to map and return the newly created map.
        data.putAll(build.getEnvVars());
    }
}
